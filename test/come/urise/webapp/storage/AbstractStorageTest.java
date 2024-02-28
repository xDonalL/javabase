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
    protected final Resume resume1 = new Resume(UUID_1, "Name1");
    protected final String UUID_2 = "uuid2";
    protected final Resume resume2 = new Resume(UUID_2, "Name2");
    protected final String UUID_3 = "uuid3";
    protected final Resume resume3 = new Resume(UUID_3, "Name3");
    protected final String UUID_4 = "uuid4";
    protected final Resume resume4 = new Resume(UUID_4, "Name1");

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() throws Exception {
        storage.clear();
        resume1.getContacts().put(ContactType.PHONE, "+79622229933");
        resume1.getContacts().put(ContactType.DISCORD, "@donal5962");
        resume1.getContacts().put(ContactType.EMAIL, "yuriy@gmail.com");
        resume1.getSections().put(SectionType.OBJECTIVE, new TextSection("Java Developer"));
        resume1.getSections().put(SectionType.PERSONAL, new TextSection("Личные качества . . ."));
        resume1.getSections().put(SectionType.ACHIEVEMENT, new ListSection(List.of("Достижение 1", "Достижение 2", "Достижение 3")));
        resume1.getSections().put(SectionType.QUALIFICATIONS, new ListSection(List.of("Version control: . . .", "JEE AS: . . .",
                "DB: . . .", " Languages: . . .", "Java Frameworks: . . .")));
        resume1.getSections().put(SectionType.EXPERIENCE, new OrganizationSection(List.of(new Organization("Name1", "https://Name2",
                new Organization.Position(2020, Month.OCTOBER, 2022, Month.APRIL, "Title", "Description"
                )), new Organization("Name2", "https://Name2", new Organization.Position(
                2022, Month.MAY, "Title", "Description")))));
        resume1.getSections().put(SectionType.EDUCATION, new OrganizationSection(List.of(new Organization("Name3", "https://Name3",
                new Organization.Position(2013, Month.SEPTEMBER, 2018, Month.JULY, "Title", ""),
                new Organization.Position(2018, Month.SEPTEMBER, 2020, Month.AUGUST, "Title", "null")))));
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
        assertEquals(resume1, list.get(0)); // uuid1 Name1
        assertEquals(resume4, list.get(1)); // uuid4 Name1
        assertEquals(resume2, list.get(2)); // uuid2 Name2
        assertEquals(resume3, list.get(3)); // uuid3 Name3
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