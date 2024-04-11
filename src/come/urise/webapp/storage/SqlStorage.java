package come.urise.webapp.storage;

import come.urise.webapp.exception.NotExitStorageException;
import come.urise.webapp.exception.StorageException;
import come.urise.webapp.model.Resume;
import come.urise.webapp.sql.SqlHelper;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        sqlHelper.<Void>execute("INSERT INTO resume VALUES (?, ?)",
                statement -> {
                    statement.setString(1, r.getUuid());
                    statement.setString(2, r.getFullName());
                     statement.execute();
                     return null;
        });

    }


    @Override
    public void update(Resume r) {
        sqlHelper.<Void>execute("UPDATE resume SET full_name = ? WHERE uuid = ?",
                statement -> {
                    statement.setString(1, r.getFullName());
                    statement.setString(2, r.getUuid());
                     statement.execute();
                     return null;
                });
    }

    @Override
    public Resume get(String uuid) throws IOException {
        return sqlHelper.execute("SELECT * FROM resume WHERE uuid = ?",
                statement -> {
                    statement.setString(1, uuid);
                    ResultSet rs = statement.executeQuery();
                    if (!rs.next()) {
                        throw new NotExitStorageException("Invalid " + uuid);
                    }
                    return new Resume(uuid, rs.getString("full_name").trim());
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
        return sqlHelper.<List<Resume>>execute("SELECT * FROM resume ORDER BY full_name, uuid",
                statement -> {
                    ArrayList<Resume> list = new ArrayList<>();
                    ResultSet rs = statement.executeQuery();
                    while (rs.next()) {
                        list.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name").trim()));
                    }
                    return list;
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
}

