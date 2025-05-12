package com.mulesoft.connectors.internal.models.groq;

public enum GroqModelName {
    MIXTRAL_8X7B_32768("mixtral-8x7b-32768", true),
    LLAMA_3_2_3B_PREVIEW("llama-3.2-3b-preview", true),
    LLAMA_3_70B_8192("llama3-70b-8192", true),
    LLAMA_3_2_90B_VISION_PREVIEW("llama-3.2-90b-vision-preview", true),
    LLAMA_3_2_11B_TEXT_PREVIEW("llama-3.2-11b-text-preview", true),
    LLAMA_3_2_1B_PREVIEW("llama-3.2-1b-preview", true),
    GEMMA2_9B_IT("gemma2-9b-it", true),
    LLAMA3_GROQ_8B_8192_TOOL_USE_PREVIEW("llama3-groq-8b-8192-tool-use-preview", true),
    LLAVA_V1_5_7B_4096_PREVIEW("llava-v1.5-7b-4096-preview", true),
    LLAMA_3_2_11B_VISION_PREVIEW("llama-3.2-11b-vision-preview", true),
    LLAMA_3_2_90B_TEXT_PREVIEW("llama-3.2-90b-text-preview", true),
    LLAMA_3_1_8B_INSTANT("llama-3.1-8b-instant", true),
    LLAMA3_8B_8192("llama3-8b-8192", true),
    LLAMA3_GROQ_70B_8192_TOOL_USE_PREVIEW("llama3-groq-70b-8192-tool-use-preview", true),
    LLAMA_3_1_70B_VERSATILE("llama-3.1-70b-versatile", true),
    GEMMA_7B_IT("gemma-7b-it", true),
    LLAMA_GUARD_3_8B("llama-guard-3-8b", true);

    private final String value;
    private final boolean textGenerationSupport;

    GroqModelName(String value, boolean textGenerationSupport) {
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