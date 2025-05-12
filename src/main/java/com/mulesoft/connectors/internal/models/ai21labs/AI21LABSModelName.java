package com.mulesoft.connectors.internal.models.ai21labs;

public enum AI21LABSModelName {
    JAMBA_LARGE("jamba-large", true),
    JAMBA_MINI("jamba-mini", true);

    private final String value;
    private final boolean textGenerationSupport;

    AI21LABSModelName(String value, boolean textGenerationSupport) {
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