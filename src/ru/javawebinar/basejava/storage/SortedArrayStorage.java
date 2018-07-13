package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void insertElement(Resume resume, int index) {
        if (size == 0) {
            storage[size] = resume;
        } else {
            int indexForInsert = index * (-1) - 1;
            System.arraycopy(storage, indexForInsert, storage, indexForInsert + 1, size - indexForInsert);
            storage[indexForInsert] = resume;
        }
    }

    @Override
    protected void fillDeletedElement(int index) {
        IntStream.range(index, size - 1).forEach(i -> storage[i] = storage[i + 1]);
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
