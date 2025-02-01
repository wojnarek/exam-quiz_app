package com.example.kacper.inzynierka.finalresult;


import com.example.kacper.inzynierka.user.service.UserDetailsIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;

@Controller
public class FinalResultController {

    private FinalResultService finalResultService;

    @Autowired
    public FinalResultController(FinalResultService finalResultService) {
        this.finalResultService = finalResultService;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/finalresults/testfinalresults", method = RequestMethod.POST)
    public String getTestFinalResults(String test_id, String test_day, @AuthenticationPrincipal UserDetailsIMPL user, Model model){
        model.addAttribute("finalResults",finalResultService.getTestFinalResults(LocalDateTime.parse(test_day),test_id, user.getId()));
        return "testFinalResults";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/finalresults/solverfinalresult/{id}", method = RequestMethod.GET)
    public String getSolverFinalResults(@PathVariable String id, @AuthenticationPrincipal UserDetailsIMPL user, Model model){

        FinalResult finalResult = finalResultService.getSingleResult(id, user.getId());

        model.addAttribute("frSolverDetails",finalResult);
        model.addAttribute("frSolverQuestions",finalResult.getUaaq());




        return "solverFinalResult";
    }


}
/*
* TODO
*  sprawdzac czy rozwiazywany test dalej miesci sie w ramach czasowych
*   Widok dla finalresult - punkty, pytania, odpowiedzi, poprawne odpowiedz, wynik etc
*   Cofnij dla zatwiedzonego pytania, poprawic zatwierdzenie pojedynczego rezultatu
*
* */