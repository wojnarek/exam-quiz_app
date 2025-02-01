package com.example.kacper.inzynierka.results;


import com.example.kacper.inzynierka.email.EmailDetails;
import com.example.kacper.inzynierka.email.EmailService;
import com.example.kacper.inzynierka.finalresult.FinalResult;
import com.example.kacper.inzynierka.finalresult.FinalResultPayload;
import com.example.kacper.inzynierka.finalresult.FinalResultRepository;
import com.example.kacper.inzynierka.finalresult.FinalResultService;
import com.example.kacper.inzynierka.questions.Question;
import com.example.kacper.inzynierka.questions.QuestionService;
import com.example.kacper.inzynierka.solve.Solver;
import com.example.kacper.inzynierka.solve.SolverService;
import com.example.kacper.inzynierka.test.Test;
import com.example.kacper.inzynierka.test.TestService;
import com.example.kacper.inzynierka.user.service.UserDetailsIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TestResultController {

    private SolverService solverService;
    private TestResultService testResultService;
    private QuestionService questionService;
    private FinalResultRepository finalResultRepository;
    private TestService testService;

    private FinalResultService finalResultService;

    private EmailService emailService;

    @Autowired
    public TestResultController(SolverService solverService, TestResultService testResultService, QuestionService questionService, FinalResultRepository finalResultRepository, TestService testService, FinalResultService finalResultService, EmailService emailService) {
        this.solverService = solverService;
        this.testResultService = testResultService;
        this.questionService = questionService;
        this.finalResultRepository = finalResultRepository;
        this.testService = testService;
        this.finalResultService = finalResultService;
        this.emailService = emailService;
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/results", method = RequestMethod.GET)
    public String getAllResults(@AuthenticationPrincipal UserDetailsIMPL user, Model model) {
        model.addAttribute("results", testResultService.getAllNotFinishedResults(user.getId()));
        model.addAttribute("finishedResults", testResultService.getAllFinishedResults(user.getId()));
        return "allResults";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/results/solvers", method = RequestMethod.GET)
    public String checkResults(ResultPayload resultPayload, Model model) {
        model.addAttribute("solvers", solverService.getAllSolverToCheck(resultPayload.getTest_id(), resultPayload.getTest_day()));
        return "resultsCheck";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/results/solvers/{id}", method = RequestMethod.GET)
    public String checkSingleResult(@PathVariable String id, Model model) throws IOException {
        Solver solver = solverService.getSingleResult(id);
        testResultService.checkAnswers(solver);
       model.addAttribute("resultlist", testResultService.checkAnswers(solver));
       model.addAttribute("s", solver);
        return "checkingSolver";
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/results/savefinalresults", method = RequestMethod.POST)
    public ResponseEntity<Object> saveResults(FinalResultPayload finalResultPayload, @AuthenticationPrincipal UserDetailsIMPL user) {
        int earned_points = 0;

        for (int i = 0; i < finalResultPayload.getEarned_points().size(); i++) {
            earned_points = earned_points + finalResultPayload.getEarned_points().get(i);
        }

        earned_points = earned_points - finalResultPayload.getNegative_points();

        Solver solver = solverService.getNameAndDate(finalResultPayload.getSolver_id());
        Test test = testService.getTestNameAndIdAndPass(solver.getSolved_test(), user.getId());

        List<UserAnswerAndQuestions> uaaq = new ArrayList<>();

        Solver solver1 = solverService.getSingleResult(finalResultPayload.getSolver_id());

//        solver1.getAnswers().forEach(solverAnswerPayload -> {
//            Question question = questionService.getOneQuestion(solverAnswerPayload.getQuestion_id());
//            uaaq.add(new UserAnswerAndQuestions(question, solverAnswerPayload.getAnswer(), 0));
//        });

        for(int i = 0; i < solver1.getAnswers().size(); i++){
                Question question = questionService.getOneQuestion(solver1.getAnswers().get(i).getQuestion().getId());
                uaaq.add(new UserAnswerAndQuestions(question, solver1.getAnswers().get(i).getAnswer(),finalResultPayload.getEarned_points().get(i)));
        }

        FinalResult finalResult = new FinalResult(finalResultPayload.getFullname(), finalResultPayload.getEmail(), earned_points, finalResultPayload.getNegative_points(), finalResultPayload.getMax_points(),test.getPass_percent(), test.getName(), solver.getTest_day(), test.getId(), user.getId(), finalResultPayload.getMessage(), uaaq);
        finalResultRepository.save(finalResult);
        solverService.updateCheckStatus(finalResultPayload.getSolver_id());

//        if (solverService.isEverySolverChecked(test.getId(), solver.getTest_day())) {
//            testResultService.updateCheckStatus(test.getId(), solver.getTest_day(), user.getId());
//        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/results/checkstatus", method = RequestMethod.POST)
    public ResponseEntity<Object> checkResultsStatus(String test_id, String test_day, @AuthenticationPrincipal UserDetailsIMPL user){
        if (solverService.isEverySolverChecked(test_id, LocalDateTime.parse(test_day)) && solverService.isMoreThan0Solvers(test_id, LocalDateTime.parse(test_day))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/results/sendemails", method = RequestMethod.POST)
    public ResponseEntity<Object> sendEmailsWithResults(String test_id, String test_day, @AuthenticationPrincipal UserDetailsIMPL user){
        List<FinalResult> finalResults = finalResultService.getAllResultsToSendEmail(test_id, LocalDateTime.parse(test_day), user.getId());

        finalResults.forEach(finalResult -> {
            EmailDetails emailDetails = new EmailDetails();

            emailDetails.setMsgBody(String.valueOf(finalResult.getMax_points()),
                    String.valueOf(finalResult.getEarned_points()),
                    finalResult.getNegative_points(),
                    (double)finalResult.getEarned_points() / (double)finalResult.getMax_points() * 100, finalResult.getTest_name(),
                    finalResult.getTest_day().toString(),
                    finalResult.getPass_percent(),
                    finalResult.getFullname(),
                    finalResult.getMessage());

            emailDetails.setSubject(finalResult.getTest_name(),
                    finalResult.getTest_day().toString(),
                    finalResult.getFullname());

            emailDetails.setRecipient(finalResult.getEmail());

            emailService.sendSimpleMail(emailDetails);
        });

        testResultService.updateCheckStatus(test_id, LocalDateTime.parse(test_day),user.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
