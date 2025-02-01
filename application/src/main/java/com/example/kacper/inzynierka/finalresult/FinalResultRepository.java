package com.example.kacper.inzynierka.finalresult;

import com.example.kacper.inzynierka.results.UserAnswerAndQuestions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalResultRepository extends MongoRepository<FinalResult, String> {
}
