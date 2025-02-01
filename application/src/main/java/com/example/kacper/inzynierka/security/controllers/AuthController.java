package com.example.kacper.inzynierka.security.controllers;

import com.example.kacper.inzynierka.security.CustomAuthenticationManager;
import com.example.kacper.inzynierka.security.jwt.JwtUtils;
import com.example.kacper.inzynierka.user.ERole;
import com.example.kacper.inzynierka.user.Role;
import com.example.kacper.inzynierka.user.User;
import com.example.kacper.inzynierka.user.repository.RoleRepository;
import com.example.kacper.inzynierka.user.repository.UserRepository;
import com.example.kacper.inzynierka.user.service.UserDetailsIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller()
public class AuthController {
    //    @Autowired
//    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private CustomAuthenticationManager authenticationManager;

    private static String password_pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    @RequestMapping(value = "/signin", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public String authenticateUser(User user, Model model) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()));
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {                          //obiekt autentykacji rozny od nulla, jezeli authentication nie jest instacja anonymu to git
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
            jwtUtils.generateJwtToken(authentication);

            return "redirect:/";
        }
        model.addAttribute("errormsg", "Niepoprawny login lub hasło!");
        return "login";
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String registerUser(User user, Model model) {

        if(!user.getPassword().matches(password_pattern)){
            model.addAttribute("erromsg","Twoje hasło jest za łatwe!");
            return "register";
        }
        if (user.getEmail().isBlank() || user.getEmail().isEmpty() || user.getUsername().isEmpty() || user.getUsername().isBlank() || user.getPassword().isBlank() || user.getPassword().isEmpty()) {
            model.addAttribute("errormsg", "Wartości nie mogą pozostac puste!");
            return "register";
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("errormsg", "Ten email jest już w użyciu!");
            return "register";
        }

        User newUser = new User(user.getEmail(),
                encoder.encode(user.getPassword()),
                user.getUsername()
        );

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        newUser.setRoles(roles);
        userRepository.save(newUser);

        model.addAttribute("successmsg", "Pozytywnie zarejestrowano! Mozesz sie zalogować!");

        return "login";
    }
}
