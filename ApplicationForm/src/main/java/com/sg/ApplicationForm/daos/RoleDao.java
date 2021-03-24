/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.ApplicationForm.daos;

import com.sg.ApplicationForm.models.Role;
import java.util.List;

/**
 *
 * @author board
 */
public interface RoleDao {
    
    Role getRoleById(int id);
    Role getRoleByRole(String role);
    List<Role> getAllRoles();
    
}
