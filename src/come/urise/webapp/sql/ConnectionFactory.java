package come.urise.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {
    Connection getConnect() throws SQLException;
}
