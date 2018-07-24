package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index <= -1) {
            throw new NotExistStorageException(uuid);
        }
        return getElement(index);

    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index <= -1) {
            throw new NotExistStorageException(uuid);
        }
        deleteElement(index);
    }

    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(resume.getUuid());
        }
        addElement(resume, index);
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index <= -1) {
            throw new NotExistStorageException(resume.getUuid());
        }
        updateElement(resume, index);
    }

    protected abstract int getIndex(String uuid);

    protected abstract Resume getElement(int index);

    protected abstract void deleteElement(int index);

    protected abstract void addElement(Resume resume, int index);

    protected abstract void updateElement(Resume resume, int index);

}
