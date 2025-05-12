package com.mulesoft.connectors.internal.models.ollama;

public enum OllamaModelName {
    MISTRAL("mistral", true),
    PHI3("phi3", true),
    ORCA_MINI("orca-mini", true),
    LLAMA2("llama2", true),
    CODE_LLAMA("codellama", true),
    TINY_LLAMA("tinyllama", true);

    private final String value;
    private final boolean textGenerationSupport;

    OllamaModelName(String value, boolean textGenerationSupport) {
        this.value = value;
        this.textGenerationSupport = textGenerationSupport;
    }

    public boolean isTextGenerationSupport() {
        return textGenerationSupport;
    }

    @Override
    public String toString() {
        return this.value;
    }
} 