package com.example.kacper.inzynierka.user.service;

import com.example.kacper.inzynierka.user.repository.UserRepository;
import com.example.kacper.inzynierka.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceIMPL implements UserDetailsService {

    UserRepository userRepo;

    @Autowired
    public UserDetailsServiceIMPL(UserRepository userRepository){
        this.userRepo = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetailsIMPL user = UserDetailsIMPL.build(userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user with this email not found: " + email)));

        return user;
    }

}
