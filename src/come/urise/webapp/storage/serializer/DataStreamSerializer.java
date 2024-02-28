package come.urise.webapp.storage.serializer;

import come.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> contactEntry : contacts.entrySet()) {
                dos.writeUTF(contactEntry.getKey().name());
                dos.writeUTF(contactEntry.getValue());
            }
            Section section;
            SectionType sectionType;
            Map<SectionType, Section> sections = resume.getSections();
            for (Map.Entry<SectionType, Section> sectionEntry : sections.entrySet()) {
                sectionType = sectionEntry.getKey();
                dos.writeUTF(sectionType.name());
                section = sectionEntry.getValue();
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(sectionEntry.getValue().toString());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> items = ((ListSection) section).getItems();
                        dos.writeInt(items.size());
                        for (int i = 0; i < items.size(); i++) {
                            dos.writeUTF(items.get(i));
                        }
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizations = ((OrganizationSection) section).getOrganizations();
                        dos.writeInt(organizations.size());
                        Organization organization;
                        List<Organization.Position> positions;
                        Link link;
                        for (int i = 0; i < organizations.size(); i++) {
                            organization = (organizations.get(i));
                            positions = organization.getPositions();
                            link = organization.getHomePage();
                            dos.writeUTF(link.getName());
                            dos.writeUTF(link.getUrl());
                            dos.writeInt(positions.size());
                            for (int j = 0; j < positions.size(); j++) {
                                dos.writeUTF(positions.get(j).getStartDate().toString());
                                dos.writeUTF(positions.get(j).getEndDate().toString());
                                dos.writeUTF(positions.get(j).getTitle());
                                if (positions.get(j).getDescription() != null) {
                                    dos.writeUTF(positions.get(j).getDescription());
                                }
                            }
                        }
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int contactSize = dis.readInt();
            for (int i = 0; i < contactSize; i++) {
                resume.addContacts(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            SectionType sectionType;
            while (dis.available() > 0) {
                sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                case PERSONAL:
                case OBJECTIVE:
                    resume.addSections(sectionType, new TextSection(dis.readUTF()));
                break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                    int listSize = dis.readInt();
                    List<String> listSection = new ArrayList<>(listSize);
                    for (int i = 0; i < listSize; i++) {
                        listSection.add(dis.readUTF());
                    }
                    resume.addSections(sectionType, new ListSection(listSection));
                break;
                    case EXPERIENCE:
                    case EDUCATION:
                    int organizationsSize = dis.readInt();
                    List<Organization> organizations = new ArrayList<>(organizationsSize);
                    int positionSize;
                    String name;
                    String url;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    for (int i = 0; i < organizationsSize; i++) {
                        name = dis.readUTF();
                        url = dis.readUTF();
                        positionSize = dis.readInt();
                        Organization.Position[] positions = new Organization.Position[positionSize];
                        for (int j = 0; j < positionSize; j++) {
                            positions[j] = (new Organization.Position(
                                    LocalDate.parse(dis.readUTF(), formatter),
                                    LocalDate.parse(dis.readUTF(), formatter),
                                    dis.readUTF(),
                                    dis.readUTF()
                            ));
                        }
                        organizations.add(new Organization(name, url, positions));
                    }
                    resume.addSections(sectionType, new OrganizationSection(organizations));
                }
            }
            return resume;
        }
    }
}
