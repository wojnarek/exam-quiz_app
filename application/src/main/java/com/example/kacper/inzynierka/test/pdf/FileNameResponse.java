package com.example.kacper.inzynierka.test.pdf;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FileNameResponse {
    private String filename;

    public FileNameResponse(String filename) {
        this.filename = filename;
    }
}
