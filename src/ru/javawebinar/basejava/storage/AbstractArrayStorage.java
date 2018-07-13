package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {

    protected static final int CAPACITY = 10000;
    protected Resume[] storage = new Resume[CAPACITY];
    protected int size;

    @Override
    public int size() {
        return size;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            throw new NotExistStorageException(uuid);
        }
        return storage[index];

    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume resume) {
        int index;
        if ((index = getIndex(resume.getUuid())) >= 0) {
            throw new ExistStorageException(resume.getUuid());
        }
        if (size == storage.length) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertElement(resume, index);
        size++;
    }

    public void delete(String uuid) {
        int index;
        if ((index = getIndex(uuid)) >= 0) {
            fillDeletedElement(index);
            storage[size - 1] = null;
            size--;
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    public void update(Resume resume) {
        int index;
        if ((index = getIndex(resume.getUuid())) >= 0) {
            storage[index] = resume;
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected abstract int getIndex(String uuid);

    protected abstract void insertElement(Resume resume, int index);

    protected abstract void fillDeletedElement(int index);
}
