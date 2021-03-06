/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.ApplicationForm.daos;

import com.sg.ApplicationForm.daos.RoleDaoDB.RoleMapper;
import com.sg.ApplicationForm.models.Role;
import com.sg.ApplicationForm.models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



/**
 *
 * @author board
 */
@Repository
public class UserDaoDB implements UserDao {

    @Autowired
    JdbcTemplate template;

    @Override
    public User createUser(User toAdd) {

        KeyHolder kh = new GeneratedKeyHolder();

        int rowsAffected = template.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO `user` (username, password, enabled) VALUES (?,?,?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, toAdd.getUsername());
                    ps.setString(2, toAdd.getPassword());
                    ps.setBoolean(3, toAdd.isEnabled());

                    return ps;
                },
                kh);

        int generatedId = kh.getKey().intValue();

        toAdd.setId(generatedId);

        for (Role role : toAdd.getRoles()) {
            template.update("INSERT INTO user_role (user_id, role_id) VALUES ( ?, ? )",
                    generatedId, role.getId());
        }

        return toAdd;

    }

    @Override
    public List<User> getAllUsers() {

        List<User> allUsers = template.query("SELECT * FROM `user`", new UserMapper());
        for (User u : allUsers) {
            u.setRoles(getRolesForUser(u.getId()));
        }

        return allUsers;
    }

    private Set<Role> getRolesForUser(int userId) {

        Set<Role> toReturn = new HashSet<>(
                template.query("SELECT * FROM Role r JOIN user_role ur ON r.id = "
                        + "ur.role_id JOIN User u ON u.id = ur.user_id WHERE u.id = ?",
                        new RoleMapper(), userId)
        );

        return toReturn;
    }

    @Override
    public User getUserById(int id) {
        try {
            User retrieved = template.queryForObject(
                    "SELECT * FROM USER WHERE id = ?", new UserMapper(), id);

            retrieved.setRoles(getRolesForUser(id));

            return retrieved;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {

        try {
            User retrieved = template.queryForObject(
                    "SELECT * FROM USER WHERE username = ?", new UserMapper(), username);

            retrieved.setRoles(getRolesForUser(retrieved.getId()));

            return retrieved;

        } catch (DataAccessException ex) {
            return null;
        }

    }

    @Override
    @Transactional
    public void updateUser(User newData) {

        template.update("UPDATE user SET username = ?, password = ?, enabled = ? WHERE id = ?",
                newData.getUsername(), newData.getPassword(), newData.isEnabled(), newData.getId());

        template.update("DELETE FROM user_role WHERE user_id = ?", newData.getId());

        for (Role role : newData.getRoles()) {
            template.update("INSERT INTO user_role(user_id, role_id) VALUES ( ?, ? )",
                    newData.getId(), role.getId());
        }
    }

    @Override
    public void deleteUser(int id) {
        template.update("DELETE FROM user_role WHERE user_id = ?", id);

        template.update("DELETE FROM user WHERE id = ?", id);
    }

    public static class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet row, int i) throws SQLException {

            User toReturn = new User();

            toReturn.setId(row.getInt("id"));
            toReturn.setPassword(row.getString("password"));
            toReturn.setUsername(row.getString("username"));
            toReturn.setEnabled(row.getBoolean("enabled"));

            return toReturn;
        }
    }



}
