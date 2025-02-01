package com.example.kacper.inzynierka.questions;


import com.example.kacper.inzynierka.solve.Solver;
import com.example.kacper.inzynierka.solve.SolverAnswerPayload;
import com.example.kacper.inzynierka.test.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class QuestionService {

    private MongoOperations mongoOperations;

    @Autowired
    public QuestionService(MongoOperations mongoOperations){
        this.mongoOperations = mongoOperations;
    }

    public List<Question> getAllTestQuestions(String owner, String test){
        Query query = new Query(new Criteria("owner").is(owner).and("tests_membership").is(test));
        List<Question> q = mongoOperations.find(query,Question.class);
            return q;
    }

    public List<Question> getForTestQuestions(String id){
        Query query = new Query(new Criteria("tests_membership").is(id));
        query.fields().include("id");
        List<Question> q = mongoOperations.find(query,Question.class);
        return q;
    }

    public Question getOneQuestion(String id){
        Query query = new Query(new Criteria("id").is(id));
        query.fields().include("question_content","answers","right_answers","image_path","points");
        return mongoOperations.findOne(query,Question.class);
    }

    public List<Question> getNotIncludeInTestQuestions(String test_id, String owner_id){
        Query query = new Query(new Criteria("tests_membership").nin(test_id).and("owner").is(owner_id));
        return mongoOperations.find(query,Question.class);
    }

    public void assignToTest(String test_id, String question_id, String owner_id){
        Update update = new Update();
        update.push("tests_membership",test_id);
        Query query = new Query(new Criteria("id").is(question_id).and("owner").is(owner_id));
        mongoOperations.findAndModify(query,update,Question.class);

    }

    public void dismissFromTest(String test_id, String question_id, String owner_id){
        Update update = new Update();
        update.pull("tests_membership",test_id);
        Query query = new Query(new Criteria("id").is(question_id).and("owner").is(owner_id));
        mongoOperations.findAndModify(query,update,Question.class);
    }

    public List<Question> getAllUserQuestion(String owner){
        Query query = new Query(new Criteria("owner").is(owner));
        return mongoOperations.find(query, Question.class);
    }

    public Question getSingleQuestion(String id, String owner) {
        Query query = new Query(new Criteria("id").is(id).and("owner").is(owner));
        return mongoOperations.findOne(query,Question.class);
    }

    public Question getSingleQuestionToSaveInAnswer(String id) {
        Query query = new Query(new Criteria("id").is(id));
        return mongoOperations.findOne(query,Question.class);
    }
    public void editQuestionContent(String owner, String id, String new_content){
        Update update = new Update();
        update.set("question_content",new_content);
        Query query = new Query(new Criteria("id").is(id).and("owner").is(owner));
        mongoOperations.findAndModify(query,update,Question.class);
    }

    public void editQuestionPoints(String owner, String id, int new_points){
        Update update = new Update();
        update.set("points",new_points);
        Query query = new Query(new Criteria("id").is(id).and("owner").is(owner));
        mongoOperations.findAndModify(query,update,Question.class);
    }

    public void editQuestionAnswers(String owner, String id, List<String> new_answers){
        Update update = new Update();
        update.set("answers",new_answers);
        Query query = new Query(new Criteria("id").is(id).and("owner").is(owner));
        mongoOperations.findAndModify(query,update,Question.class);
    }

    public void editQuestionRightAnswers(String owner, String id, List<String> new_right_answers){
        Update update = new Update();
        update.set("right_answers",new_right_answers);
        Query query = new Query(new Criteria("owner").is(owner).and("id").is(id));
        mongoOperations.findAndModify(query,update,Question.class);
    }

    public void editQuestionImage(String owner, String id, String new_image_path){
        Update update = new Update();
        update.set("image_path",new_image_path);
        Query query = new Query(new Criteria("owner").is(owner).and("id").is(id));
        mongoOperations.findAndModify(query,update,Question.class);
    }

    public void setQuestionsToSolver(String solver_id, List<SolverAnswerPayload> solverAnswerPayloads){
        Update update = new Update();
        update.set("answers",solverAnswerPayloads);
        Query query = new Query(new Criteria("id").is(solver_id));
        mongoOperations.findAndModify(query,update, Solver.class);
    }

    public List<Question> getToSaveForSolverQuestions(String test_id){
        Query query = new Query(new Criteria("tests_membership").is(test_id));
       return mongoOperations.find(query, Question.class);
    }

    public List<Question> findQuestions(String looking_for){
        Query query = new Query(new Criteria("question_content").regex(looking_for));
        Query query1 = new Query(new Criteria("answers").regex(looking_for));


        List<Question> l1 = mongoOperations.find(query,Question.class);
        List<Question> l2 = mongoOperations.find(query1,Question.class);

        List<Question> finallist = Stream.concat(l1.stream(),l2.stream()).toList();

        return finallist;
    }

    public boolean isQuestionFreeFromTests(String question_id, String owner){
        Query query = new Query(new Criteria("id").is(question_id).and("owner").is(owner));
        query.fields().include("tests_membership");
        Question q = mongoOperations.findOne(query, Question.class);
        if(q.getTests_membership() == null || q.getTests_membership().isEmpty()){
            return true;
        }
        return false;
    }

    public void deleteQuestion(String question_id, String owner){
        Query query = new Query(new Criteria("id").is(question_id).and("owner").is(owner));
        mongoOperations.findAndRemove(query, Question.class);
    }

    public List<Question> getLatest(String owner){
        Query query = new Query(new Criteria("owner").is(owner)).with(Sort.by(Sort.Direction.DESC, "id")).limit(5);
        return mongoOperations.find(query, Question.class);
    }

}
