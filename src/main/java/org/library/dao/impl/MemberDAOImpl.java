package org.library.dao.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.library.dao.interfaces.MemberDAO;
import org.library.entity.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl implements MemberDAO {
    private Connection connection;

    public MemberDAOImpl(Connection conn) {
        this.connection = conn;
    }

    @Override
    public Member findById(Integer memberID) {
        Member member = null;
        String sql = "SELECT * FROM Member WHERE memberID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                member = new Member();
                member.setMemberID(resultSet.getInt("memberID"));
                member.setFirstName(resultSet.getString("firstName"));
                member.setLastName(resultSet.getString("lastName"));
                member.setDateOfBirth(resultSet.getString("dateOfBirth"));
                member.setAddress(resultSet.getString("address"));
                member.setEmail(resultSet.getString("email"));
                member.setContactNumber(resultSet.getString("contactNumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return member;
    }

    @Override
    public ObservableList<Member> findAll() {
        ObservableList<Member> members = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Member";

        try (var pstmt = connection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                Member member = new Member();
                member.setMemberID(resultSet.getInt("memberID"));
                member.setFirstName(resultSet.getString("firstName"));
                member.setLastName(resultSet.getString("lastName"));
                member.setDateOfBirth(resultSet.getString("dateOfBirth"));
                member.setAddress(resultSet.getString("address"));
                member.setEmail(resultSet.getString("email"));
                member.setContactNumber(resultSet.getString("contactNumber"));

                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    @Override
    public boolean save(Member member) {
        String sql = "INSERT INTO Member (firstName, lastName, dateOfBirth, address, email, contactNumber) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, member.getFirstName());
            statement.setString(2, member.getLastName());
            statement.setString(3, member.getDateOfBirth());
            statement.setString(4, member.getAddress());
            statement.setString(5, member.getEmail());
            statement.setString(6, member.getContactNumber());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    member.setMemberID(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Member member) {
        String sql = "UPDATE Member SET firstName = ?, lastName = ?, dateOfBirth = ?, " +
                "address = ?, email = ?, contactNumber = ? WHERE memberID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getFirstName());
            statement.setString(2, member.getLastName());
            statement.setString(3, member.getDateOfBirth());
            statement.setString(4, member.getAddress());
            statement.setString(5, member.getEmail());
            statement.setString(6, member.getContactNumber());
            statement.setInt(7, member.getMemberID());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer memberID) {
        String sql = "DELETE FROM Member WHERE memberID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberID);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
