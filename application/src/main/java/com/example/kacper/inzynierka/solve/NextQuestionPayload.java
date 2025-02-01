package com.example.kacper.inzynierka.solve;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NextQuestionPayload {
    private String test_id;
    private String question_id;
}
