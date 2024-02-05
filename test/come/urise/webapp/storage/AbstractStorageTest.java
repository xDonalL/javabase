package come.urise.webapp.storage;

import come.urise.webapp.exception.ExistStorageException;
import come.urise.webapp.exception.NotExitStorageException;
import come.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractStorageTest {
    protected Storage storage;
    protected final String UUID_1 = "uuid1";
    protected final Resume Resume1 = new Resume(UUID_1);
    protected final String UUID_2 = "uuid2";
    protected final Resume Resume2 = new Resume(UUID_2);
    protected final String UUID_3 = "uuid3";
    protected final Resume Resume3 = new Resume(UUID_3);
    protected final String UUID_4 = "uuid4";
    protected final Resume Resume4 = new Resume(UUID_4);

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
        Resume resume = new Resume(UUID_1);
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
    public void getAll() throws Exception {
        Resume[] resumes = storage.getAll();
        assertEquals(3, resumes.length);
        assertEquals(Resume1, resumes[0]);
        assertEquals(Resume2, resumes[1]);
        assertEquals(Resume3, resumes[2]);
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