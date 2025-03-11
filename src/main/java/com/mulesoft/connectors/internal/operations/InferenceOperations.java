package com.mulesoft.connectors.internal.operations;

import com.mulesoft.connectors.internal.api.metadata.LLMResponseAttributes;
import com.mulesoft.connectors.internal.api.metadata.TokenUsage;
import com.mulesoft.connectors.internal.config.InferenceConfiguration;
import com.mulesoft.connectors.internal.constants.InferenceConstants;
import com.mulesoft.connectors.internal.exception.InferenceErrorType;
import com.mulesoft.connectors.internal.helpers.TokenHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.mulesoft.connectors.internal.helpers.ResponseHelper.createLLMResponse;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

/**
 * This class contains operations for the inference connector.
 * Each public method represents an extension operation.
 */
public class InferenceOperations {
    private static final Logger LOGGER = LoggerFactory.getLogger(InferenceOperations.class);
    private static final String[] NO_TEMPERATURE_MODELS = {"o3-mini", "o1", "o1-mini"};
    private static final String ERROR_MSG_FORMAT = "%s result error";

    /**
     * Chat completions by messages array including system, users messages i.e. conversation history
     * @param configuration the connector configuration
     * @param messages the conversation history as a JSON array
     * @return result containing the LLM response
     * @throws ModuleException if an error occurs during the operation
     */
    @MediaType(value = APPLICATION_JSON, strict = false)
    @Alias("Chat-completions")
    @OutputJsonType(schema = "api/response/Response.json")
    public Result<InputStream, LLMResponseAttributes> chatCompletion(
            @Config InferenceConfiguration configuration,
            @Content InputStream messages) throws ModuleException {
        try {
            JSONArray messagesArray = parseInputStreamToJsonArray(messages);
            URL chatCompUrl = getConnectionURLChatCompletion(configuration);

            JSONObject payload = buildPayload(configuration, messagesArray, null);
            String response = executeREST(chatCompUrl, configuration, payload.toString());

            LOGGER.debug("Chat completions result {}", response);
            return processLLMResponse(response, configuration);
        } catch (Exception e) {
            LOGGER.error("Error in chat completions: {}", e.getMessage(), e);
            throw new ModuleException(String.format(ERROR_MSG_FORMAT, "Chat completions"),
                    InferenceErrorType.CHAT_COMPLETION, e);
        }
    }

    /**
     * Simple chat answer for a single prompt
     * @param configuration the connector configuration
     * @param prompt the user's prompt
     * @return result containing the LLM response
     * @throws ModuleException if an error occurs during the operation
     */
    @MediaType(value = APPLICATION_JSON, strict = false)
    @Alias("Chat-answer-prompt")
    @OutputJsonType(schema = "api/response/Response.json")
    public Result<InputStream, LLMResponseAttributes> chatAnswerPrompt(
            @Config InferenceConfiguration configuration,
            @Content String prompt) throws ModuleException {
        try {
            JSONArray messagesArray = new JSONArray();
            JSONObject usersPrompt = new JSONObject();
            usersPrompt.put("role", "user");
            usersPrompt.put("content", prompt);
            messagesArray.put(usersPrompt);

            URL chatCompUrl = getConnectionURLChatCompletion(configuration);
            JSONObject payload = buildPayload(configuration, messagesArray, null);
            String response = executeREST(chatCompUrl, configuration, payload.toString());

            LOGGER.debug("Chat answer prompt result {}", response);
            return processLLMResponse(response, configuration);
        } catch (Exception e) {
            LOGGER.error("Error in chat answer prompt: {}", e.getMessage(), e);
            throw new ModuleException(String.format(ERROR_MSG_FORMAT, "Chat answer prompt"),
                    InferenceErrorType.CHAT_COMPLETION, e);
        }
    }

