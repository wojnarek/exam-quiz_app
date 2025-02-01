package com.example.kacper.inzynierka.results;


import com.example.kacper.inzynierka.questions.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserAnswerAndQuestions {

    private Question questions;

    private String user_answer;

    private int earn_points;

    public UserAnswerAndQuestions(Question questions, String user_answer, int earn_points) {
        this.questions = questions;
        this.user_answer = user_answer;
        this.earn_points = earn_points;
    }

    public UserAnswerAndQuestions(Question questions, String user_answer){
        this.questions = questions;
        this.user_answer = user_answer;
    }
}
