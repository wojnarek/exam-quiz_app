package com.example.kacper.inzynierka.results;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TestResultRepository extends MongoRepository<TestResult, String > {

@Query("{'owner':  ?0}")
    List<TestResult> findAllOrderByDate(String owner, Sort sort);

}
