package com.example.kacper.inzynierka.user.repository;


import com.example.kacper.inzynierka.user.ERole;
import com.example.kacper.inzynierka.user.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);

}
