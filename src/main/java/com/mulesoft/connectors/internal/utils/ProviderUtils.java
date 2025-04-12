package com.mulesoft.connectors.internal.utils;

import com.mulesoft.connectors.internal.config.*;
import com.mulesoft.connectors.internal.connection.ChatCompletionBase;
import com.mulesoft.connectors.internal.connection.ModerationImageGenerationBase;
import org.jetbrains.annotations.NotNull;
import org.mule.runtime.http.api.client.HttpClient;

/**
 * Utility class for provider-specific operations.
 */
public class ProviderUtils {

    /**
     * Check if the inference type is OLLAMA
     * @param configuration the connector configuration
     * @return true if the inference type is OLLAMA, false otherwise
     */
    public static boolean isOllama(ChatCompletionBase configuration) {
        return "OLLAMA".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is OLLAMA
     * @param configuration the connector configuration
     * @return true if the inference type is OLLAMA, false otherwise
     */
    public static boolean isHuggingFace(ChatCompletionBase configuration) {
        return "HUGGING_FACE".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is Anthropic
     * @param configuration the connector configuration
     * @return true if the inference type is Anthropic, false otherwise
     */
    public static boolean isAnthropic(ChatCompletionBase configuration) {
        return "ANTHROPIC".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is NVIDIA
     * @param configuration the connector configuration
     * @return true if the inference type is NVIDIA, false otherwise
     */
    public static boolean isNvidia(ChatCompletionBase configuration) {
        return "NVIDIA".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is Cohere
     * @param configuration the connector configuration
     * @return true if the inference type is Cohere, false otherwise
     */
    public static boolean isCohere(ChatCompletionBase configuration) {
        return "COHERE".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is VERTEX_AI_EXPRESS
     * @param configuration the connector configuration
     * @return true if the inference type is Cohere, false otherwise
     */
    public static boolean isVertexAIExpress(ChatCompletionBase configuration) {
        return "VERTEX_AI_EXPRESS".equals(configuration.getInferenceType());
    }


    /**
     * Check if the inference type is OPENAI
     * @param configuration the connector configuration
     * @return true if the inference type is Cohere, false otherwise
     */
    public static boolean isOpenAI(ChatCompletionBase configuration) {
        return "OPENAI".equals(configuration.getInferenceType());

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


    public static @NotNull TextGenerationConfig convertToInferenceConfig(VisionConfig visionConfig) {
        return convert(visionConfig);
    }

    public static @NotNull TextGenerationConfig convertToInferenceConfig(ImageGenerationConfig imageConfig) {
        return convert(imageConfig);
    }

    private static @NotNull TextGenerationConfig convert(Object config) {
        TextGenerationConfig inferenceConfig = new TextGenerationConfig();

        if (config instanceof VisionConfig vision) {
//            inferenceConfig.setInferenceType(vision.getInferenceType());
//            inferenceConfig.setApiKey(vision.getApiKey());
//            inferenceConfig.setModelName(vision.getModelName());
//            inferenceConfig.setMaxTokens(vision.getMaxTokens());
//            inferenceConfig.setTemperature(vision.getTemperature());
//            inferenceConfig.setTopP(vision.getTopP());
//            inferenceConfig.setTimeout(vision.getTimeout());
            inferenceConfig.setOllamaUrl(vision.getOllamaUrl());
            inferenceConfig.setVirtualKey(vision.getVirtualKey());
            inferenceConfig.setOpenAICompatibleURL(vision.getOpenAICompatibleURL());
        } else if (config instanceof obsolete_ImageGenerationConfiguration) {
            obsolete_ImageGenerationConfiguration image = (obsolete_ImageGenerationConfiguration) config;
//            inferenceConfig.setInferenceType(image.getInferenceType());
//            inferenceConfig.setApiKey(image.getApiKey());
//            inferenceConfig.setModelName(image.getModelName());
//            inferenceConfig.setTimeout("600000");
        }

        return inferenceConfig;
    }



    public static @NotNull ChatCompletionBase convertToBaseConnection(ModerationImageGenerationBase imageGenerationBase) {
        BaseConnectionImpl baseConnection = new BaseConnectionImpl();

        baseConnection.setHttpClient(imageGenerationBase.getHttpClient());
        baseConnection.setInferenceType(imageGenerationBase.getInferenceType());
        baseConnection.setApiKey(imageGenerationBase.getApiKey());
        baseConnection.setModelName(imageGenerationBase.getModelName());
        baseConnection.setTimeout(imageGenerationBase.getTimeout());

        return baseConnection;
    }

    private static class BaseConnectionImpl implements ChatCompletionBase {
        private HttpClient httpClient;
        private String inferenceType;
        private String apiKey;
        private String modelName;
        private int timeout;
        private Number maxTokens;
        private Number temperature;
        private Number topP;

        @Override
        public HttpClient getHttpClient() { return httpClient; }
        public void setHttpClient(HttpClient httpClient) { this.httpClient = httpClient; }

        @Override
        public String getInferenceType() { return inferenceType; }
        public void setInferenceType(String inferenceType) { this.inferenceType = inferenceType; }

        @Override
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }

        @Override
        public String getModelName() { return modelName; }
        public void setModelName(String modelName) { this.modelName = modelName; }

        @Override
        public int getTimeout() { return timeout; }
        public void setTimeout(int timeout) { this.timeout = timeout; }

        @Override
        public Number getMaxTokens() { return maxTokens; }
        public void setMaxTokens(Number maxTokens) { this.maxTokens = maxTokens; }

        @Override
        public Number getTemperature() { return temperature; }
        public void setTemperature(Number temperature) { this.temperature = temperature; }

        @Override
        public Number getTopP() { return topP; }
        public void setTopP(Number topP) { this.topP = topP; }
    }

}