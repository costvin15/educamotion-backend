package com.viniciuscastro.dto.response.openai;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @Type(value = QuestionObjective.class, name = "QUESTION_OBJECTIVE"),
    @Type(value = QuestionSubjetive.class, name = "QUESTION_SUBJECTIVE"),
    @Type(value = QuestionMultipleChoice.class, name = "QUESTION_MULTIPLE_CHOICE"),
    @Type(value = WordCloud.class, name = "WORD_CLOUD")
})
public abstract class InteractiveObject {
}
