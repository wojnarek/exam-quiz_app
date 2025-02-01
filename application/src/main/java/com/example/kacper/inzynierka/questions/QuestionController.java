package com.example.kacper.inzynierka.questions;


import com.example.kacper.inzynierka.test.SearchPayload;
import com.example.kacper.inzynierka.user.service.UserDetailsIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class QuestionController {

    private QuestionRepository questionRepository;
    private QuestionService questionService;
    private FileService fileService;

    @Autowired
    public QuestionController(QuestionRepository questionRepository, QuestionService questionService, FileService fileService) {
        this.questionRepository = questionRepository;
        this.questionService = questionService;
        this.fileService = fileService;
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/q/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Question> addNewQuestionFromTestManage(MultipartFile file, Question question, @AuthenticationPrincipal UserDetailsIMPL user) throws IOException {
        question = new Question(
                user.getId(),
                question.getTests_membership(),
                question.getTest_type(),
                question.getPoints(),
                question.getQuestion_content(),
                question.getAnswers(),
                question.getRight_answers(),
                fileService.getImagePath(file)
        );
        questionRepository.save(question);
        return new ResponseEntity<>(question, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/q/assignToTest", method = RequestMethod.POST)
    public ResponseEntity<Question> assignToTest(@AuthenticationPrincipal UserDetailsIMPL user, String test_id, String question_id) {
        questionService.assignToTest(test_id, question_id, user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/q/dismissFromTest", method = RequestMethod.POST)
    public ResponseEntity<Question> dismissFromTest(@AuthenticationPrincipal UserDetailsIMPL user, String test_id, String question_id) {
        questionService.dismissFromTest(test_id, question_id, user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public String allQuestions(Model model, @AuthenticationPrincipal UserDetailsIMPL user) {
        model.addAttribute("questions", questionService.getAllUserQuestion(user.getId()));
        return "allQuestions";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/questions/manage/{id}", method = RequestMethod.GET)
    public String manageQuestion(@PathVariable String id, @AuthenticationPrincipal UserDetailsIMPL user, Model model) {
        if(id.isEmpty() || id.isBlank()){
            return "error";
        }
        model.addAttribute("question", questionService.getSingleQuestion(id, user.getId()));
        return "manageQuestion";
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    @RequestMapping(value = "/questions/edit", method = RequestMethod.POST)
    public Question edit(Question question, @RequestParam("form_type") String form_type, @AuthenticationPrincipal UserDetailsIMPL user, MultipartFile file) throws IOException {

        System.out.println(form_type);

        switch (form_type) {
            case "form_1":
                questionService.editQuestionContent(user.getId(),question.getId(),question.getQuestion_content());
                break;
            case "form_2":
                questionService.editQuestionAnswers(user.getId(),question.getId(), question.getAnswers());
                break;
            case "form_3":
                questionService.editQuestionRightAnswers(user.getId(),question.getId(),question.getRight_answers());
                break;
            case "form_4":
                questionService.editQuestionImage(user.getId(),question.getId(),fileService.getImagePath(file));
                break;
            case "form_5":
                questionService.editQuestionPoints(user.getId(),question.getId(),question.getPoints());
                break;
        }


        return question;
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/questions/add-new", method = RequestMethod.GET)
    public String addNewQuestion(){
        return "newQuestionForm";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/questions/find", method = RequestMethod.POST)
    @ResponseBody
    public List<Question> find(SearchPayload searchPayload){
        return questionService.findQuestions(searchPayload.getTo_search());
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/questions/delete", method = RequestMethod.POST)
    public ResponseEntity<Question> delete(String question_id, @AuthenticationPrincipal UserDetailsIMPL user){
        System.out.println(question_id);
        if(questionService.isQuestionFreeFromTests(question_id,user.getId())){
            questionService.deleteQuestion(question_id, user.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
