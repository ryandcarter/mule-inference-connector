package com.mulesoft.connectors.internal.utils;

import com.mulesoft.connectors.internal.config.obsolete_ImageGenerationConfiguration;
import com.mulesoft.connectors.internal.config.obsolete_InferenceConfiguration;
import com.mulesoft.connectors.internal.config.obsolete_VisionConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for provider-specific operations.
 */
public class obsolete_ProviderUtils {

    /**
     * Check if the inference type is OLLAMA
     * @param configuration the connector configuration
     * @return true if the inference type is OLLAMA, false otherwise
     */
    public static boolean isOllama(obsolete_InferenceConfiguration configuration) {
        return "OLLAMA".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is OLLAMA
     * @param configuration the connector configuration
     * @return true if the inference type is OLLAMA, false otherwise
     */
    public static boolean isHuggingFace(obsolete_InferenceConfiguration configuration) {
        return "HUGGING_FACE".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is Anthropic
     * @param configuration the connector configuration
     * @return true if the inference type is Anthropic, false otherwise
     */
    public static boolean isAnthropic(obsolete_InferenceConfiguration configuration) {
        return "ANTHROPIC".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is NVIDIA
     * @param configuration the connector configuration
     * @return true if the inference type is NVIDIA, false otherwise
     */
    public static boolean isNvidia(obsolete_InferenceConfiguration configuration) {
        return "NVIDIA".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is Cohere
     * @param configuration the connector configuration
     * @return true if the inference type is Cohere, false otherwise
     */
    public static boolean isCohere(obsolete_InferenceConfiguration configuration) {
        return "COHERE".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is VERTEX_AI_EXPRESS
     * @param configuration the connector configuration
     * @return true if the inference type is Cohere, false otherwise
     */
    public static boolean isVertexAIExpress(obsolete_InferenceConfiguration configuration) {
        return "VERTEX_AI_EXPRESS".equals(configuration.getInferenceType());
    }
    
    public static @NotNull obsolete_InferenceConfiguration convertToInferenceConfig_old(obsolete_VisionConfiguration visionConfig) {
        obsolete_InferenceConfiguration inferenceConfig = new obsolete_InferenceConfiguration();

        inferenceConfig.setInferenceType(visionConfig.getInferenceType());
        inferenceConfig.setApiKey(visionConfig.getApiKey());
        inferenceConfig.setModelName(visionConfig.getModelName());
        inferenceConfig.setMaxTokens(visionConfig.getMaxTokens());
        inferenceConfig.setTemperature(visionConfig.getTemperature());
        inferenceConfig.setTopP(visionConfig.getTopP());
        inferenceConfig.setTimeout(visionConfig.getTimeout());
        inferenceConfig.setOllamaUrl(visionConfig.getOllamaUrl());
        inferenceConfig.setVirtualKey(visionConfig.getVirtualKey());

        return inferenceConfig;
    }


    public static @NotNull obsolete_InferenceConfiguration convertToInferenceConfig(obsolete_VisionConfiguration visionConfig) {
        return convert(visionConfig);
    }

    public static @NotNull obsolete_InferenceConfiguration convertToInferenceConfig(obsolete_ImageGenerationConfiguration imageConfig) {
        return convert(imageConfig);
    }

    private static @NotNull obsolete_InferenceConfiguration convert(Object config) {
        obsolete_InferenceConfiguration inferenceConfig = new obsolete_InferenceConfiguration();

        if (config instanceof obsolete_VisionConfiguration) {
            obsolete_VisionConfiguration vision = (obsolete_VisionConfiguration) config;
            inferenceConfig.setInferenceType(vision.getInferenceType());
            inferenceConfig.setApiKey(vision.getApiKey());
            inferenceConfig.setModelName(vision.getModelName());
            inferenceConfig.setMaxTokens(vision.getMaxTokens());
            inferenceConfig.setTemperature(vision.getTemperature());
            inferenceConfig.setTopP(vision.getTopP());
            inferenceConfig.setTimeout(vision.getTimeout());
            inferenceConfig.setOllamaUrl(vision.getOllamaUrl());
            inferenceConfig.setVirtualKey(vision.getVirtualKey());
            inferenceConfig.setOpenAICompatibleURL(vision.getOpenAICompatibleURL());
        } else if (config instanceof obsolete_ImageGenerationConfiguration) {
            obsolete_ImageGenerationConfiguration image = (obsolete_ImageGenerationConfiguration) config;
            inferenceConfig.setInferenceType(image.getInferenceType());
            inferenceConfig.setApiKey(image.getApiKey());
            inferenceConfig.setModelName(image.getModelName());
            inferenceConfig.setTimeout("600000");
        }

        return inferenceConfig;
    }

    public static boolean isOpenAI(obsolete_InferenceConfiguration configuration) {
        return "OPENAI".equals(configuration.getInferenceType());

    }
}