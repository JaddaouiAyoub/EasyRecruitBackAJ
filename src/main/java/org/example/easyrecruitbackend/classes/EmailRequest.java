package org.example.easyrecruitbackend.classes;
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor

public class EmailRequest {
    private String to;
    private String subject;
    private String body;


}

