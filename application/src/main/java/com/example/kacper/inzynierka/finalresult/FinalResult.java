package com.example.kacper.inzynierka.finalresult;


import com.example.kacper.inzynierka.results.UserAnswerAndQuestions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "finalresults")
public class FinalResult {

    @Id
    private String id;
    private String fullname;
    private String email;
    private int earned_points;

    private int negative_points;
    private int max_points;
    private String test_name;
    private LocalDateTime test_day;
    private String test_id;
    private String owner;
    private String message;
    private double pass_percent;
    private List<UserAnswerAndQuestions> uaaq;

    public FinalResult(String fullname, String email, int earned_points, int negative_points, int max_points, double pass_percent, String test_name, LocalDateTime test_day, String test_id, String owner, String message, List<UserAnswerAndQuestions> uaaq) {
        this.fullname = fullname;
        this.email = email;
        this.earned_points = earned_points;
        this.negative_points = negative_points;
        this.max_points = max_points;
        this.pass_percent = pass_percent;
        this.test_name = test_name;
        this.test_day = test_day;
        this.test_id = test_id;
        this.owner = owner;
        this.message = message;
        this.uaaq = uaaq;
    }

    @Override
    public String toString() {
        return "FinalResult{" +
                "fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", earned_points=" + earned_points +
                ", max_points=" + max_points +
                ", test_name='" + test_name + '\'' +
                ", test_day='" + test_day + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
