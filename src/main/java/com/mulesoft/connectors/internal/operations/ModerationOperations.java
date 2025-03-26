package com.mulesoft.connectors.internal.operations;

import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mulesoft.connectors.internal.api.delegate.Moderation;
import com.mulesoft.connectors.internal.api.metadata.LLMResponseAttributes;
import com.mulesoft.connectors.internal.config.ModerationConfiguration;
import com.mulesoft.connectors.internal.exception.InferenceErrorType;

import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import java.io.InputStream;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;

public class ModerationOperations {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModerationOperations.class);
    private static final String ERROR_MSG_FORMAT = "%s moderation error";
    
     /**
     * The moderation oepration allows users to check whether text or images are potentially harmful
     * @param configuration
     * @param systemContent
     * @param userContent
     * @return
     */
    @MediaType(value = APPLICATION_JSON, strict = false)
    @Alias("Toxicity-Detection-Text")
    @DisplayName("[Toxicity] Detection by Text")
    @OutputJsonType(schema = "api/response/ResponseModeration.json")
    @Summary("Detects toxic input by text and classifies it into categories.")
    public Result<InputStream, LLMResponseAttributes> textModeration(
            @Config ModerationConfiguration configuration,
            @Content(primary = true) @Summary("Text to moderate. Can be a single string or an array of strings") InputStream text) throws ModuleException {
        try {
            return  Moderation.getInstance(configuration).moderate(text);
            
        } catch (Exception e) {
            LOGGER.error("Error in moderation: {}", e.getMessage(), e); 
            throw new ModuleException(String.format(ERROR_MSG_FORMAT, "Moderation"),
                    InferenceErrorType.CHAT_COMPLETION, e);
        }
    }
}
