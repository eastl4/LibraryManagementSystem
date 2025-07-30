package org.library.dao.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.library.dao.interfaces.BookDAO;
import org.library.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAOImpl implements BookDAO {
    private Connection conn;

    public BookDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Book findById(String isbn) {

        String sql = "SELECT * FROM Book WHERE isbn = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Book(rs.getString("title"),rs.getString("author"),
                        rs.getString("isbn"), rs.getInt("quantity"),
                        rs.getInt("available"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public ObservableList<Book> findAll() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Book";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(rs.getString("title"),
                        rs.getString("author"), rs.getString("isbn"),
                        rs.getInt("quantity"), rs.getInt("available")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return books;
    }

    @Override
    public boolean save(Book entity) {
        String sql = "INSERT INTO Book (title, author, isbn, quantity, available) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getTitle());
            pstmt.setString(2, entity.getAuthor());
            pstmt.setString(3, entity.getISBN());
            pstmt.setInt(4, entity.getQuantity());
            pstmt.setInt(5, entity.getAvailable());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Book entity) {
        String sql = "UPDATE Book SET title=?, author=?, quantity=?, available=? WHERE isbn=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getTitle());
            pstmt.setString(2, entity.getAuthor());
            pstmt.setInt(3, entity.getQuantity());
            pstmt.setInt(4, entity.getAvailable());
            pstmt.setString(5, entity.getISBN());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

    }

    @Override
    public boolean delete(String isbn) {
        String sql = "DELETE FROM Book WHERE isbn=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
