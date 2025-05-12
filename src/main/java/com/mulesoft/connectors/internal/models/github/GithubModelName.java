package com.mulesoft.connectors.internal.models.github;

public enum GithubModelName {
    GPT_4O("gpt-4o", true),
    PHI_3_5_MOE_INSTRUCT("Phi-3.5-MoE-instruct", true),
    MISTRAL_LARGE("Mistral-large", true),
    MISTRAL_SMALL("Mistral-small", true),
    GPT_4_TURBO("gpt-4-turbo", true),
    AI21_JAMBA_1_5_LARGE("AI21-Jamba-1.5-Large", true),
    COHERE_COMMAND_R("Cohere-command-r", true);

    private final String value;
    private final boolean textGenerationSupport;

    GithubModelName(String value, boolean textGenerationSupport) {
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