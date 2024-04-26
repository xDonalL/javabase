package come.urise.webapp.storage;

import come.urise.webapp.exception.NotExitStorageException;
import come.urise.webapp.exception.StorageException;
import come.urise.webapp.model.ContactType;
import come.urise.webapp.model.Resume;
import come.urise.webapp.model.Section;
import come.urise.webapp.model.SectionType;
import come.urise.webapp.sql.SqlHelper;
import come.urise.webapp.util.JsonParser;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(String url, String user, String password) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(url, user, password));
    }

    @Override
    public void clear() throws IOException {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public void save(Resume r) {
        sqlHelper.<Void>executeTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO resume VALUES (?, ?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContact(connection, r);
            insertSection(connection, r);
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.executeTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                ps.execute();
                if (ps.executeUpdate() != 1) {
                    throw new NotExitStorageException(r.getUuid());
                }
            }
            deleteContact(connection, r);
            insertContact(connection, r);
            deleteSection(connection, r);
            insertSection(connection, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) throws IOException {
        return sqlHelper.executeTransaction(connection -> {
            Resume r;
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM resume WHERE uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExitStorageException(uuid);
                }
                r = new Resume(uuid, rs.getString("full_name").trim());
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM contact WHERE resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    do {
                        addContact(rs, r);
                    } while (rs.next());
                }
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM sections WHERE resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    do {
                        addSection(rs, r);
                    } while (rs.next());
                }
            }
            return r;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.<Void>execute("DELETE FROM resume WHERE uuid = ?",
                statement -> {
                    statement.setString(1, uuid);
                    if (statement.executeUpdate() == 0) {
                        throw new NotExitStorageException(uuid);
                    }
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        return sqlHelper.execute(
                "SELECT * FROM resume " +
                        "ORDER BY full_name, uuid", ps ->  {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name").trim()));
                    }
                    List<Resume> result = new ArrayList<>();
                    for (Resume r : resumes) {
                        Resume resume =  get(r.getUuid());
                        result.add(resume);
                    }
                    return result;
                });
    }

    @Override
    public int size() {
        return sqlHelper.<Integer>execute("SELECT COUNT(*) AS size FROM resume",
                statement -> {
                    ResultSet rs = statement.executeQuery();
                    if (!rs.next()) {
                        throw new StorageException("size exception");
                    }
                    return rs.getInt("size");
                });
    }

    private void insertSection(Connection connection, Resume r) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("" +
                "INSERT INTO sections(resume_uuid, type, value) " +
                "VALUES (?, ?, ?)")) {
            Map<SectionType, Section> sections = r.getSections();
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, entry.getKey().name());
                Section section = entry.getValue();
                ps.setString(3, JsonParser.write(section, Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertContact(Connection connection, Resume r) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO contact(resume_uuid, type, value) " +
                        "VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteSection(Connection connection, Resume r) throws SQLException {
        deleteAttributes(connection, r,
                "DELETE FROM sections " +
                        "WHERE resume_uuid = ?");
    }

    private void deleteContact(Connection connection, Resume r) throws SQLException {
        deleteAttributes(connection, r,
                "DELETE FROM contact " +
                        "WHERE resume_uuid = ?");
    }

    private void deleteAttributes(Connection connection, Resume r, String sql) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                sql)) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            ContactType type = ContactType.valueOf(rs.getString("type"));
            r.addContacts(type, value);
        }
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            SectionType type = SectionType.valueOf(rs.getString("type"));
            r.addSections(type, JsonParser.read(value, Section.class));
        }
    }
}