package come.urise.webapp.sql;

import come.urise.webapp.exception.StorageException;

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

    public void executeTransaction(SqlTransaction statement) {
        try (Connection con = connectionFactory.getConnect()) {
            try {
                con.setAutoCommit(false);
                statement.executeTransaction(con);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw ExceptionUtil.convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
