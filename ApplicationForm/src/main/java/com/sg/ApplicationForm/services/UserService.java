/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.ApplicationForm.services;

import com.sg.ApplicationForm.daos.DaoException;
import com.sg.ApplicationForm.daos.RoleDao;
import com.sg.ApplicationForm.daos.UserDao;
import com.sg.ApplicationForm.models.Role;
import com.sg.ApplicationForm.models.User;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author board
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    RoleDao role;

    @Autowired
    UserDao user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loaded = user.getUserByUsername(username);

        if (loaded == null) {
            throw new UsernameNotFoundException("could not find username: " + username);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Role role : loaded.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        org.springframework.security.core.userdetails.User toReturn
                = new org.springframework.security.core.userdetails.User(
                        loaded.getUsername(),
                         loaded.getPassword(),
                         grantedAuthorities);

        return toReturn;
    }

    public User createUser(User toAdd) throws DaoException {
        User foundUser = user.getUserByUsername(toAdd.getUsername());
        if (foundUser == null) {
            return user.createUser(toAdd);
        } else {
            throw new DaoException("Username already exists!");
        }
    }

    public Role getRoleByRole(String r) {
        return role.getRoleByRole(r);
    }

}
