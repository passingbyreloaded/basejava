package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import static java.util.stream.IntStream.range;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage{

    @Override
    protected void insertElement(Resume resume, int index) {
        storage[size] = resume;
    }

    @Override
    protected void fillDeletedElement(int index) {
        storage[index] = storage[size - 1];
    }

    protected int getIndex(String uuid) {
        return range(0, size)
                .filter(i -> uuid.equals(storage[i].toString()))
                .findFirst().orElse(-1);
    }

}
