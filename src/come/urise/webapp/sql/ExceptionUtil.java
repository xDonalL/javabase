package come.urise.webapp.sql;

import come.urise.webapp.exception.ExistStorageException;
import come.urise.webapp.exception.StorageException;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

public class ExceptionUtil {
    private ExceptionUtil() {

    }
    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException && e.getSQLState().equals("23505")) {
                throw new ExistStorageException(null);
        }
        return new StorageException(null);
    }
}
