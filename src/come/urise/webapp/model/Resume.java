package come.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static java.util.UUID.randomUUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final Resume EMPTY = new Resume();

    static {
        EMPTY.addSections(SectionType.OBJECTIVE, TextSection.EMPTY);
        EMPTY.addSections(SectionType.PERSONAL, TextSection.EMPTY);
        EMPTY.addSections(SectionType.ACHIEVEMENT, ListSection.EMPTY);
        EMPTY.addSections(SectionType.QUALIFICATIONS, ListSection.EMPTY);
        EMPTY.addSections(SectionType.EXPERIENCE, new OrganizationSection(List.of(Organization.EMPTY)));
        EMPTY.addSections(SectionType.EDUCATION, new OrganizationSection(List.of(Organization.EMPTY)));
    }

    private String uuid;

    private String fullName;

    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, Section> getSections() {
        return sections;
    }

    public void addContacts(ContactType contactType, String title) {
        contacts.put(contactType, title);
    }

    public void addSections(SectionType sectionType, Section section) {
        sections.put(sectionType, section);
    }

    public Resume() {
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(fullName);
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public Resume(String fullName) {
        Objects.requireNonNull(fullName);
        this.uuid = randomUUID().toString();
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return uuid + " : " + fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) && Objects.equals(fullName, resume.fullName) && Objects.equals(contacts, resume.contacts) && Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (contacts != null ? contacts.hashCode() : 0);
        result = 31 * result + (sections != null ? sections.hashCode() : 0);
        return result;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int compareTo(Resume o) {
        int result = fullName.compareTo(o.fullName);
        return result != 0 ? result : uuid.compareTo(o.uuid);
    }
}