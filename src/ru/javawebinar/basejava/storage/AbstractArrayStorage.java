package ru.javawebinar.basejava.storage;

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
            System.out.println("Resume " + uuid + " does not exist");
            return null;
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
            System.out.println("The resume is already in the storage");
            return;
        }
        if (size == storage.length) {
            System.out.println("Storage overflow");
            return;
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
            System.out.println("No such resume in the storage");
        }
    }

    public void update(Resume resume) {
        int index;
        if ((index = getIndex(resume.getUuid())) >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("No such resume in the storage");
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
