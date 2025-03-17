package org.example.exmdirect_new.service;


import org.example.exmdirect_new.entity.Admin;
import org.example.exmdirect_new.repository.AdminRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminService extends AbstractUserService<Admin> implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        super(adminRepository);
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                admin.getLogin(),
                admin.getPassword(),
                Collections.singleton(getAuthority(admin))
        );
    }

    private GrantedAuthority getAuthority(Admin admin) {
        return new SimpleGrantedAuthority("ROLE_ADMIN");
    }
}
