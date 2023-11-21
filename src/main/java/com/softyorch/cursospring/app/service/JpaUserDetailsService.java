package com.softyorch.cursospring.app.service;

import com.softyorch.cursospring.app.models.dao.IUserDao;
import com.softyorch.cursospring.app.models.entity.Role;
import com.softyorch.cursospring.app.models.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserDao userDao;

    private final Logger log = LoggerFactory.getLogger(JpaUserDetailsService.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Buscar al usuario
        SysUser sysUser = userDao.findByUsername(username);

        if (sysUser == null) {
            log.error("Login Error: No existe el usuario '".concat(username).concat("'"));
            throw new UsernameNotFoundException("Username ".concat(username).concat(" no existe en el sistema!"));
        }

        // Listar los roles del usuario
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role: sysUser.getAuthorities()) {
            log.info("ROLE: ".concat(role.getAuthority()));
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        if (authorities.isEmpty()) {
            log.error("Login Error: El usuario '".concat(username).concat("'").concat(" no tiene ROLE asignados!"));
            throw new UsernameNotFoundException("Username ".concat(username).concat(" no tiene ROLE asignados!"));
        }

        return new User(
                sysUser.getUsername(),
                sysUser.getPassword(),
                sysUser.getEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}
