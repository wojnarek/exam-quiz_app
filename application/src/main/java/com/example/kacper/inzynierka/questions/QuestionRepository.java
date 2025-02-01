package com.example.kacper.inzynierka.questions;

import com.example.kacper.inzynierka.test.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {

    @Query("{owner: ?0}")
    List<Question> findAllByOwnerId(String id);



}
