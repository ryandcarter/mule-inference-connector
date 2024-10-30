package com.mulesoft.connectors.internal.operations;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;
import static com.mulesoft.connectors.internal.helpers.ResponseHelper.createLLMResponse;
import com.mulesoft.connectors.internal.constants.PortkeyConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.json.JSONObject;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mulesoft.connectors.internal.api.metadata.LLMResponseAttributes;
import com.mulesoft.connectors.internal.api.metadata.TokenUsage;
import com.mulesoft.connectors.internal.config.PortkeyAIGatewayConfiguration;

import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;

import org.mule.runtime.extension.api.annotation.param.Config;
import static org.apache.commons.io.IOUtils.toInputStream;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class PortkeyAIGatewayOperations {
  private static final Logger LOGGER = LoggerFactory.getLogger(PortkeyAIGatewayOperations.class);

  
  /**
   * Use OpenAI Moderation models to moderate the input (any, from user or llm)
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("Chat-completions")
  @OutputJsonType(schema = "api/response/Response.json")
  public org.mule.runtime.extension.api.runtime.operation.Result<InputStream, LLMResponseAttributes> moderateInput(
                            @Config PortkeyAIGatewayConfiguration configuration,
                            String input) {
    try {
      JSONObject payload = new JSONObject();
      payload.put("model", "");
      payload.put("input", input);
      String apiKey = configuration.getApiKey();
      String virtualApiKey = configuration.getVirtualKey();

      String response = executeREST(apiKey, virtualApiKey, payload.toString());
      JSONObject jsonObject = new JSONObject();
      jsonObject.put(PortkeyConstants.RESPONSE, response);

      LOGGER.debug("Toxicity detection result {}", response);
      Map<String, String> responseAttributes;

      TokenUsage tokenUsage = null;
      return createLLMResponse(jsonObject.toString(), tokenUsage, responseAttributes);
    } catch (Exception e) {
      //throw new ModuleException("Unable to perform toxicity detection", MuleChainErrorType.AI_SERVICES_FAILURE, e);
      return null;

    }
  }

  private static HttpURLConnection getConnectionObject(URL url, String apiKey, String virtualApiKey) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setDoOutput(true);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("x-portkey-api-key", apiKey);
    conn.setRequestProperty("x-portkey-virtual-key", virtualApiKey);
    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
    return conn;
  }

  private static String executeREST(String apiKey, String virtualApiKey, String payload) {

    try {
      URL url = new URL(PortkeyConstants.BASE_URL + PortkeyConstants.CHAT_COMPLETIONS);
      HttpURLConnection conn = getConnectionObject(url, apiKey, virtualApiKey);

      try (OutputStream os = conn.getOutputStream()) {
        byte[] input = payload.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
      }

      int responseCode = conn.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        try (BufferedReader br = new BufferedReader(
                                                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
          StringBuilder response = new StringBuilder();
          String responseLine;
          while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
          }
          return response.toString();
        }
      } else {
        return "Error: " + responseCode;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Exception occurred: " + e.getMessage();
    }

  }

}
