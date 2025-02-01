package com.example.kacper.inzynierka.security;

import com.example.kacper.inzynierka.user.User;
import com.example.kacper.inzynierka.user.service.UserDetailsIMPL;
import com.example.kacper.inzynierka.user.service.UserDetailsServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserDetailsServiceIMPL customUserDetailsService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UserDetailsIMPL user = (UserDetailsIMPL) customUserDetailsService.loadUserByUsername(authentication.getName());


        if (user == null) {
            return null;
        }
        if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            return null;
        }

//        Set<String> roles = user.getAuthorities().stream()
//                   .map(item -> item.getAuthority())
//                   .collect(Collectors.toList());


        List<GrantedAuthority> roles = (List<GrantedAuthority>) user.getAuthorities();

        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), roles);
    }

}
