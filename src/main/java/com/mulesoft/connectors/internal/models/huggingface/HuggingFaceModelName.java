package com.mulesoft.connectors.internal.models.huggingface;

public enum HuggingFaceModelName {
    TII_UAE_FALCON_7B_INSTRUCT("tiiuae/falcon-7b-instruct", true),
    PHI3("microsoft/Phi-3.5-mini-instruct", true),
    MISTRAL_7B_INSTRUCT_V03("mistralai/Mistral-7B-Instruct-v0.3", true),
    TINY_LLAMA("TinyLlama/TinyLlama-1.1B-Chat-v1.0", true);

    private final String value;
    private final boolean textGenerationSupport;

    HuggingFaceModelName(String value, boolean textGenerationSupport) {
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