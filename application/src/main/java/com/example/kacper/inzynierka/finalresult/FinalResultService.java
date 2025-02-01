package com.example.kacper.inzynierka.finalresult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinalResultService {

    private MongoOperations mongoOperations;

    @Autowired
    public FinalResultService(MongoOperations operations){
        this.mongoOperations = operations;
    }


    public List<FinalResult> getAllResultsToSendEmail(String test_id, LocalDateTime test_day, String owner){
        Query query = new Query(new Criteria("test_id").is(test_id).and("test_day").is(test_day).and("owner").is(owner));
        return mongoOperations.find(query, FinalResult.class);
    }

    public List<FinalResult> getTestFinalResults(LocalDateTime test_day, String test_id, String owner){
        Query query = new Query(new Criteria("test_id").is(test_id).and("test_day").is(test_day).and("owner").is(owner));
        return mongoOperations.find(query, FinalResult.class);
    }

    public FinalResult getSingleResult(String id, String owner){
        Query query = new Query(new Criteria("id").is(id).and("owner").is(owner));
        return mongoOperations.findOne(query, FinalResult.class);
    }


}
