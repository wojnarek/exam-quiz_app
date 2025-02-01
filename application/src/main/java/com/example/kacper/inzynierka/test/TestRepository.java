package com.example.kacper.inzynierka.test;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends MongoRepository<Test, String > {
    Optional<Test> findById(String id);

    @Query("{owner: ?0}")
    List<Test> findAllByOwnerId(String id);

    @Query("{test_code: ?0}")
    List<Test> findExistingCode(String code);

}
