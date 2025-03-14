package com.mulesoft.connectors.internal.utils;

import com.mulesoft.connectors.internal.config.InferenceConfiguration;
import com.mulesoft.connectors.internal.constants.InferenceConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility class for payload operations.
 */
public class PayloadUtils {
    private static final String[] NO_TEMPERATURE_MODELS = {"o3-mini", "o1", "o1-mini"};

    /**
     * Build the payload for the API request
     * @param configuration the connector configuration
     * @param messagesArray the messages array
     * @param toolsArray the tools array (can be null)
     * @return the payload as a JSON object
     */
    public static JSONObject buildPayload(InferenceConfiguration configuration, JSONArray messagesArray, JSONArray toolsArray) {
        JSONObject payload = new JSONObject();
        if (!"AZURE_OPENAI".equals(configuration.getInferenceType())) {
            payload.put(InferenceConstants.MODEL, configuration.getModelName());
        }
        payload.put(InferenceConstants.MESSAGES, messagesArray);

        // Different max token parameter names for different providers
        if ("GROQ".equalsIgnoreCase(configuration.getInferenceType()) ||
                "OPENAI".equalsIgnoreCase(configuration.getInferenceType())) {
            payload.put(InferenceConstants.MAX_COMPLETION_TOKENS, configuration.getMaxTokens());
        } else {
            payload.put(InferenceConstants.MAX_TOKENS, configuration.getMaxTokens());
        }

        // Some models don't support temperature/top_p parameters
        String modelName = configuration.getModelName();
        if (!Arrays.asList(NO_TEMPERATURE_MODELS).contains(modelName)) {
            payload.put(InferenceConstants.TEMPERATURE, configuration.getTemperature());
            payload.put(InferenceConstants.TOP_P, configuration.getTopP());
        }

        // Add tools array if provided
        if (toolsArray != null && !toolsArray.isEmpty()) {
            payload.put(InferenceConstants.TOOLS, toolsArray);
        }

        // Special handling for Ollama's and Azure OpenAI's stream parameter
        if ("OLLAMA".equals(configuration.getInferenceType()) || "AZURE_OPENAI".equals(configuration.getInferenceType())) {
            payload.put("stream", false);
        }

        return payload;
    }

    /**
     * Parse an input stream to a JSON array
     * @param inputStream the input stream to parse
     * @return the parsed JSON array
     * @throws IOException if an error occurs during parsing
     */
    public static JSONArray parseInputStreamToJsonArray(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return new JSONArray();
        }

        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String jsonString = stringBuilder.toString().trim();
            if (jsonString.isEmpty()) {
                return new JSONArray();
            }

            return new JSONArray(jsonString);
        }
    }

    /**
     * Creates a messages array with system prompt and user message
     * @param configuration the connector configuration
     * @param systemContent content for the system/assistant message
     * @param userContent content for the user message
     * @return JSONArray containing the messages
     */
    public static JSONArray createMessagesArrayWithSystemPrompt(
            InferenceConfiguration configuration, String systemContent, String userContent) {
        JSONArray messagesArray = new JSONArray();

        // Create system/assistant message based on provider
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", ProviderUtils.isAnthropic(configuration) ? "assistant" : "system");
        systemMessage.put("content", systemContent);
        messagesArray.put(systemMessage);

        // Create user message
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", userContent);
        messagesArray.put(userMessage);

        return messagesArray;
    }
} 