package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;
import java.util.Arrays;
import static java.util.stream.IntStream.range;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage{

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume resume) {
        if (getIndex(resume.getUuid()) >= 0) {
            System.out.println("The resume is already in the storage");
            return;
        }
        if (size == storage.length) {
            System.out.println("Storage overflow");
            return;
        }
        storage[size] = resume;
        size++;
    }

    public void update(Resume resume) {
        int index;
        if ((index = getIndex(resume.getUuid())) >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("No such resume in the storage");
        }
    }

    public void delete(String uuid) {
        int index;
        if ((index = getIndex(uuid)) >= 0) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("No such resume in the storage");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected int getIndex(String uuid) {
        return range(0, size)
                .filter(i -> uuid.equals(storage[i].toString()))
                .findFirst().orElse(-1);
    }
}
