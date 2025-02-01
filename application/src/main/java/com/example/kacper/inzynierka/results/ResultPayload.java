package com.example.kacper.inzynierka.results;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ResultPayload {
    private String test_id;
    private String test_day;

}
