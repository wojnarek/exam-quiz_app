package com.example.kacper.inzynierka.finalresult;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FinalResultPayload {
    private List<String> question_id;
    private List<Integer> earned_points;
    private int max_points;
    private String email;
    private String fullname;
    private String message;
    private String solver_id;

    private int negative_points;


    public FinalResultPayload(String fullname, String email, int max_points, List<Integer> earned_points , List<String> question_id, String message, String solver_id, int negative_points) {
        this.fullname = fullname;
        this.email = email;
        this.max_points = max_points;
        this.earned_points = earned_points;
        this.question_id = question_id;
        this.message = message;
        this.solver_id = solver_id;
        this.negative_points = negative_points;
    }
}


