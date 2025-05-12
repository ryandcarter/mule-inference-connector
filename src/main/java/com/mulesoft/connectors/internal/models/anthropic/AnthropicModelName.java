package com.mulesoft.connectors.internal.models.anthropic;

public enum AnthropicModelName {

    CLAUDE_3_7_SONNET_LATEST("claude-3-7-sonnet-latest", true),
    CLAUDE_3_5_HAIKU_LATEST("claude-3-5-haiku-latest", true),
    CLAUDE_3_5_SONNET_LATEST("claude-3-5-sonnet-latest", true),
    CLAUDE_3_OPUS_LATEST("claude-3-opus-latest", true);

    private final String value;
    private final boolean textGenerationSupport;

    AnthropicModelName(String value, boolean textGenerationSupport) {
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
