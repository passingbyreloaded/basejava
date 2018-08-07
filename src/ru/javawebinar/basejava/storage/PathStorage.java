package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PathStorage extends AbstractPathStorage {

    protected PathStorage(String dir, SerializationStrategy strategy) {
        super(dir);
        this.strategy = strategy;
    }

    @Override
    protected void doWrite(Resume r, OutputStream os) throws IOException {
        strategy.doWrite(r, os);
    }

    @Override
    protected Resume doRead(InputStream is) throws IOException {
        return strategy.doRead(is);
    }
}
