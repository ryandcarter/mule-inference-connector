package com.mulesoft.connectors.internal.operations;

import com.mulesoft.connectors.internal.api.metadata.LLMResponseAttributes;
import com.mulesoft.connectors.internal.config.TextGenerationConfig;
import com.mulesoft.connectors.internal.connection.ChatCompletionBase;
import com.mulesoft.connectors.internal.exception.InferenceErrorType;
import com.mulesoft.connectors.internal.utils.*;
import org.codehaus.plexus.interpolation.PrefixAwareRecursionInterceptor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
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
public class TextGenerationOperations {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextGenerationOperations.class);
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
    @DisplayName("[Chat] Completions")
    @OutputJsonType(schema = "api/response/Response.json")
    @Summary("Native chat completion operation")
    public Result<InputStream, LLMResponseAttributes> chatCompletion(
            @Config TextGenerationConfig configuration, @Connection ChatCompletionBase connection,
            @Content InputStream messages) throws ModuleException {
        try {

            JSONArray messagesArray = PayloadUtils.parseInputStreamToJsonArray(messages);

            URL chatCompUrl = ConnectionUtils.getConnectionURLChatCompletion(connection);
            LOGGER.debug("Chatting with {}", chatCompUrl);

            JSONObject payload = PayloadUtils.buildPayload(connection, messagesArray, null);

            String response = ConnectionUtils.executeREST(chatCompUrl, connection, payload.toString());

            LOGGER.debug("Chat completions result {}", response);
            return ResponseUtils.processLLMResponse(response, connection);
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
    @DisplayName("[Chat] Answer Prompt")
    @OutputJsonType(schema = "api/response/Response.json")
    @Summary("Simple chat answer prompt")
    public Result<InputStream, LLMResponseAttributes> chatAnswerPrompt(
            @Config TextGenerationConfig configuration, @Connection ChatCompletionBase connection,
            @Content String prompt) throws ModuleException {
        try {        
        	JSONObject payload = PayloadUtils.buildChatAnswerPromptPayload(configuration, connection, prompt);

            URL chatCompUrl = ConnectionUtils.getConnectionURLChatCompletion(connection);
            String response = ConnectionUtils.executeREST(chatCompUrl, connection, payload.toString());

            LOGGER.debug("Chat answer prompt result {}", response);


            return ResponseUtils.processLLMResponse(response, connection);
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
    @DisplayName("[Agent] Define Prompt Template")
    @OutputJsonType(schema = "api/response/Response.json")
    @Summary("Define a prompt template with instructions, and data ")
    public Result<InputStream, LLMResponseAttributes> promptTemplate(
            @Config TextGenerationConfig configuration, @Connection ChatCompletionBase connection,
            @Content String template,
            @Content String instructions,
            @Content(primary = true) String data) throws ModuleException {
        try {
        	        	
        	JSONObject payload = PayloadUtils.buildPromptTemplatePayload(configuration, connection, template, instructions, data);

            URL chatCompUrl = ConnectionUtils.getConnectionURLChatCompletion(connection);
            String response = ConnectionUtils.executeREST(chatCompUrl, connection, payload.toString());

            LOGGER.debug("Agent define prompt template result {}", response);
            return ResponseUtils.processLLMResponse(response, connection);
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
    @DisplayName("[Tools] Native Template (Reasoning only)")
    @OutputJsonType(schema = "api/response/Response.json")
    @Summary("Define a prompt template with instructions, data and tools")
    public Result<InputStream, LLMResponseAttributes> toolsTemplate(
            @Config TextGenerationConfig configuration, @Connection ChatCompletionBase connection,
            @Content String template,
            @Content String instructions,
            @Content(primary = true) String data,
            @Content @Summary("JSON Array defining the tools set to be used in the template so that the LLM can use them if required") InputStream tools) throws ModuleException {
        
    	try {
        	        	
        	JSONObject payload = PayloadUtils.buildToolsTemplatePayload(configuration, connection, template, instructions, data, tools);
        	
            URL chatCompUrl = ConnectionUtils.getConnectionURLChatCompletion(connection);
            String response = ConnectionUtils.executeREST(chatCompUrl, connection, payload.toString());

            LOGGER.debug("Tools use native template result {}", response);
            return ResponseUtils.processToolsResponse(response, connection);
        } catch (Exception e) {
            LOGGER.error("Error in tools use native template: {}", e.getMessage(), e);
            throw new ModuleException(String.format(ERROR_MSG_FORMAT, "Tools use native template"),
                    InferenceErrorType.CHAT_COMPLETION, e);
        }
    }

}