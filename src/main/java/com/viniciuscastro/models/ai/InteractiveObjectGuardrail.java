package com.viniciuscastro.models.ai;

import com.viniciuscastro.dto.response.openai.InteractiveObjects;

import io.quarkiverse.langchain4j.guardrails.AbstractJsonExtractorOutputGuardrail;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InteractiveObjectGuardrail extends AbstractJsonExtractorOutputGuardrail {
    @Override
    protected Class<?> getOutputClass() {
        return InteractiveObjects.class;
    }
}
