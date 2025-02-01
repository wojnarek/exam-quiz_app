package com.example.kacper.inzynierka.solve;

import com.example.kacper.inzynierka.test.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolverRepository extends MongoRepository<Solver, String> {

    @Query("{test_day:  ?0, solved_test:  ?1}")
    List<Solver> findAllSolversToCheck(String test_day, String test_id);
}
