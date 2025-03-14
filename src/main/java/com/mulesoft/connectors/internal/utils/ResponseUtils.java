package com.mulesoft.connectors.internal.utils;

import com.mulesoft.connectors.internal.api.metadata.LLMResponseAttributes;
import com.mulesoft.connectors.internal.api.metadata.TokenUsage;
import com.mulesoft.connectors.internal.config.InferenceConfiguration;
import com.mulesoft.connectors.internal.constants.InferenceConstants;
import com.mulesoft.connectors.internal.helpers.ResponseHelper;
import com.mulesoft.connectors.internal.helpers.TokenHelper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for processing API responses.
 */
public class ResponseUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseUtils.class);

    /**
     * Process the response from the LLM API
     * @param response the response string from the API
     * @param configuration the connector configuration
     * @param isToolsResponse whether this is a tools response
     * @return result containing the LLM response
     * @throws Exception if an error occurs during processing
     */
    public static Result<InputStream, LLMResponseAttributes> processResponse(
            String response, InferenceConfiguration configuration, boolean isToolsResponse) throws Exception {

        JSONObject root = new JSONObject(response);
        ResponseInfo responseInfo = extractResponseInfo(root, configuration);
        String content = null;

        // Process tool calls if needed
        JSONArray toolCalls = new JSONArray();
        if (isToolsResponse && responseInfo.message.has("tool_calls")) {
            toolCalls = processToolCalls(responseInfo.message.getJSONArray("tool_calls"));
        }

        // Handle Anthropic tool_use for tools responses
        if (isToolsResponse && ProviderUtils.isAnthropic(configuration)) {
            JSONArray toolsCallAnthropic = extractAnthropicToolCalls(root.getJSONArray("content"));
            if (!toolsCallAnthropic.isEmpty()) {
                responseInfo.message.put("tool_calls", toolsCallAnthropic);
            }
        }

        if (ProviderUtils.isCohere(configuration)) {
            JSONArray contentArray = responseInfo.message.has("content") && !responseInfo.message.isNull("content")
                    ? responseInfo.message.getJSONArray("content")
                    : null;

            if (contentArray != null && contentArray.length() > 0) {
                JSONObject firstContent = contentArray.getJSONObject(0); // Get the first item in the array
                if (firstContent.has("text") && !firstContent.isNull("text")) {
                    content = firstContent.getString("text"); // Extract the "text" field
                }
            }
        } else {
            content = responseInfo.message.has("content") && !responseInfo.message.isNull("content")
                    ? responseInfo.message.getString("content") : null;
        }

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

        return ResponseHelper.createLLMResponse(jsonObject.toString(), tokenUsage, responseAttributes);
    }

    /**
     * Process the response from the LLM API for standard chat operations
     * @param response the response string from the API
     * @param configuration the connector configuration
     * @return result containing the LLM response
     * @throws Exception if an error occurs during processing
     */
    public static Result<InputStream, LLMResponseAttributes> processLLMResponse(
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
    public static Result<InputStream, LLMResponseAttributes> processToolsResponse(
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
    private static ResponseInfo extractResponseInfo(JSONObject root, InferenceConfiguration configuration) {
        ResponseInfo info = new ResponseInfo();
        info.model = !("AI21LABS".equals(configuration.getInferenceType())
                || "COHERE".equals(configuration.getInferenceType()))
                ? root.getString("model")
                : configuration.getModelName();

        info.id = ProviderUtils.isOllama(configuration) ? null : root.getString("id");
        info.message = new JSONObject();

        if (ProviderUtils.isOllama(configuration)) {
            info.message = root.getJSONObject("message");
            info.finishReason = root.getString("done_reason");
        } else if (ProviderUtils.isCohere(configuration)) {
            info.message = root.getJSONObject("message");
            info.finishReason = root.getString("finish_reason");
        } else if (ProviderUtils.isAnthropic(configuration)) {
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

            info.message = new JSONObject();
            info.message.put("content", info.text);
        } else {
            // Default case for other models (OpenAI, etc.)
            JSONArray choicesArray = root.getJSONArray("choices");
            JSONObject firstChoice = choicesArray.getJSONObject(0);
            info.finishReason = ProviderUtils.isNvidia(configuration) ? "" : firstChoice.getString("finish_reason");
            info.message = firstChoice.getJSONObject("message");
        }

        return info;
    }

    /**
     * Extract tool calls from Anthropic format response
     * @param contentArray the content array from Anthropic response
     * @return JSONArray of tool calls in a standardized format
     */
    private static JSONArray extractAnthropicToolCalls(JSONArray contentArray) {
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
    private static JSONArray processToolCalls(JSONArray toolCalls) {
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
} 