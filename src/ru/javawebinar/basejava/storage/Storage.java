package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public interface Storage {

    int size();

    void clear();

    Resume get(String uuid);

    void delete(String uuid);

    void save(Resume resume);

    void update(Resume resume);

    Resume[] getAll();
}
