package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.List;

public interface Storage {

    int size();

    void clear();

    Resume get(String uuid);

    void delete(String uuid);

    void save(Resume resume);

    void update(Resume resume);

    List<Resume> getAllSorted();
}
