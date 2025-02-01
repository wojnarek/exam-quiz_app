package com.example.kacper.inzynierka.results;


import com.example.kacper.inzynierka.solve.Solver;
import com.example.kacper.inzynierka.solve.SolverService;
import nonapi.io.github.classgraph.fileslice.ArraySlice;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TestResultService {

    private MongoOperations mongoOperations;
    private TestResultRepository testResultRepository;


    public TestResultService(MongoOperations mongoOperations, TestResultRepository testResultRepository) {
        this.mongoOperations = mongoOperations;
        this.testResultRepository = testResultRepository;
    }

    public List<TestResult> getAllNotFinishedResults(String owner) {
        Query query = new Query(new Criteria("owner").is(owner).and("checked").is(false)).with(Sort.by(Sort.Direction.DESC, "test_day"));
        return mongoOperations.find(query, TestResult.class);
    }

    public List<TestResult> getAllFinishedResults(String owner) {
        Query query = new Query(new Criteria("owner").is(owner).and("checked").is(true)).with(Sort.by(Sort.Direction.DESC, "test_day"));
        return mongoOperations.find(query, TestResult.class);
    }

    public void updateCheckStatus(String test_id, LocalDateTime test_day, String owner) {
        Update update = new Update();
        update.set("checked", true);
        Query query = new Query(new Criteria("owner").is(owner).and("test_id").is(test_id).and("test_day").is(test_day));
        mongoOperations.findAndModify(query, update, TestResult.class);
    }


    public List<UserAnswerAndQuestions> checkAnswers(@NotNull Solver solver) {
        List<UserAnswerAndQuestions> uaaq = new ArrayList<>();
       
        solver.getAnswers().forEach(answers -> {
            switch (answers.getQuestion().getTest_type()) {
                case "JW":
                    UserAnswerAndQuestions userAnswerAndQuestions = new UserAnswerAndQuestions();
                    if(answers.getAnswer() == null){
                        userAnswerAndQuestions.setEarn_points(0);
                    }else {
                        if (!answers.getAnswer().equals("")) {
                            if (Arrays.asList(answers.getAnswer().split(",")).equals(answers.getQuestion().getRight_answers())) {
                                userAnswerAndQuestions.setEarn_points(answers.getQuestion().getPoints());
                            } else {
                                userAnswerAndQuestions.setEarn_points(0);
                            }
                        } else {
                            userAnswerAndQuestions.setEarn_points(0);
                        }
                    }
                    userAnswerAndQuestions.setUser_answer(answers.getAnswer());
                    userAnswerAndQuestions.setQuestions(answers.getQuestion());
                    uaaq.add(userAnswerAndQuestions);
                    break;

                case "WW":
                    UserAnswerAndQuestions userAnswerAndQuestionsWW = new UserAnswerAndQuestions();
                    if(answers.getAnswer() == null){
                        userAnswerAndQuestionsWW.setEarn_points(0);
                    }else {
                        int temp_points = 0;
                        List<String> user_answers = new ArrayList<>(Arrays.asList(answers.getAnswer().split((","))));
                        List<String> right_answers = answers.getQuestion().getRight_answers();
                        Collections.sort(user_answers);
                        Collections.sort(right_answers);
                        for (int i = 0; i < user_answers.size(); i++) {
                            for (int j = 0; j < right_answers.size(); j++) {
                                if (user_answers.get(i).equals(right_answers.get(j))) {
                                    temp_points++;
                                }
                            }
                        }
                        if (user_answers.size() == answers.getQuestion().getAnswers().size()) {
                            temp_points = 0;
                        }
                        if (user_answers.size() > answers.getQuestion().getRight_answers().size()) {
                            int points_to_sub = user_answers.size() - answers.getQuestion().getRight_answers().size();
                            if (temp_points > points_to_sub) {
                                temp_points = temp_points - points_to_sub;
                            } else {
                                temp_points = 0;
                            }
                        }
                        userAnswerAndQuestionsWW.setEarn_points(temp_points);
                    }
                    userAnswerAndQuestionsWW.setQuestions(answers.getQuestion());
                    userAnswerAndQuestionsWW.setUser_answer(answers.getAnswer());
                    uaaq.add(userAnswerAndQuestionsWW);
                    break;

                case "PF":
                    UserAnswerAndQuestions userAnswerAndQuestionsPF = new UserAnswerAndQuestions();
                    if(answers.getAnswer() == null){
                        userAnswerAndQuestionsPF.setEarn_points(0);
                    }
                    else {
                        if (Arrays.asList(answers.getAnswer().split(",")).equals(answers.getQuestion().getRight_answers())) {
                            userAnswerAndQuestionsPF.setEarn_points(answers.getQuestion().getPoints());
                        } else {
                            userAnswerAndQuestionsPF.setEarn_points(0);
                        }
                    }
                    userAnswerAndQuestionsPF.setUser_answer(answers.getAnswer());
                    userAnswerAndQuestionsPF.setQuestions(answers.getQuestion());
                    uaaq.add(userAnswerAndQuestionsPF);
                    break;

                default:
                    uaaq.add(new UserAnswerAndQuestions(answers.getQuestion(), answers.getAnswer(), -1));
            }
        });
        uaaq.sort(Comparator.comparing(UserAnswerAndQuestions::getEarn_points));
        return uaaq;
    }

    public List<TestResult> getLatestResults(String owner){
        Query query = new Query(new Criteria("owner").is(owner)).with(Sort.by(Sort.Direction.DESC,"id")).limit(5);
        return mongoOperations.find(query, TestResult.class);
    }

}
