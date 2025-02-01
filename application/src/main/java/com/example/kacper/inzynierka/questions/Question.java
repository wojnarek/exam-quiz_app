package com.example.kacper.inzynierka.questions;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Document(collection = "questions")
public class Question {

    @Id
    private String id;

    private String owner;

    private List<String> tests_membership;

    private String test_type;

    private String question_content;

    private List<String> answers;

    private List<String> right_answers;

    private String image_path;

    private int points;

    public Question(String owner, List<String> tests_membership, String test_type, int points, String question_content, List<String> answers, List<String> right_answers, String image_path) {
        this.owner = owner;
        this.tests_membership = tests_membership;
        this.test_type = test_type;
        this.points = points;
        this.question_content = question_content;
        this.answers = answers;
        this.right_answers = right_answers;
        this.image_path = image_path;
    }
}
