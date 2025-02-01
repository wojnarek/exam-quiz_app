package com.example.kacper.inzynierka.test;


import com.example.kacper.inzynierka.results.TestResult;
import com.example.kacper.inzynierka.results.TestResultRepository;
import com.example.kacper.inzynierka.questions.QuestionService;
import com.example.kacper.inzynierka.solve.SolverRepository;
import com.example.kacper.inzynierka.test.pdf.FileNameResponse;
import com.example.kacper.inzynierka.test.pdf.PDFGeneratorIMPL;
import com.example.kacper.inzynierka.user.service.UserDetailsIMPL;
import com.example.kacper.inzynierka.user.service.UserDetailsServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/tests")
public class TestController {

    private TestRepository testRepository;
    private UserDetailsServiceIMPL userDetailsServiceIMPL;
    private TestService testService;
    private QuestionService questionService;

    private SolverRepository solverRepository;

    private TestResultRepository testResultRepository;

    private PDFGeneratorIMPL pdfGeneratorIMPL;


    @Autowired
    public TestController(TestRepository testRepository, UserDetailsServiceIMPL userDetailsServiceIMPL, TestService testService, QuestionService questionService, SolverRepository solverRepository, TestResultRepository testResultRepository, PDFGeneratorIMPL pdfGeneratorIMPL) {
        this.testRepository = testRepository;
        this.userDetailsServiceIMPL = userDetailsServiceIMPL;
        this.testService = testService;
        this.questionService = questionService;
        this.solverRepository = solverRepository;
        this.testResultRepository = testResultRepository;
        this.pdfGeneratorIMPL = pdfGeneratorIMPL;
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/new-test", method = RequestMethod.GET)
    public String getNewTestForm(Model model) {
        model.addAttribute("test", new Test());
        return "newTestForm";
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addNewTest(Test test, @AuthenticationPrincipal UserDetailsIMPL user) {

        test = new Test(user.getId(),
                test.getName(),
                test.getDescription(),
                test.getCategory(),
                test.getPass_percent(),
                test.getStart_date(),
                test.getEnd_date(),
                testService.generateTestCode(),
                test.getQuestions_count(),
                test.getTime_for_answer(),
                false
        );
        testRepository.save(test);

        TestResult testResult = new TestResult(test.getStart_date(),
                test.getName(),
                test.getId(),
                user.getId(),
                false);
        testResultRepository.save(testResult);

        return "redirect:/tests/manage/" + test.getId();
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/manage/{id}", method = RequestMethod.GET)
    public String manageTest(Model model, @PathVariable String id, @AuthenticationPrincipal UserDetailsIMPL user) {
        model.addAttribute("test", testService.getToEditTest(id, user.getId()));
        model.addAttribute("questions", questionService.getAllTestQuestions(user.getId(), id));
        model.addAttribute("notassignedquestions",questionService.getNotIncludeInTestQuestions(id,user.getId()));
        return "manageTest";
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String allTests(Model model, @AuthenticationPrincipal UserDetailsIMPL user) {
        model.addAttribute("tests", testService.getAllNotHideTests(user.getId()));
        model.addAttribute("hideTests", testService.getAllNotHideTests(user.getId()));
        model.addAttribute(new Test());
        return "allTests";
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Test update(Test test, @RequestParam("form_type") String form_type, @AuthenticationPrincipal UserDetailsIMPL user) {

        System.out.println(test.getId());
        System.out.println("nazwa test"+test.getName());


        switch (form_type) {
            case "1":
                testService.updateName(test.getId(), test.getName());
                break;
            case "2":
                testService.updateDescription(test.getId(), test.getDescription());
                break;
            case "3":
                testService.updateCategory(test.getId(), test.getCategory());
                break;
            case "4":
                testService.updateDate(test.getId(), test.getStart_date(), test.getEnd_date());
                TestResult testResult = new TestResult(test.getStart_date(),
                        test.getName(),
                        test.getId(),
                        user.getId(),
                        false);
                testResultRepository.save(testResult);
                break;
            case "5":
                testService.updatePassPercent(test.getId(), test.getPass_percent());
                break;
            case "6":
                testService.updateTimeForAnswer(test.getId(), test.getTime_for_answer());
                break;
        }
        return test;
    }


    @RequestMapping(value = "/{test_code}", method = RequestMethod.GET)
    public String checkTestStatus(@PathVariable String test_code, Model model) {
        if(testService.getTestDate(test_code).getStart_date() != null) {
            LocalDateTime start_date = testService.getTestDate(test_code).getStart_date();
            LocalDateTime end_date = testService.getTestDate(test_code).getEnd_date();
            if (LocalDateTime.now().isAfter(start_date) && LocalDateTime.now().isBefore(end_date)) {
                model.addAttribute("testcode", testService.getTestId(test_code).getId());
                return "testStatusSolverDetail";
            }
        }
        return "testNotActive";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/hide", method = RequestMethod.POST)
    public String hideTest(String test_id, @AuthenticationPrincipal UserDetailsIMPL user){
            testService.hideTest(test_id,user.getId());
        return "redirect:/tests";
    }


    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    @RequestMapping(value = "/generatepdf", method = RequestMethod.POST)
    public FileNameResponse generatePdf(String test_id, String test_name, @AuthenticationPrincipal UserDetailsIMPL user){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        Test t = testService.getTestDetails(test_id);
        String file_name =  pdfGeneratorIMPL.GeneratePDF("pdfTemplate.html",questionService.getAllTestQuestions(user.getId(),test_id),t,timestamp+"_"+test_name+".pdf");
        return new FileNameResponse(file_name);
    }


    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public List<Test>findTest(SearchPayload serachPayload){
        List<Test> tests = testService.findTest(serachPayload.getTo_search());
        return tests;
    }

}
