package com.viniciuscastro.dto.response.openai;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionObjective extends InteractiveObject {
    private String question;
    private List<String> options;    
}
