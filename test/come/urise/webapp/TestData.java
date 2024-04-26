package come.urise.webapp;

import come.urise.webapp.model.*;

import java.time.Month;
import java.util.List;
import java.util.UUID;

public class TestData {

    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String UUID_4 = UUID.randomUUID().toString();

    public static final Resume resume1 = new Resume(UUID_1, "Name1");
    public static final Resume resume2 = new Resume(UUID_2, "Name2");
    public static final Resume resume3 = new Resume(UUID_3, "Name3");
    public static final Resume resume4 = new Resume(UUID_4, "Name1");

    static {
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
    }
}
