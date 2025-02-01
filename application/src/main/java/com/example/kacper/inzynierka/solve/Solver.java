package com.example.kacper.inzynierka.solve;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@Document(collection = "solvers")
public class Solver {

    @Id
    private String id;
    private String fullname;
    private String email;

    private LocalDateTime test_day;
    private String solved_test;

    private int cheating;

    private List<SolverAnswerPayload> answers;

    private boolean checked;

    public Solver(String fullname, String email, LocalDateTime test_day, String solved_test, int cheating, List<SolverAnswerPayload> answers, boolean checked) {
        this.fullname = fullname;
        this.email = email;
        this.solved_test = solved_test;
        this.test_day = test_day;
        this. cheating = 0;
        this.answers = answers;
        this.checked = checked;
    }
}
