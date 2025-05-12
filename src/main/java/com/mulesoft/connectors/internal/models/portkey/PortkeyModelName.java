package com.mulesoft.connectors.internal.models.portkey;

public enum PortkeyModelName {
    GPT_4O("gpt-4o", true),
    CHATGPT_4O_LATEST("chatgpt-4o-latest", true),
    GPT_4O_MINI("gpt-4o-mini", true),
    MISTRAL_LARGE_LATEST("mistral-large-latest", true),
    MISTRAL_SMALL_LATEST("mistral-small-latest", true),
    GPT_4_TURBO("gpt-4-turbo", true),
    GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview", true),
    GPT_4("gpt-4", true),
    GPT_3_5_TURBO("gpt-3.5-turbo", true);

    private final String value;
    private final boolean textGenerationSupport;

    PortkeyModelName(String value, boolean textGenerationSupport) {
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