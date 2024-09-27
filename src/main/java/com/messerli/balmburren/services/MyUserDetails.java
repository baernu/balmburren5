package com.messerli.balmburren.services;

import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.entities.UsersRole;
import com.messerli.balmburren.repositories.UsersRoleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class MyUserDetails implements UserDetails {

    private final User user;
    @Autowired
    private final UsersRoleRepo usersRoleRepo;

    public MyUserDetails(User user, UsersRoleRepo usersRoleRepo) {
        this.user = user;
        this.usersRoleRepo = usersRoleRepo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(usersRoleRepo == null)log.info("UserRoleRepo in MyUserDetails is null");
        List<UsersRole> roles = usersRoleRepo.findAllByUser(user);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        log.info("All UsersRoles: " + roles.get(0).toString());
        for (UsersRole usersRole : roles) {
            authorities.add(new SimpleGrantedAuthority(usersRole.getRole().getName().name()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

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
        return user.isEnabled();
    }

}
