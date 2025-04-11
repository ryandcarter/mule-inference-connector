package com.mulesoft.connectors.internal.operations;

import com.mulesoft.connectors.internal.api.metadata.LLMResponseAttributes;
import com.mulesoft.connectors.internal.config.ImageGenerationConfiguration;
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
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;

import static com.mulesoft.connectors.internal.utils.PayloadUtils.createRequestImageGeneration;
import static com.mulesoft.connectors.internal.utils.PayloadUtils.createRequestImageURL;
import static com.mulesoft.connectors.internal.utils.ResponseUtils.encodeImageToBase64;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

/**
 * This class contains operations for the inference connector.
 * Each public method represents an extension operation.
 */
public class ImageGenerationOperations {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageGenerationOperations.class);
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
            @Config ImageGenerationConfiguration configuration,
            @Content String prompt) throws ModuleException {
        try {

            JSONObject requestJson = createRequestImageGeneration(configuration.getInferenceType(), prompt);
            String response;
            InferenceConfiguration inferenceConfig = ProviderUtils.convertToInferenceConfig(configuration);

            URL imageGenerationUrl = ConnectionUtils.getConnectionURLImageGeneration(inferenceConfig);
            LOGGER.debug("Generate Image with {}", imageGenerationUrl);

            JSONObject payload = PayloadUtils.buildPayloadImageGeneration(inferenceConfig, requestJson);

            if ((ProviderUtils.isHuggingFace((inferenceConfig)))) {
                response = ConnectionUtils.executeRESTHuggingFaceImage(imageGenerationUrl, inferenceConfig, payload.toString());
            } else {
                response = ConnectionUtils.executeREST(imageGenerationUrl, inferenceConfig, payload.toString());
            }

            LOGGER.debug("Generate Image result {}", response);
            return ResponseUtils.processImageGenResponse(response, inferenceConfig);
        } catch (Exception e) {
            LOGGER.error("Error in Generate Image: {}", e.getMessage(), e);
            throw new ModuleException(String.format(ERROR_MSG_FORMAT, "Generate Image"),
                    InferenceErrorType.IMAGE_GENERATION, e);
        }
    }
}