package com.mulesoft.connectors.internal.utils;

import com.mulesoft.connectors.internal.config.ImageGenerationConfiguration;
import com.mulesoft.connectors.internal.config.InferenceConfiguration;
import com.mulesoft.connectors.internal.config.VisionConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for provider-specific operations.
 */
public class ProviderUtils {

    /**
     * Check if the inference type is OLLAMA
     * @param configuration the connector configuration
     * @return true if the inference type is OLLAMA, false otherwise
     */
    public static boolean isOllama(InferenceConfiguration configuration) {
        return "OLLAMA".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is Anthropic
     * @param configuration the connector configuration
     * @return true if the inference type is Anthropic, false otherwise
     */
    public static boolean isAnthropic(InferenceConfiguration configuration) {
        return "ANTHROPIC".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is NVIDIA
     * @param configuration the connector configuration
     * @return true if the inference type is NVIDIA, false otherwise
     */
    public static boolean isNvidia(InferenceConfiguration configuration) {
        return "NVIDIA".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is Cohere
     * @param configuration the connector configuration
     * @return true if the inference type is Cohere, false otherwise
     */
    public static boolean isCohere(InferenceConfiguration configuration) {
        return "COHERE".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is VERTEX_AI_EXPRESS
     * @param configuration the connector configuration
     * @return true if the inference type is Cohere, false otherwise
     */
    public static boolean isVertexAIExpress(InferenceConfiguration configuration) {
        return "VERTEX_AI_EXPRESS".equals(configuration.getInferenceType());
    }
    
    public static @NotNull InferenceConfiguration convertToInferenceConfig_old(VisionConfiguration visionConfig) {
        InferenceConfiguration inferenceConfig = new InferenceConfiguration();

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


    public static @NotNull InferenceConfiguration convertToInferenceConfig(VisionConfiguration visionConfig) {
        return convert(visionConfig);
    }

    public static @NotNull InferenceConfiguration convertToInferenceConfig(ImageGenerationConfiguration imageConfig) {
        return convert(imageConfig);
    }

    private static @NotNull InferenceConfiguration convert(Object config) {
        InferenceConfiguration inferenceConfig = new InferenceConfiguration();

        if (config instanceof VisionConfiguration) {
            VisionConfiguration vision = (VisionConfiguration) config;
            inferenceConfig.setInferenceType(vision.getInferenceType());
            inferenceConfig.setApiKey(vision.getApiKey());
            inferenceConfig.setModelName(vision.getModelName());
            inferenceConfig.setMaxTokens(vision.getMaxTokens());
            inferenceConfig.setTemperature(vision.getTemperature());
            inferenceConfig.setTopP(vision.getTopP());
            inferenceConfig.setTimeout(vision.getTimeout());
            inferenceConfig.setOllamaUrl(vision.getOllamaUrl());
            inferenceConfig.setVirtualKey(vision.getVirtualKey());
        } else if (config instanceof ImageGenerationConfiguration) {
            ImageGenerationConfiguration image = (ImageGenerationConfiguration) config;
            inferenceConfig.setInferenceType(image.getInferenceType());
            inferenceConfig.setApiKey(image.getApiKey());
            inferenceConfig.setModelName(image.getModelName());
        }

        return inferenceConfig;
    }

    public static boolean isOpenAI(InferenceConfiguration configuration) {
        return "OPENAI".equals(configuration.getInferenceType());

    }
}