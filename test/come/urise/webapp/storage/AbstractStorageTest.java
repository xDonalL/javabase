package come.urise.webapp.storage;

import come.urise.webapp.exception.ExistStorageException;
import come.urise.webapp.exception.NotExitStorageException;
import come.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractStorageTest {
    protected Storage storage;
    protected final String UUID_1 = "uuid1";
    protected final Resume Resume1 = new Resume(UUID_1, "Name3");
    protected final String UUID_2 = "uuid2";
    protected final Resume Resume2 = new Resume(UUID_2, "Name1");
    protected final String UUID_3 = "uuid3";
    protected final Resume Resume3 = new Resume(UUID_3, "Name2");
    protected final String UUID_4 = "uuid4";
    protected final Resume Resume4 = new Resume(UUID_4, "Name1");

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() throws Exception {
        storage.clear();
        storage.save(Resume1);
        storage.save(Resume2);
        storage.save(Resume3);
    }

    @Test
    public void save() throws Exception {
        storage.save(Resume4);
        assertEquals(4, storage.size());
    }

    @Test
    public void saveExist() throws Exception {
        assertThrows(ExistStorageException.class, () -> storage.save(Resume1));
    }

    @Test
    public void update() throws Exception {
        Resume resume = new Resume(UUID_1, "Name");
        storage.update(resume);
        assertTrue(resume == storage.get(UUID_1));
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_1);
        assertEquals(2, storage.size());
    }

    @Test
    public void deleteNotExit() throws Exception {
        assertThrows(NotExitStorageException.class, () -> storage.delete("uuid19"));
    }

    @Test
    public void get() throws Exception {
        storage.get("uuid2");
        assertEquals(UUID_2, "uuid2");
    }

    @Test
    public void getNotExit() throws Exception {
        assertThrows(NotExitStorageException.class, () -> storage.get("uuid19"));
    }

    @Test
    public void getAllSorted() throws Exception {
        storage.save(Resume4);
       List<Resume> resumes = storage.getAllSorted();
        assertEquals(Resume2, resumes.get(0)); // uuid2 Name1
        assertEquals(Resume4, resumes.get(1)); // uuid4 Name1
        assertEquals(Resume3, resumes.get(2)); // uuid3 Name2
        assertEquals(Resume1, resumes.get(3)); // uuid1 Name3
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void size() throws Exception {
        assertEquals(3, storage.size());
    }
}