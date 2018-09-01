package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {

    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
                    Resume resume;
                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid =?")) {
                        ps.setString(1, uuid);
                        ResultSet rs = ps.executeQuery();
                        if (!rs.next()) {
                            throw new NotExistStorageException(uuid);
                        }
                        resume = new Resume(uuid, rs.getString("full_name"));
                    }
                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact c WHERE c.resume_uuid =?")) {
                        ps.setString(1, uuid);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            addContact(rs, resume);
                        }
                    }
                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section s WHERE s.resume_uuid =?")) {
                        ps.setString(1, uuid);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            addSection(rs, resume);
                        }
                    }
                    return resume;
                }
        );
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.<Void>execute("DELETE FROM resume r WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, resume.getFullName());
                        ps.execute();
                    }
                    insertContacts(conn, resume);
                    insertSections(conn, resume);
                    return null;
                }
        );
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name=? WHERE uuid=?")) {
                        ps.setString(1, resume.getFullName());
                        ps.setString(2, resume.getUuid());
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(resume.getUuid());
                        }
                    }
                    deleteContacts(conn, resume);
                    insertContacts(conn, resume);
                    deleteSections(conn, resume);
                    insertSections(conn, resume);
                    return null;
                }
        );
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
                    Map<String, Resume> map = new LinkedHashMap<>();
                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r ORDER BY r.full_name, r.uuid")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("uuid");
                            map.put(uuid, new Resume(uuid, rs.getString("full_name")));
                        }
                    }
                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("resume_uuid");
                            Resume resume = map.get(uuid);
                            addContact(rs, resume);
                        }
                    }
                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("resume_uuid");
                            Resume resume = map.get(uuid);
                            addSection(rs, resume);
                        }
                    }
                    return new ArrayList<>(map.values());
                }
        );
    }

    private void insertContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> e : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                SectionType type = e.getKey();
                String value = "";
                if (type == SectionType.PERSONAL || type == SectionType.OBJECTIVE) {
                    value = ((TextSection) e.getValue()).getContent();
                } else if (type == SectionType.ACHIEVEMENT || type == SectionType.QUALIFICATIONS) {
                    value = String.join("\n", ((ListSection) e.getValue()).getItems());
                }
                ps.setString(2, type.name());
                ps.setString(3, value);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection conn, Resume r) {
        sqlHelper.execute("DELETE  FROM contact WHERE resume_uuid=?", ps -> {
            ps.setString(1, r.getUuid());
            ps.execute();
            return null;
        });
    }

    private void deleteSections(Connection conn, Resume r) {
        sqlHelper.execute("DELETE  FROM section WHERE resume_uuid=?", ps -> {
            ps.setString(1, r.getUuid());
            ps.execute();
            return null;
        });
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        r.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        SectionType type = SectionType.valueOf(rs.getString("type"));
        Section value = null;
        if (type == SectionType.PERSONAL || type == SectionType.OBJECTIVE) {
            value = new TextSection(rs.getString("value"));
        } else if (type == SectionType.ACHIEVEMENT || type == SectionType.QUALIFICATIONS) {
            value = new ListSection(new ArrayList<>(Arrays.asList(rs.getString("value").split("\n"))));
        }
        r.addSection(type, value);
    }
}
