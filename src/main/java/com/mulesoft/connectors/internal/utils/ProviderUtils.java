package com.mulesoft.connectors.internal.utils;

import com.mulesoft.connectors.internal.config.InferenceConfiguration;
import com.mulesoft.connectors.internal.config.VisionConfiguration;

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

    // Add this utility method to your class or to a utility class
    public static InferenceConfiguration convertToInferenceConfig(VisionConfiguration visionConfig) {
        // Create a new instance of InferenceConfiguration
        InferenceConfiguration inferenceConfig = new InferenceConfiguration();

        // Copy all the shared properties from VisionConfiguration to InferenceConfiguration
        // Using reflection or setter methods

        // Using setter methods (you'll need to add them to InferenceConfiguration if they don't exist)
        try {
            // Use reflection to set fields since the class might not have setters
            java.lang.reflect.Field inferenceTypeField = InferenceConfiguration.class.getDeclaredField("inferenceType");
            inferenceTypeField.setAccessible(true);
            inferenceTypeField.set(inferenceConfig, visionConfig.getInferenceType());

            java.lang.reflect.Field apiKeyField = InferenceConfiguration.class.getDeclaredField("apiKey");
            apiKeyField.setAccessible(true);
            apiKeyField.set(inferenceConfig, visionConfig.getApiKey());

            java.lang.reflect.Field modelNameField = InferenceConfiguration.class.getDeclaredField("modelName");
            modelNameField.setAccessible(true);
            modelNameField.set(inferenceConfig, visionConfig.getModelName());

            java.lang.reflect.Field maxTokensField = InferenceConfiguration.class.getDeclaredField("maxTokens");
            maxTokensField.setAccessible(true);
            maxTokensField.set(inferenceConfig, visionConfig.getMaxTokens());

            java.lang.reflect.Field temperatureField = InferenceConfiguration.class.getDeclaredField("temperature");
            temperatureField.setAccessible(true);
            temperatureField.set(inferenceConfig, visionConfig.getTemperature());

            // Set default value for topP since VisionConfiguration doesn't have it
            java.lang.reflect.Field topPField = InferenceConfiguration.class.getDeclaredField("topP");
            topPField.setAccessible(true);
            topPField.set(inferenceConfig, 0.9);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error converting VisionConfiguration to InferenceConfiguration", e);
        }

        return inferenceConfig;
    }
} 