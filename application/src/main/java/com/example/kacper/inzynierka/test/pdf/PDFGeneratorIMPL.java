package com.example.kacper.inzynierka.test.pdf;


import com.example.kacper.inzynierka.questions.Question;
import com.example.kacper.inzynierka.test.Test;
import com.lowagie.text.DocumentException;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PDFGeneratorIMPL implements PDFGenerator {


    private static String pdfDirectory = "src/main/resources/static/pdfs/";


    private TemplateEngine templateEngine;

    @Autowired
    public PDFGeneratorIMPL(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;
    }

    @Override
    public String GeneratePDF(String template, List<Question> questionList, Test test_details, String file_name) {

            Context context = new Context();
            context.setVariable("questions",questionList);
            context.setVariable("testdetails",test_details);

            String htmlContent = templateEngine.process(template,context);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(pdfDirectory + file_name);
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(htmlContent);
                renderer.layout();
                renderer.createPDF(fileOutputStream, false);
                renderer.finishPDF();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
            return file_name;
    }
}
