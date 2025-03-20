package com.mulesoft.connectors.internal.operations;

import com.mulesoft.connectors.internal.api.metadata.LLMResponseAttributes;
import com.mulesoft.connectors.internal.config.InferenceConfiguration;
import com.mulesoft.connectors.internal.config.VisionConfiguration;
import com.mulesoft.connectors.internal.exception.InferenceErrorType;
import com.mulesoft.connectors.internal.utils.ConnectionUtils;
import com.mulesoft.connectors.internal.utils.PayloadUtils;
import com.mulesoft.connectors.internal.utils.ProviderUtils;
import com.mulesoft.connectors.internal.utils.ResponseUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;

import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

/**
 * This class contains operations for the inference connector.
 * Each public method represents an extension operation.
 */
public class VisionOperations {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisionOperations.class);
    private static final String ERROR_MSG_FORMAT = "%s result error";

    /**
     * Chat completions by messages array including system, users messages i.e. conversation history
     * @param configuration the connector configuration
     * @param prompt the users prompt
     * @param imageUrl the image Url to be sent to the Vision Model
     * @return result containing the LLM response
     * @throws ModuleException if an error occurs during the operation
     */
    @MediaType(value = APPLICATION_JSON, strict = false)
    @Alias("Read-image")
    @OutputJsonType(schema = "api/response/Response.json")
    public Result<InputStream, LLMResponseAttributes> readImage(
            @Config VisionConfiguration configuration,
            @Content String prompt,
            @Content(primary = true) String imageUrl) throws ModuleException {
        try {

            JSONArray messagesArray = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            JSONArray contentArray = new JSONArray();
            JSONObject textContent = new JSONObject();
            textContent.put("type", "text");
            textContent.put("text", prompt);
            contentArray.put(textContent);

            JSONObject imageContent = new JSONObject();
            imageContent.put("type", "image_url");

            JSONObject imageMessage = new JSONObject();
            imageMessage.put("url", imageUrl);
            imageContent.put("image_url", imageMessage);

            contentArray.put(imageContent);

            userMessage.put("content", contentArray);
            messagesArray.put(userMessage);

            InferenceConfiguration inferenceConfig = ProviderUtils.convertToInferenceConfig(configuration);

            URL chatCompUrl = ConnectionUtils.getConnectionURLChatCompletion(inferenceConfig);

            LOGGER.debug("Chatting with {}", chatCompUrl);

            // Use the converted configuration
            JSONObject payload = PayloadUtils.buildPayload(inferenceConfig, messagesArray, null);

            String response = ConnectionUtils.executeREST(chatCompUrl, inferenceConfig, payload.toString());

            LOGGER.debug("Chat completions result {}", response);
            return ResponseUtils.processLLMResponse(response, inferenceConfig);
        } catch (Exception e) {
            LOGGER.error("Error in chat completions: {}", e.getMessage(), e);
            throw new ModuleException(String.format(ERROR_MSG_FORMAT, "Chat completions"),
                    InferenceErrorType.CHAT_COMPLETION, e);
        }
    }
}