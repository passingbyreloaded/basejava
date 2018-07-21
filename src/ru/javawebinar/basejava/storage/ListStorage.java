package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ListStorage extends AbstractStorage {

    private final List<Resume> storage = new ArrayList();

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected int getIndex(String uuid) {
        return IntStream.range(0,storage.size())
                .filter(i -> storage.get(i).getUuid().equals(uuid))
                .findFirst().orElse(-1);
    }

    @Override
    protected Resume getElement(int index) {
        return storage.get(index);
    }

    @Override
    protected void deleteElement(int index) {
        storage.remove(index);
    }

    @Override
    protected void addElement(Resume resume, int index) {
        storage.add(resume);
    }

    @Override
    protected void updateElement(Resume resume, int index) {
        storage.set(index, resume);
    }

    @Override
    public Resume[] getAll() {
        Resume[] array = new Resume[storage.size()];
        return storage.toArray(array);
    }


}
