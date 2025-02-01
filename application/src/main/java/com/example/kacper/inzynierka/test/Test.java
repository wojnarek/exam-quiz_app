package com.example.kacper.inzynierka.test;


import com.example.kacper.inzynierka.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@NoArgsConstructor
@Setter
@Getter
@Document(collection = "tests")
public class Test {

    @Id
    private String id;

    private String owner;

    private String name;

    private String description;

    private String category;

    private int pass_percent;

    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start_date;
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime end_date;

    private String test_code;

    private int questions_count;

    private double time_for_answer;

    private boolean hide;


    public Test(String owner, String name, String description, String category, int pass_percent, LocalDateTime start_date, LocalDateTime end_date, String test_code, int questions_count, double time_for_answer, boolean hide) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.category = category;
        this.pass_percent = pass_percent;
        this.start_date = start_date;
        this.end_date = end_date;
        this.test_code = test_code;
        this.questions_count = questions_count;
        this.time_for_answer = time_for_answer;
        this.hide = hide;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", pass_percent=" + pass_percent +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", test_code='" + test_code + '\'' +
                ", questions_count=" + questions_count +
                ", time_for_answer=" + time_for_answer +
                '}';
    }



    public static String testCodeGenerator(){
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        int random = rnd.nextInt(10-5)+5;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < random; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
