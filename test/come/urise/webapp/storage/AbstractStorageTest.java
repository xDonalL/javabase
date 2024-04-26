package come.urise.webapp.storage;

import come.urise.webapp.exception.ExistStorageException;
import come.urise.webapp.exception.NotExitStorageException;
import come.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static come.urise.webapp.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class AbstractStorageTest {
    protected static final String STORAGE_DIR = "ResumeStorage";
    protected Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() throws Exception {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @Test
    public void save() throws Exception{
        storage.save(resume4);
        assertEquals(4, storage.size());
    }

    @Test
    public void saveExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(resume2));
    }

    @Test
    public void update() throws Exception {
        Resume resume = new Resume(UUID_1, "Name");
        storage.update(resume);
        assertTrue(resume.equals(storage.get(UUID_1)));
    }

    @Test
    public void delete() {
        storage.delete(UUID_1);
        assertEquals(2, storage.size());
    }

    @Test
    public void deleteNotExit() {
        assertThrows(NotExitStorageException.class, () -> storage.delete("uuid19"));
    }

    @Test
    public void get() throws Exception {
        assertEquals(resume1, storage.get(resume1.getUuid()));
        assertEquals(resume2, storage.get(resume2.getUuid()));
        assertEquals(resume3, storage.get(resume3.getUuid()));
    }

    @Test
    public void getNotExit() {
        assertThrows(NotExitStorageException.class, () -> storage.get("uuid19"));
    }

    @Test
    public void getAllSorted() throws Exception {
        storage.save(resume4);
        List<Resume> list = storage.getAllSorted();
        List<Resume> sortedList = Arrays.asList(resume1, resume2, resume3, resume4);
        Collections.sort(sortedList);
        assertEquals(4, list.size());
        assertEquals(sortedList, list);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }
}