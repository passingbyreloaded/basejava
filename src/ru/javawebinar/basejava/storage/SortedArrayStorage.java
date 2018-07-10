package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void save(Resume resume) {
        int index;
        if ((index = getIndex(resume.getUuid())) >= 0) {
            System.out.println("The resume is already in the storage");
        } else if (size == storage.length) {
            System.out.println("Storage overflow");
        } else if (size == 0) {
            storage[size] = resume;
            size++;
        } else {
            int indexForInsert = index * (-1) - 1;
            System.arraycopy(storage, indexForInsert, storage, indexForInsert + 1, size - indexForInsert);
            storage[indexForInsert] = resume;
            size++;
        }
    }

    @Override
    public void update(Resume resume) {
        int index;
        if ((index = getIndex(resume.getUuid())) >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("No such resume in the storage");
        }
    }

    @Override
    public void delete(String uuid) {
        int index;
        if ((index = getIndex(uuid)) >= 0) {
            IntStream.range(index, size - 1).forEach(i -> storage[i] = storage[i + 1]);
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("No such resume in the storage");
        }
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
