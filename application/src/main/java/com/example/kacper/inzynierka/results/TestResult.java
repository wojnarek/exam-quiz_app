package com.example.kacper.inzynierka.results;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@Document(collection = "results")
public class TestResult {

    @Id
    private String id;
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime test_day;
    private String test_name;
    private String test_id;

    private String owner;

    private boolean checked;

    public TestResult(LocalDateTime test_day, String test_name, String test_id, String owner, boolean checked) {
        this.test_day = test_day;
        this.test_name = test_name;
        this.test_id = test_id;
        this.owner = owner;
        this.checked = checked;
    }
}
