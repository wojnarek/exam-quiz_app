package com.example.kacper.inzynierka.solve;


import com.example.kacper.inzynierka.questions.Question;
import com.example.kacper.inzynierka.questions.QuestionService;
import com.example.kacper.inzynierka.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
public class SolverController {

    private SolverRepository solverRepository;
    private QuestionService questionService;
    private TestService testService;
    private SolverService solverService;


    @Autowired
    public SolverController(SolverRepository solverRepository, QuestionService questionService, TestService testService, SolverService solverService) {
        this.solverRepository = solverRepository;
        this.questionService = questionService;
        this.testService = testService;
        this.solverService = solverService;
    }

    @RequestMapping(value = "/solve", method = RequestMethod.POST)
    public String startTest(SolverPayload solverPayload, Model model) {
        if (solverPayload.getData() != null && solverPayload.getEmail() != null && solverPayload.getId() != null) {
            LocalDateTime test_day = testService.getTestStartDate(solverPayload.getId()).getStart_date();
            Solver s = new Solver(
                    solverPayload.getData(),
                    solverPayload.getEmail(),
                    test_day,
                    solverPayload.getId(),
                    0,
                    new ArrayList<SolverAnswerPayload>() {
                    },
                    false);
            solverRepository.save(s);
            model.addAttribute("test_id", solverPayload.getId());
            model.addAttribute("solver_id", s.getId());
            model.addAttribute("time", testService.getTimeToAnswer(solverPayload.getId()));
            model.addAttribute("test_name",testService.getTestDetails(solverPayload.getId()).getName());
            model.addAttribute("time_to_answer",testService.getTestDetails(solverPayload.getId()).getTime_for_answer());
            model.addAttribute("pass_percent",testService.getTestDetails(solverPayload.getId()).getPass_percent());
            model.addAttribute("test_time",ChronoUnit.MINUTES.between(testService.getTestDetails(solverPayload.getId()).getStart_date(), testService.getTestDetails(solverPayload.getId()).getEnd_date()));
            return "solveTest";
        }
        return "testNotActive";
    }


    @RequestMapping(value = "/solve/start", method = RequestMethod.POST)
    public ResponseEntity<List<Question>> test(StartTestPayload startTestPayload) {
        List<Question> q = questionService.getForTestQuestions(startTestPayload.getTest_id());
        List<Question> questionsToSave = questionService.getToSaveForSolverQuestions(startTestPayload.getTest_id());
        List<SolverAnswerPayload> solverAnswerPayloadList = new ArrayList<>();
        questionsToSave.forEach(e -> {
            solverAnswerPayloadList.add(new SolverAnswerPayload("",e));
        });
        questionService.setQuestionsToSolver(startTestPayload.getSolver_id(),solverAnswerPayloadList);
        return new ResponseEntity<>(q, HttpStatus.OK);
    }


    @RequestMapping(value = "/solve/getnextquestion", method = RequestMethod.POST)
    @ResponseBody
    public Question getNextQuestion(NextQuestionPayload nextQuestionPayload){
       Question one_question = new Question();
        if(testService.isTestActive(nextQuestionPayload.getTest_id())) {
            one_question = solverService.getNextQuestion(nextQuestionPayload.getQuestion_id(), nextQuestionPayload.getTest_id());}
        return one_question;
    }


    @RequestMapping(value = "/solve/saveanswer", method = RequestMethod.POST)
    public ResponseEntity<Object> saveAnswer(SolverSaveAnswerPayload answerPayload, String question_id) {
        SolverAnswerPayload answer = new SolverAnswerPayload(answerPayload.getAnswer(), questionService.getSingleQuestionToSaveInAnswer(question_id));
        solverService.saveAnswer(answer, answerPayload.getTest_id(), answerPayload.getSolver_id());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/solve/endaftertime", method = RequestMethod.POST)
    public ResponseEntity<Object> endTestAfterTime(String q){

        System.out.println(q);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/solve/end", method = RequestMethod.POST)
    public ResponseEntity<Object> endTest(SolverSaveAnswerPayload answerPayload, @RequestParam("question_id") String question_id, @RequestParam("cheat_count") int cheat) {
        solverService.updateCheatCount(answerPayload.getTest_id(), answerPayload.getSolver_id(), cheat);
        SolverAnswerPayload answer = new SolverAnswerPayload(answerPayload.getAnswer(), questionService.getSingleQuestionToSaveInAnswer(question_id));
        solverService.saveAnswer(answer, answerPayload.getTest_id(), answerPayload.getSolver_id());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/solve/finish", method = RequestMethod.GET)
    public String finish() {return "finishTest";}

}
