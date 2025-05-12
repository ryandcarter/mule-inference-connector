package com.mulesoft.connectors.internal.models.xai;

public enum XAIModelName {
    GROK_2_1212("grok-2-1212", true),
    GROK_2_VISION_1212("grok-2-vision-1212", true),
    GROK_3_BETA("grok-3-beta", true),
    GROK_3_MINI_BETA("grok-3-mini-beta", true);

    private final String value;
    private final boolean textGenerationSupport;

    XAIModelName(String value, boolean textGenerationSupport) {
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