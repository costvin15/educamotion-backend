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
            Os tipos de objetos interativos disponiveis são Questões de única alternativa ("QUESTION_OBJECTIVE"), Questões Subjetivas ("QUESTION_SUBJECTIVE"), Questões de Múltipla Escolha ("QUESTION_MULTIPLE_CHOICE") e Nuvem de Palavras ("WORD_CLOUD").
            """)
    @UserMessage("""
            Descreva a imagem e sugira objetos interativos. Não é necessário gerar todos os tipos de objeto interativo.disponíveis, apenas os que fazem sentido para a imagem. É possível sugerir mais de um objeto interativo do mesmo tipo.
            A resposta deve conter apenas JSON com dados de objetos interativos, com nenhuma outra frase.
            A resposta deve ser uma lista "interactive_objects" com objetos.
            Os campos obrigatórios de cada objeto são: "type" (enum: "QUESTION_OBJECTIVE", "QUESTION_SUBJECTIVE", "QUESTION_MULTIPLE_CHOICE", "WORD_CLOUD"), "title" (apenas para "QUESTION_OBJECTIVE", "QUESTION_SUBJECTIVE" e "QUESTION_MULTIPLE_CHOICE"), "description" (apenas para "QUESTION_OBJECTIVE", "QUESTION_SUBJECTIVE" e "QUESTION_MULTIPLE_CHOICE"),
            "options" (array de strings, apenas para "QUESTION_OBJECTIVE" e "QUESTION_MULTIPLE_CHOICE"), "answer" (apenas para "QUESTION_OBJECTIVE"), "correct_options" (apenas para "QUESTION_MULTIPLE_CHOICE") e "words" (apenas para "WORD_CLOUD"). O campo "words" deve ser um objeto contendo "value" e "weight".
            """)
    @OutputGuardrails(InteractiveObjectGuardrail.class)
    InteractiveObjects generateInteractiveObjects(@ImageUrl URL link);
}
