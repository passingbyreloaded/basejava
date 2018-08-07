package ru.javawebinar.basejava.storage;


public class ObjectStreamStorageFileTest extends AbstractStorageTest {

    public ObjectStreamStorageFileTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamStorage()));
    }

}
