package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {

    private final Map<String, Resume> storage = new HashMap();

    @Override
    protected int getIndex(String uuid) {
        return 0;
    }

    @Override
    protected Resume getElement(int index) {
        return null;
    }

    @Override
    protected void deleteElement(int index) {

    }

    @Override
    protected void addElement(Resume resume, int index) {

    }

    @Override
    protected void updateElement(Resume resume, int index) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public Resume[] getAll() {
        return new Resume[0];
    }
}
