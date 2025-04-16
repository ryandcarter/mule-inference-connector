package com.mulesoft.connectors.internal.utils;

import com.mulesoft.connectors.internal.config.ImageGenerationConfiguration;
import com.mulesoft.connectors.internal.config.InferenceConfiguration;
import com.mulesoft.connectors.internal.config.VisionConfiguration;
import com.mulesoft.connectors.internal.operations.InferenceOperations;

import org.jetbrains.annotations.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility class for provider-specific operations.
 */
public class ProviderUtils {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(InferenceOperations.class);


    /**
     * Check if the inference type is OLLAMA
     * @param configuration the connector configuration
     * @return true if the inference type is OLLAMA, false otherwise
     */
    public static boolean isOllama(InferenceConfiguration configuration) {
        return "OLLAMA".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is OLLAMA
     * @param configuration the connector configuration
     * @return true if the inference type is OLLAMA, false otherwise
     */
    public static boolean isHuggingFace(InferenceConfiguration configuration) {
        return "HUGGING_FACE".equals(configuration.getInferenceType());
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

    public static boolean isVertexAI(InferenceConfiguration configuration) {
        return "VERTEX_AI".equals(configuration.getInferenceType());
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
            inferenceConfig.setVertexAILocationId(vision.getVertexAILocationId());
            inferenceConfig.setVertexAIProjectId(vision.getVertexAIProjectId());
            inferenceConfig.setOpenAICompatibleURL(vision.getOpenAICompatibleURL());
        } else if (config instanceof ImageGenerationConfiguration) {
            ImageGenerationConfiguration image = (ImageGenerationConfiguration) config;
            inferenceConfig.setInferenceType(image.getInferenceType());
            inferenceConfig.setApiKey(image.getApiKey());
            inferenceConfig.setModelName(image.getModelName());
            inferenceConfig.setTimeout("600000");
        }

        return inferenceConfig;
    }

    public static boolean isOpenAI(InferenceConfiguration configuration) {
        return "OPENAI".equals(configuration.getInferenceType());

    }
    
    //get the providers based on the models
    public static String getProviderByModel(String modelName) {
        LOGGER.debug("model name {}", modelName);

        if (modelName == null || modelName.isEmpty()) {
            return "Unknown";
        }

        String upperName = modelName.toUpperCase();
        
        if (upperName.startsWith("GEMINI")) {
            return "Google";
        } else if (upperName.startsWith("CLAUDE")) {
            return "Anthropic";
        } else if (upperName.startsWith("META")) {
            return "Meta";
        } else {
            return "Unknown";
        }
    }

}