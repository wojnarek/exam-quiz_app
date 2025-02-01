package com.example.kacper.inzynierka.solve;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SolverSaveAnswerPayload {
    private String answer;
    private String solver_id;
    private String test_id;

}
