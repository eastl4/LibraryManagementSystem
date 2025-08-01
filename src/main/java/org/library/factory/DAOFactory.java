package org.library.factory;

import org.library.dao.impl.IssuedBookDAOImpl;
import org.library.dao.impl.BookDAOImpl;
import org.library.dao.impl.MemberDAOImpl;
import org.library.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class DAOFactory {

    private static Connection conn = DatabaseConnection.getInstance().getConnection();

    public static void commitTransaction() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.commit();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void rollbackTransaction() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static BookDAOImpl getBookDAO() {
        return new BookDAOImpl(conn);
    }
    public static IssuedBookDAOImpl getIssuedBookDAO() {
        return new IssuedBookDAOImpl(conn);
    }
    public static MemberDAOImpl getMemberDAO() {
        return new MemberDAOImpl(conn);
    }
}