    /**
     * Define a prompt template with instructions and data
     * @param configuration the connector configuration
     * @param template the template string
     * @param instructions instructions for the LLM
     * @param data the primary data content
     * @return result containing the LLM response
     * @throws ModuleException if an error occurs during the operation
     */
    @MediaType(value = APPLICATION_JSON, strict = false)
    @Alias("Agent-define-prompt-template")
    @OutputJsonType(schema = "api/response/Response.json")
    public Result<InputStream, LLMResponseAttributes> promptTemplate(
            @Config InferenceConfiguration configuration,
            @Content String template,
            @Content String instructions,
            @Content(primary = true) String data) throws ModuleException {
        try {
            JSONArray messagesArray = createMessagesArrayWithSystemPrompt(
                    configuration, template + " - " + instructions, data);

            URL chatCompUrl = getConnectionURLChatCompletion(configuration);
            JSONObject payload = buildPayload(configuration, messagesArray, null);
            String response = executeREST(chatCompUrl, configuration, payload.toString());

            LOGGER.debug("Agent define prompt template result {}", response);
            return processLLMResponse(response, configuration);
        } catch (Exception e) {
            LOGGER.error("Error in agent define prompt template: {}", e.getMessage(), e);
            throw new ModuleException(String.format(ERROR_MSG_FORMAT, "Agent define prompt template"),
                    InferenceErrorType.CHAT_COMPLETION, e);
        }
    }

    /**
     * Define a tools template with instructions, data and tools
     * @param configuration the connector configuration
     * @param template the template string
     * @param instructions instructions for the LLM
     * @param data the primary data content
     * @param tools tools configuration as a JSON array
     * @return result containing the LLM response
     * @throws ModuleException if an error occurs during the operation
     */
    @MediaType(value = APPLICATION_JSON, strict = false)
    @Alias("Tools-native-template")
    @OutputJsonType(schema = "api/response/Response.json")
    public Result<InputStream, LLMResponseAttributes> toolsTemplate(
            @Config InferenceConfiguration configuration,
            @Content String template,
            @Content String instructions,
            @Content(primary = true) String data,
            @Content InputStream tools) throws ModuleException {
        try {
            JSONArray toolsArray = parseInputStreamToJsonArray(tools);
            JSONArray messagesArray = createMessagesArrayWithSystemPrompt(
                    configuration, template + " - " + instructions, data);

            URL chatCompUrl = getConnectionURLChatCompletion(configuration);
            JSONObject payload = buildPayload(configuration, messagesArray, toolsArray);
            String response = executeREST(chatCompUrl, configuration, payload.toString());

            LOGGER.debug("Tools use native template result {}", response);
            return processResponse(response, configuration, true);
        } catch (Exception e) {
            LOGGER.error("Error in tools use native template: {}", e.getMessage(), e);
            throw new ModuleException(String.format(ERROR_MSG_FORMAT, "Tools use native template"),
                    InferenceErrorType.CHAT_COMPLETION, e);
        }
    }

    /**
     * Creates a messages array with system prompt and user message
     * @param configuration the connector configuration
     * @param systemContent content for the system/assistant message
     * @param userContent content for the user message
     * @return JSONArray containing the messages
     */
    private JSONArray createMessagesArrayWithSystemPrompt(
            InferenceConfiguration configuration, String systemContent, String userContent) {
        JSONArray messagesArray = new JSONArray();

        // Create system/assistant message based on provider
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", isAnthropic(configuration) ? "assistant" : "system");
        systemMessage.put("content", systemContent);
        messagesArray.put(systemMessage);

        // Create user message
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", userContent);
        messagesArray.put(userMessage);

        return messagesArray;
    }

