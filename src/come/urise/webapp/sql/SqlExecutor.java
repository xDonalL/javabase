package come.urise.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlExecutor<T> {
      T executor(PreparedStatement statement) throws SQLException;
}
