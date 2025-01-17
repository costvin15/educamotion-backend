package com.viniciuscastro.interfaces;

import java.net.URL;

import com.viniciuscastro.dto.response.openai.InteractiveObjects;
import com.viniciuscastro.models.ai.InteractiveObjectGuardrail;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.ImageUrl;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrails;

@RegisterAiService
public interface OpenAIService {
    @SystemMessage("""
            Você é um assistente de IA que sugere objetos interativos que podem ser adicionados com base em uma imagem.
            Os objetos interativos disponiveis são Questões objetivas, Questões subjetivas, Questões de Múltipla Escolha e Nuvem de Palavras.
            """)
    @UserMessage("""
            Descreva a imagem e sugira objetos interativos. A resposta deve conter apenas JSON com dados de objetos interativos, com nenhuma outra frase.
            A resposta deve ser uma lista "interactive_objects" com objetos. Os campos obrigatórios de cada objeto são: "type" (enum: "QUESTION_OBJECTIVE", "QUESTION_SUBJECTIVE", "QUESTION_MULTIPLE_CHOICE", "WORD_CLOUD"), "question" (apenas para "QUESTION_OBJECTIVE", "QUESTION_SUBJECTIVE" e "QUESTION_MULTIPLE_CHOICE"),
            "options" (apenas para "QUESTION_OBJECTIVE" e "QUESTION_MULTIPLE_CHOICE"), "words" (apenas para "WORD_CLOUD"). O campo "words" deve ser um objeto contendo "value" e "weight".
            """)
    @OutputGuardrails(InteractiveObjectGuardrail.class)
    InteractiveObjects generateInteractiveObjects(@ImageUrl URL link);
}