    /**
     * Process the response from the LLM API
     * @param response the response string from the API
     * @param configuration the connector configuration
     * @param isToolsResponse whether this is a tools response
     * @return result containing the LLM response
     * @throws Exception if an error occurs during processing
     */
    private Result<InputStream, LLMResponseAttributes> processResponse(
            String response, InferenceConfiguration configuration, boolean isToolsResponse) throws Exception {

        JSONObject root = new JSONObject(response);
        ResponseInfo responseInfo = extractResponseInfo(root, configuration);

        // Process tool calls if needed
        JSONArray toolCalls = new JSONArray();
        if (isToolsResponse && responseInfo.message.has("tool_calls")) {
            toolCalls = processToolCalls(responseInfo.message.getJSONArray("tool_calls"));
        }

        // Handle Anthropic tool_use for tools responses
        if (isToolsResponse && isAnthropic(configuration)) {
            JSONArray toolsCallAnthropic = extractAnthropicToolCalls(root.getJSONArray("content"));
            if (!toolsCallAnthropic.isEmpty()) {
                responseInfo.message.put("tool_calls", toolsCallAnthropic);
            }
        }

        // Extract content 
        String content = responseInfo.message.has("content") && !responseInfo.message.isNull("content")
                ? responseInfo.message.getString("content") : null;

        // Build response
        TokenUsage tokenUsage = TokenHelper.parseUsageFromResponse(response);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(InferenceConstants.RESPONSE, content);

        if (isToolsResponse) {
            JSONArray finalToolCalls = toolCalls;
            // Check if we have tool calls in the message from Anthropic
            if (responseInfo.message.has("tool_calls") && !responseInfo.message.getJSONArray("tool_calls").isEmpty()) {
                finalToolCalls = responseInfo.message.getJSONArray("tool_calls");
            }
            jsonObject.put(InferenceConstants.TOOLS, finalToolCalls);
        }

        Map<String, String> responseAttributes = new HashMap<>();
        responseAttributes.put(InferenceConstants.FINISH_REASON, responseInfo.finishReason);
        responseAttributes.put(InferenceConstants.MODEL, responseInfo.model);
        responseAttributes.put(InferenceConstants.ID_STRING, responseInfo.id);

        return createLLMResponse(jsonObject.toString(), tokenUsage, responseAttributes);
    }

    /**
     * Process the response from the LLM API for standard chat operations
     * @param response the response string from the API
     * @param configuration the connector configuration
     * @return result containing the LLM response
     * @throws Exception if an error occurs during processing
     */
    private Result<InputStream, LLMResponseAttributes> processLLMResponse(
            String response, InferenceConfiguration configuration) throws Exception {
        return processResponse(response, configuration, false);
    }

    /**
     * Process the response from the LLM API for tools operations
     * @param response the response string from the API
     * @param configuration the connector configuration
     * @return result containing the LLM response
     * @throws Exception if an error occurs during processing
     */
    private Result<InputStream, LLMResponseAttributes> processToolsResponse(
            String response, InferenceConfiguration configuration) throws Exception {
        return processResponse(response, configuration, true);
    }

    /**
     * Container class for response information
     */
    private static class ResponseInfo {
        String model;
        String id;
        JSONObject message;
        String finishReason;
        String text = "";
    }

    /**
     * Extract basic information from the response
     * @param root the root JSON object of the response
     * @param configuration the connector configuration
     * @return ResponseInfo containing the extracted information
     */
    private ResponseInfo extractResponseInfo(JSONObject root, InferenceConfiguration configuration) {
        ResponseInfo info = new ResponseInfo();
        info.model = root.getString("model");
        info.id = isOllama(configuration) ? null : root.getString("id");
        info.message = new JSONObject();

        if (isOllama(configuration)) {
            info.message = root.getJSONObject("message");
            info.finishReason = root.getString("done_reason");
        } else if (isAnthropic(configuration)) {
            info.finishReason = root.getString("stop_reason");

            // Extract text from content array
            if (root.has("content") && root.getJSONArray("content").length() > 0) {
                JSONArray contentArray = root.getJSONArray("content");
                for (int i = 0; i < contentArray.length(); i++) {
                    JSONObject contentItem = contentArray.getJSONObject(i);
                    if ("text".equals(contentItem.getString("type")) && info.text.isEmpty()) {
                        info.text = contentItem.getString("text");
                        break;
                    }
                }
            }

            info.message.put("content", info.text);
        } else {
            JSONArray choicesArray = root.getJSONArray("choices");
            JSONObject firstChoice = choicesArray.getJSONObject(0);
            info.finishReason = isNvidia(configuration) ? "" : firstChoice.getString("finish_reason");
            info.message = firstChoice.getJSONObject("message");
        }

        return info;
    }

