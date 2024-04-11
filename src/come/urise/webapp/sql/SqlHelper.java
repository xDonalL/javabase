package come.urise.webapp.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connection) {
        connectionFactory = connection;
    }

    public void execute(String sql) {
        try (Connection con = connectionFactory.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }

    public <T> T execute(String sql, SqlExecutor<T> statement) {
        try (Connection con = connectionFactory.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return statement.executor(ps);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }
}
