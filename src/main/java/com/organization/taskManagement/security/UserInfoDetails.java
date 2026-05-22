package com.organization.taskManagement.security;

import com.organization.taskManagement.Model.EmployeeRegisterModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * STEP-BY-STEP INTERNAL WORKINGS:
 * 
 * 1. This class implements UserDetails, which is a core interface in Spring Security.
 * 2. It acts as a wrapper for our EmployeeRegisterModel entity to make it compatible with Spring Security.
 * 3. Spring Security uses this object to store user information like username, password, and authorities (roles).
 */
public class UserInfoDetails implements UserDetails {

    // Internal reference to our Employee entity
    private EmployeeRegisterModel employee;

    public UserInfoDetails(EmployeeRegisterModel employee) {
        this.employee = employee;
    }

    /**
     * INTERNAL FLOW:
     * When Spring Security needs to check permissions, it calls this method.
     * We convert our Employee's role/ID into a SimpleGrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(employee != null && employee.getRole() != null) {
            // We use the employee ID as the authority for identification
            return Collections.singleton(new SimpleGrantedAuthority(employee.getEmployeeId()));
        }
        else return Collections.emptyList();
    }

    /**
     * INTERNAL FLOW:
     * Returns the hashed password from the database for Spring Security to compare with the user's input.
     */
    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    /**
     * INTERNAL FLOW:
     * Returns the unique identifier for the user (in our case, the employee ID).
     */
    @Override
    public String getUsername() {
        return employee.getEmployeeId();
    }

    // Standard UserDetails flags - here we default to 'true' (active)
    
    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
