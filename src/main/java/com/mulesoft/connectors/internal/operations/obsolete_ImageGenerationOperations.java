package com.mulesoft.connectors.internal.operations;

import com.mulesoft.connectors.internal.api.metadata.LLMResponseAttributes;
import com.mulesoft.connectors.internal.config.obsolete_ImageGenerationConfiguration;
import com.mulesoft.connectors.internal.config.obsolete_InferenceConfiguration;
import com.mulesoft.connectors.internal.exception.InferenceErrorType;
import com.mulesoft.connectors.internal.utils.obsolete_ConnectionUtils;
import com.mulesoft.connectors.internal.utils.obsolete_PayloadUtils;
import com.mulesoft.connectors.internal.utils.obsolete_ProviderUtils;
import com.mulesoft.connectors.internal.utils.obsolete_ResponseUtils;
import org.json.JSONObject;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;

import static com.mulesoft.connectors.internal.utils.obsolete_PayloadUtils.createRequestImageGeneration;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

/**
 * This class contains operations for the inference connector.
 * Each public method represents an extension operation.
 */
public class obsolete_ImageGenerationOperations {
    private static final Logger LOGGER = LoggerFactory.getLogger(obsolete_ImageGenerationOperations.class);
    private static final String ERROR_MSG_FORMAT = "%s result error";

    /**
     * Chat completions by messages array including system, users messages i.e. conversation history
     * @param configuration the connector configuration
     * @param prompt the users prompt
     * @return result containing the LLM response
     * @throws ModuleException if an error occurs during the operation
     */
    @MediaType(value = APPLICATION_JSON, strict = false)
    @Alias("Generate-image")
    @DisplayName("[Image] Generate (only Base64)")
    @OutputJsonType(schema = "api/response/Response.json")
    public Result<InputStream, LLMResponseAttributes> generateImage(
            @Config obsolete_ImageGenerationConfiguration configuration,
            @Content String prompt) throws ModuleException {
        try {

            JSONObject requestJson = createRequestImageGeneration(configuration.getInferenceType(), prompt);
            String response;
            obsolete_InferenceConfiguration inferenceConfig = obsolete_ProviderUtils.convertToInferenceConfig(configuration);

            URL imageGenerationUrl = obsolete_ConnectionUtils.getConnectionURLImageGeneration(inferenceConfig);
            LOGGER.debug("Generate Image with {}", imageGenerationUrl);

            JSONObject payload = obsolete_PayloadUtils.buildPayloadImageGeneration(inferenceConfig, requestJson);

            if ((obsolete_ProviderUtils.isHuggingFace((inferenceConfig)))) {
                response = obsolete_ConnectionUtils.executeRESTHuggingFaceImage(imageGenerationUrl, inferenceConfig, payload.toString());
            } else {
                response = obsolete_ConnectionUtils.executeREST(imageGenerationUrl, inferenceConfig, payload.toString());
            }

            LOGGER.debug("Generate Image result {}", response);
            return obsolete_ResponseUtils.processImageGenResponse(response, inferenceConfig);
        } catch (Exception e) {
            LOGGER.error("Error in Generate Image: {}", e.getMessage(), e);
            throw new ModuleException(String.format(ERROR_MSG_FORMAT, "Generate Image"),
                    InferenceErrorType.IMAGE_GENERATION, e);
        }
    }
}