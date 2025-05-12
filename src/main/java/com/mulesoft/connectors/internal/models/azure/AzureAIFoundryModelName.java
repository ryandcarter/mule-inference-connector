package com.mulesoft.connectors.internal.models.azure;

public enum AzureAIFoundryModelName {

    DEEPSEEK_V3("DeepSeek-V3", true),
    GPT_4O_MINI("gpt-4o-mini", true);

    private final String value;
    private final boolean textGenerationSupport;

    AzureAIFoundryModelName(String value, boolean textGenerationSupport) {
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
