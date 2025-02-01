package com.example.kacper.inzynierka.solve;


import com.example.kacper.inzynierka.questions.Question;
import com.example.kacper.inzynierka.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolverService {
    private MongoOperations mongoOperations;
    private TestService testService;
    private SolverRepository solverRepository;

    @Autowired
    public SolverService(MongoOperations mongoOperations, TestService testService, SolverRepository solverRepository){
        this.testService = testService;
        this.mongoOperations = mongoOperations;
        this.solverRepository = solverRepository;
    }

    public Question getNextQuestion(String id, String test_id){
        Query query = new Query(new Criteria("id").is(id).and("tests_membership").is(test_id));
        query.fields().include("test_type","question_content","answers","image_path","points");
        return mongoOperations.findOne(query,Question.class);
    }

    public List<Solver> getAllSolverToCheck(String test_id, String test_day){
        LocalDateTime date = LocalDateTime.parse(test_day);
        Query query = new Query(new Criteria("solved_test").is(test_id).and("test_day").is(date));
        return mongoOperations.find(query,Solver.class);
    }

    public void saveAnswer(SolverAnswerPayload answer, String test_id, String solver_id){
        Update update = new Update();
        update.set("answers.$.answer",answer.getAnswer());
        Query query = new Query(new Criteria("id").is(solver_id).and("solved_test").is(test_id).and("answers").elemMatch(Criteria.where("question.id").is(answer.getQuestion().getId())));
        mongoOperations.findAndModify(query, update, Solver.class);
    }

    public void updateCheatCount(String test_id, String solver_id, int cheatcount){
        Update update = new Update();
        update.set("cheating",cheatcount);
        Query query = new Query(new Criteria("id").is(solver_id).and("solved_test").is(test_id));
        mongoOperations.findAndModify(query,update,Solver.class);
    }

    public Solver getSingleResult(String solver_id){
        Query query = new Query(new Criteria("id").is(solver_id));
        query.fields().include("answers","email","fullname","cheating");
        Solver solver = mongoOperations.findOne(query, Solver.class);

        return solver;
    }

    public Solver getNameAndDate(String solver_id){
        Query query = new Query(new Criteria("id").is(solver_id));
        query.fields().include("test_day","solved_test");
            return mongoOperations.findOne(query,Solver.class);
    }

    public void updateCheckStatus(String solver_id){
        Update update = new Update();
        update.set("checked",true);
        Query query = new Query(new Criteria("id").is(solver_id));
        mongoOperations.updateFirst(query,update,Solver.class);
    }

    public boolean isEverySolverChecked(String test_id, LocalDateTime test_day){
        Query query = new Query(new Criteria("solved_test").is(test_id).and("test_day").is(test_day).and("checked").is(false));
        List<Solver> solvers = mongoOperations.find(query, Solver.class);

        if (solvers.size() > 0){
            return false;
        }else {
            return true;
        }
    }

    public boolean isMoreThan0Solvers(String test_id, LocalDateTime test_day){
        Query query = new Query(new Criteria("solved_test").is(test_id).and("test_day").is(test_day).and("checked").is(false));
        List<Solver> solvers = mongoOperations.find(query, Solver.class);
        if (solvers.size() > 1){
            return false;
        }else {
            return true;
        }
    }



}
