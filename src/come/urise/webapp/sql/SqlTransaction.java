package come.urise.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlTransaction<T> {
    void executeTransaction(Connection connection) throws SQLException;
}
