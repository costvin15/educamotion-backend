package com.viniciuscastro.elements.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerReportResponse {
    private List<QuestionAnswerGroupedByPage> pages;
}