    /**
     * Extract tool calls from Anthropic format response
     * @param contentArray the content array from Anthropic response
     * @return JSONArray of tool calls in a standardized format
     */
    private JSONArray extractAnthropicToolCalls(JSONArray contentArray) {
        JSONArray toolCalls = new JSONArray();

        for (int i = 0; i < contentArray.length(); i++) {
            JSONObject contentItem = contentArray.getJSONObject(i);
            String type = contentItem.getString("type");

            if ("tool_use".equals(type)) {
                JSONObject functionObject = new JSONObject();
                functionObject.put("name", contentItem.getString("name"));
                functionObject.put("arguments", contentItem.getJSONObject("input").toString());

                JSONObject toolItem = new JSONObject();
                toolItem.put("function", functionObject);
                toolItem.put("id", contentItem.getString("id"));
                toolItem.put("type", "function");

                toolCalls.put(toolItem);
            }
        }

        return toolCalls;
    }

    /**
     * Process tool calls from the LLM response
     * @param toolCalls the raw tool calls JSON array
     * @return processed tool calls JSON array
     */
    private JSONArray processToolCalls(JSONArray toolCalls) {
        JSONArray processedToolCalls = new JSONArray();

        for (int i = 0; i < toolCalls.length(); i++) {
            JSONObject toolCall = toolCalls.getJSONObject(i);

            if (toolCall.has("function")) {
                JSONObject functionObject = toolCall.getJSONObject("function");
                if (functionObject.has("arguments")) {
                    // Try to convert "arguments" from a string to a JSON object
                    try {
                        JSONObject arguments = new JSONObject(functionObject.getString("arguments"));
                        functionObject.put("arguments", arguments);
                    } catch (Exception e) {
                        // Keep original string if not valid JSON
                        LOGGER.warn("Failed to parse function arguments as JSON: {}", e.getMessage());
                    }
                }
            }

            processedToolCalls.put(toolCall);
        }

        return processedToolCalls;
    }

    /**
     * Build the HTTP connection for the API request
     * @param url the URL to connect to
     * @param configuration the connector configuration
     * @return the configured HTTP connection
     * @throws IOException if an error occurs during connection setup
     */
    private static HttpURLConnection getConnectionObject(URL url, InferenceConfiguration configuration) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept", "application/json");

        switch (configuration.getInferenceType()) {
            case "ANTHROPIC":
                conn.setRequestProperty("x-api-key", configuration.getApiKey());
                conn.setRequestProperty("anthropic-version", "2023-06-01");
                break;
            case "PORTKEY":
                conn.setRequestProperty("x-portkey-api-key", configuration.getApiKey());
                conn.setRequestProperty("x-portkey-virtual-key", configuration.getVirtualKey());
                break;
            default:
                conn.setRequestProperty("Authorization", "Bearer " + configuration.getApiKey());
                break;
        }

