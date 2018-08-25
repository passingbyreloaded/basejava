package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.DateUtil;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyDataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            Map<SectionType, Section> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                Section section = entry.getValue();
                dos.writeUTF(section.getClass().getSimpleName());
                if (section instanceof TextSection) {
                    dos.writeUTF(((TextSection) section).getContent());
                } else if (section instanceof ListSection) {
                    List<String> list = ((ListSection) section).getItems();
                    dos.writeInt(list.size());
                    for (String s : list) {
                        dos.writeUTF(s);
                    }
                } else if (section instanceof OrganizationSection) {
                    List<Organization> list = ((OrganizationSection) section).getOrganizations();
                    dos.writeInt(list.size());
                    for (Organization organization : list) {
                        dos.writeUTF(organization.getHomePage().getName());
                        String url = organization.getHomePage().getUrl();
                        dos.writeUTF(url == null ? "" : url);
                        List<Organization.Position> positions = organization.getPositions();
                        dos.writeInt(positions.size());
                        for (Organization.Position position : positions) {
                            dos.writeInt(position.getStartDate().getYear());
                            dos.writeUTF(position.getStartDate().getMonth().name());
                            dos.writeInt(position.getEndDate().getYear());
                            dos.writeUTF(position.getEndDate().getMonth().name());
                            dos.writeUTF(position.getTitle());
                            String description = position.getDescription();
                            dos.writeUTF(description == null ? "" : description);
                        }
                    }
                }
            }
            // TODO implements sections
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int contactsSize = dis.readInt();
            for (int i = 0; i < contactsSize; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                String className = dis.readUTF();
                if (className.equals("TextSection")) {
                    resume.addSection(sectionType, new TextSection(dis.readUTF()));
                } else if (className.equals("ListSection")) {
                    List<String> list = new ArrayList<>();
                    int listSize = dis.readInt();
                    for (int j = 0; j < listSize; j++) {
                        list.add(dis.readUTF());
                    }
                    resume.addSection(sectionType, new ListSection(list));
                } else if (className.equals("OrganizationSection")) {
                    int organizationList = dis.readInt();
                    List<Organization> organizations = new ArrayList<>();
                    for (int j = 0; j < organizationList; j++) {
                        String name = dis.readUTF();
                        String url = dis.readUTF();
                        int positionList = dis.readInt();
                        List<Organization.Position> positions = new ArrayList<>();
                        for (int k = 0; k < positionList; k++) {
                            LocalDate start = DateUtil.of(dis.readInt(), Month.valueOf(dis.readUTF()));
                            LocalDate end = DateUtil.of(dis.readInt(), Month.valueOf(dis.readUTF()));
                            String title = dis.readUTF();
                            String description = dis.readUTF();
                            positions.add(new Organization.Position(start, end, title, description.equals("") ? null : description));
                        }
                        organizations.add(new Organization(new Link(name, url.equals("") ? null : url), positions));
                    }
                    resume.addSection(sectionType, new OrganizationSection(organizations));
                }
            }
            // TODO implements sections
            return resume;
        }
    }
}
