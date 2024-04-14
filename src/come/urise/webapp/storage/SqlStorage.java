package come.urise.webapp.storage;

import come.urise.webapp.exception.NotExitStorageException;
import come.urise.webapp.exception.StorageException;
import come.urise.webapp.model.ContactType;
import come.urise.webapp.model.Resume;
import come.urise.webapp.sql.SqlHelper;

import java.io.IOException;
import java.sql.*;
import java.util.*;

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
        sqlHelper.executeTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO resume VALUES (?, ?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContact(connection, r);
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.executeTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                ps.execute();
                if (ps.executeUpdate() != 1) {
                    throw new NotExitStorageException(r.getUuid());
                }
            }
            deleteContact(connection, r);
            insertContact(connection, r);
        });
    }

    @Override
    public Resume get(String uuid) throws IOException {
        return sqlHelper.execute("" +
                        "SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON (r.uuid = c.resume_uuid) " +
                        "WHERE r.uuid = ?",
                statement -> {
                    statement.setString(1, uuid);
                    ResultSet rs = statement.executeQuery();
                    if (!rs.next()) {
                        throw new NotExitStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name").trim());
                    do {
                        addContact(rs, r);
                    } while (rs.next());
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
        return sqlHelper.execute("" +
                "   SELECT * FROM resume r " +
                "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                "ORDER BY full_name, uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            Map<String, Resume> map = new LinkedHashMap<>();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                Resume r = map.get(uuid);
                if (r == null) {
                    r = new Resume(uuid, rs.getString("full_name").trim());
                    map.put(uuid, r);
                }
                addContact(rs, r);
            }
            return new ArrayList<>(map.values());
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

    private void insertContact(Connection connection, Resume r) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("" +
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

    private void deleteContact(Connection connection, Resume r) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("" +
                "DELETE FROM contact " +
                "WHERE resume_uuid = ?")) {
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
}