        return conn;
    }

    /**
     * Get the appropriate URL for chat completion based on the configuration
     * @param configuration the connector configuration
     * @return the URL for the chat completion endpoint
     * @throws MalformedURLException if the URL is invalid
     */
    private static URL getConnectionURLChatCompletion(InferenceConfiguration configuration) throws MalformedURLException {
        switch (configuration.getInferenceType()) {
            case "PORTKEY":
                return new URL(InferenceConstants.PORTKEY_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "GROQ":
                return new URL(InferenceConstants.GROQ_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "HUGGING_FACE":
                return new URL(InferenceConstants.HUGGINGFACE_URL + "/models/" + configuration.getModelName() + "/v1" + InferenceConstants.CHAT_COMPLETIONS);
            case "OPENROUTER":
                return new URL(InferenceConstants.OPENROUTER_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "GITHUB":
                return new URL(InferenceConstants.GITHUB_MODELS_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "OLLAMA":
                return new URL(configuration.getOllamaUrl() + InferenceConstants.CHAT_COMPLETIONS_OLLAMA);
            case "CEREBRAS":
                return new URL(InferenceConstants.CEREBRAS_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "NVIDIA":
                return new URL(InferenceConstants.NVIDIA_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "FIREWORKS":
                return new URL(InferenceConstants.FIREWORKS_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "TOGETHER":
                return new URL(InferenceConstants.TOGETHER_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "DEEPINFRA":
                return new URL(InferenceConstants.DEEPINFRA_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "PERPLEXITY":
                return new URL(InferenceConstants.PERPLEXITY_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "XAI":
                return new URL(InferenceConstants.X_AI_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "OPENAI":
                return new URL(InferenceConstants.OPEN_AI_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "MISTRAL":
                return new URL(InferenceConstants.MISTRAL_AI_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "ANTHROPIC":
                return new URL(InferenceConstants.ANTHROPIC_URL + "/" + InferenceConstants.MESSAGES);
            default:
                throw new MalformedURLException("Unsupported inference type: " + configuration.getInferenceType());
        }
    }

    /**
     * Build the payload for the API request
     * @param configuration the connector configuration
     * @param messagesArray the messages array
     * @param toolsArray the tools array (can be null)
     * @return the payload as a JSON object
     */
    private static JSONObject buildPayload(InferenceConfiguration configuration, JSONArray messagesArray, JSONArray toolsArray) {
        JSONObject payload = new JSONObject();
        payload.put(InferenceConstants.MODEL, configuration.getModelName());
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

        // Special handling for Ollama's stream parameter
        if ("OLLAMA".equals(configuration.getInferenceType())) {
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
    private static JSONArray parseInputStreamToJsonArray(InputStream inputStream) throws IOException {
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
     * Execute a REST API call
     * @param resourceUrl the URL to call
     * @param configuration the connector configuration
     * @param payload the payload to send
     * @return the response string
     * @throws IOException if an error occurs during the API call
     */
    private static String executeREST(URL resourceUrl, InferenceConfiguration configuration, String payload) throws IOException {
        HttpURLConnection conn = getConnectionObject(resourceUrl, configuration);

        // Set appropriate timeouts
        conn.setConnectTimeout(30000);  // 30 seconds
        conn.setReadTimeout(120000);    // 2 minutes

        // Send the payload
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read successful response
            return readResponseStream(conn.getInputStream());
        } else {
            // Read error response
            String errorResponse = readResponseStream(conn.getErrorStream());

            LOGGER.error("API request failed with status code: {} and message: {}", responseCode, errorResponse);
            throw new IOException("API request failed with status code: " + responseCode +
                    " and message: " + errorResponse);
        }
    }

    /**
     * Read data from an input stream into a string
     * @param stream the input stream to read from
     * @return the stream contents as a string
     * @throws IOException if an error occurs during reading
     */
    private static String readResponseStream(InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine);
            }
            return response.toString();
        }
    }

    /**
     * Check if the inference type is OLLAMA
     * @param configuration the connector configuration
     * @return true if the inference type is OLLAMA, false otherwise
     */
    private boolean isOllama(InferenceConfiguration configuration) {
        return "OLLAMA".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is Anthropic
     * @param configuration the connector configuration
     * @return true if the inference type is Anthropic, false otherwise
     */
    private boolean isAnthropic(InferenceConfiguration configuration) {
        return "ANTHROPIC".equals(configuration.getInferenceType());
    }

    /**
     * Check if the inference type is NVIDIA
     * @param configuration the connector configuration
     * @return true if the inference type is NVIDIA, false otherwise
     */
    private boolean isNvidia(InferenceConfiguration configuration) {
        return "NVIDIA".equals(configuration.getInferenceType());
    }
}