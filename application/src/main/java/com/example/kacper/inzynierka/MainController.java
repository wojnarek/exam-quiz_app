package com.example.kacper.inzynierka;

import com.example.kacper.inzynierka.questions.QuestionService;
import com.example.kacper.inzynierka.results.TestResult;
import com.example.kacper.inzynierka.results.TestResultService;
import com.example.kacper.inzynierka.test.TestService;
import com.example.kacper.inzynierka.user.User;
import com.example.kacper.inzynierka.user.service.UserDetailsIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    private QuestionService questionService;
    private TestService testService;
    private TestResultService testResultService;

    @Autowired
    public MainController(QuestionService questionService, TestService testService, TestResultService testResultService) {
        this.testResultService = testResultService;
        this.questionService = questionService;
        this.testService = testService;
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(Model model) {
        model.addAttribute(new User());
        return "login";
    }

    @RequestMapping(value = "/register")
    public String getRegisterPage(Model model) {
        model.addAttribute(new User());
        return "register";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndexPage(Model model, @AuthenticationPrincipal UserDetailsIMPL user) {
        model.addAttribute(new User());
        if (user != null) {
                model.addAttribute("testresults",testResultService.getLatestResults(user.getId()));
                model.addAttribute("questions",questionService.getLatest(user.getId()));
                model.addAttribute("tests",testService.getLatest(user.getId()));
            return "index";
        }
        return "welcomePage";
    }


//
//    @PreAuthorize("hasRole('USER')")
//    @RequestMapping(value = "/test", method = RequestMethod.GET)
//    public String getTest(){
//        return "test";
//    }


}
