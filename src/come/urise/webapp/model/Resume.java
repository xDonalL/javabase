package come.urise.webapp.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static java.util.UUID.randomUUID;

public class Resume implements Comparable<Resume> {

    private final String uuid;

    private final String fullName;

    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section> sections = new EnumMap <> (SectionType.class);

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, Section> getSections() {
        return sections;
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
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    public String getFullName() {
        return fullName;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }
}