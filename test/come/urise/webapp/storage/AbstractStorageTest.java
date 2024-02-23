package come.urise.webapp.storage;

import come.urise.webapp.exception.ExistStorageException;
import come.urise.webapp.exception.NotExitStorageException;
import come.urise.webapp.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractStorageTest {
    protected static final String STORAGE_DIR = "D:\\project\\storage";
    protected Storage storage;
    protected final String UUID_1 = "uuid1";
    protected final Resume Resume1 = new Resume(UUID_1, "Name1");
    protected final String UUID_2 = "uuid2";
    protected final Resume Resume2 = new Resume(UUID_2, "Name2");
    protected final String UUID_3 = "uuid3";
    protected final Resume Resume3 = new Resume(UUID_3, "Name3");
    protected final String UUID_4 = "uuid4";
    protected final Resume Resume4 = new Resume(UUID_4, "Name1");

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() throws Exception {
        storage.clear();
        Resume1.getContacts().put(ContactType.PHONE, "+79622229933");
        Resume1.getContacts().put(ContactType.DISCORD, "@donal5962");
        Resume1.getContacts().put(ContactType.EMAIL, "yuriy@gmail.com");
        Resume1.getSections().put(SectionType.OBJECTIVE, new TextSection("Java Developer"));
        Resume1.getSections().put(SectionType.PERSONAL, new TextSection("Личные качества . . ."));
        Resume1.getSections().put(SectionType.ACHIEVEMENT, new ListSection(List.of("Достежение 1", "Достижение 2", "Достижение 3")));
        Resume1.getSections().put(SectionType.QUALIFICATIONS, new ListSection(List.of("Version control: . . .", "JEE AS: . . .",
                "DB: . . .", " Languages: . . .", "Java Frameworks: . . .")));
        Resume1.getSections().put(SectionType.EXPERIENCE, new OrganizationSection(List.of(new Organization("Name1", "https://Name2",
                new Organization.Position(2020, Month.OCTOBER, 2022, Month.APRIL, "Title", "Description"
                )), new Organization("Name2", "https://Name2", new Organization.Position(
                2022, Month.MAY, "Title", "Description")))));
        Resume1.getSections().put(SectionType.EDUCATION, new OrganizationSection(List.of(new Organization("Name3", "https://Name3",
                new Organization.Position(2013, Month.SEPTEMBER, 2018, Month.JULY, "Title", null),
                new Organization.Position(2018, Month.SEPTEMBER, 2020, Month.AUGUST, "Title", null)))));
        storage.save(Resume1);
        storage.save(Resume2);
        storage.save(Resume3);
    }

    @Test
    public void save() throws Exception{
        storage.save(Resume4);
        assertEquals(4, storage.size());
    }

    @Test
    public void saveExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(Resume2));
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
        storage.get("uuid2");
        assertEquals("uuid2", UUID_2);
    }

    @Test
    public void getNotExit() {
        assertThrows(NotExitStorageException.class, () -> storage.get("uuid19"));
    }

    @Test
    public void getAllSorted() throws Exception {
        storage.save(Resume4);
        List<Resume> list = storage.getAllSorted();
        assertEquals(Resume1, list.get(0)); // uuid1 Name1
        assertEquals(Resume4, list.get(1)); // uuid4 Name1
        assertEquals(Resume2, list.get(2)); // uuid2 Name2
        assertEquals(Resume3, list.get(3)); // uuid3 Name3
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