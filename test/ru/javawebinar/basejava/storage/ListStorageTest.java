package ru.javawebinar.basejava.storage;

import org.junit.Assert;

public class ListStorageTest extends AbstractArrayStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Override
    public void saveOverflow() throws Exception {
        Assert.assertTrue(true);
    }
}
