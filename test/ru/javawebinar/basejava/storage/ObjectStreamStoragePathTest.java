package ru.javawebinar.basejava.storage;

public class ObjectStreamStoragePathTest extends AbstractStorageTest {

    public ObjectStreamStoragePathTest() {
        super(new PathStorage(STORAGE_DIR.toString(), new ObjectStreamStorage()));
    }
}
