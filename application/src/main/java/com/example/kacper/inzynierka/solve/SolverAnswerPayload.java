package com.example.kacper.inzynierka.solve;


import com.example.kacper.inzynierka.questions.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SolverAnswerPayload {

        private String answer;
        private Question question;

        public SolverAnswerPayload(String answer, Question question) {
                this.answer = answer;
                this.question = question;
        }
}
