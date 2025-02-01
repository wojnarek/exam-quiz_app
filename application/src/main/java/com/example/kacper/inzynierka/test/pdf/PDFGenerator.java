package com.example.kacper.inzynierka.test.pdf;

import com.example.kacper.inzynierka.questions.Question;
import com.example.kacper.inzynierka.test.Test;

import java.util.List;

public interface PDFGenerator {
    String GeneratePDF(String template, List<Question> questionList, Test testdetails, String file_name);
}
