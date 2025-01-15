package org.example.easyrecruitbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EmailDTO {
    private String to;

    private String subject;

    private Map<String, Object> variables;
}
