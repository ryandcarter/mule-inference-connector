package com.mulesoft.connectors.internal.operations;

import static com.mulesoft.connectors.internal.helpers.ResponseHelper.createLLMResponse;
import com.mulesoft.connectors.internal.constants.InferenceConstants;
import com.mulesoft.connectors.internal.helpers.TokenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mulesoft.connectors.internal.api.metadata.LLMResponseAttributes;
import com.mulesoft.connectors.internal.api.metadata.TokenUsage;
import com.mulesoft.connectors.internal.config.InferenceConfiguration;

import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;

import org.mule.runtime.extension.api.annotation.param.Config;
import static org.apache.commons.io.IOUtils.toInputStream;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class InferenceOperations {
  private static final Logger LOGGER = LoggerFactory.getLogger(InferenceOperations.class);

  
  /**
   * Use OpenAI Moderation models to moderate the input (any, from user or llm)
      * @throws Exception 
         */
        @MediaType(value = APPLICATION_JSON, strict = false)
        @Alias("Chat-completions")
        @OutputJsonType(schema = "api/response/Response.json")
        public org.mule.runtime.extension.api.runtime.operation.Result<InputStream, LLMResponseAttributes> moderateInput(
                                  @Config InferenceConfiguration configuration,
                                  InputStream input) throws Exception {
   try {
      InputStreamReader reader = new InputStreamReader(input);
      StringBuilder inputStringBuilder = new StringBuilder();
      int c;
      while ((c = reader.read()) != -1) {
          inputStringBuilder.append((char) c);
      }
      String inputString = inputStringBuilder.toString();
      JSONArray messagesArray = new JSONArray(inputString);

      JSONObject payload = new JSONObject();
      payload.put(InferenceConstants.MODEL, "gpt-4o");
      payload.put(InferenceConstants.MESSAGES, messagesArray);

      String apiKey = configuration.getApiKey();
      String virtualApiKey = configuration.getVirtualKey();

      String response = executeREST(apiKey, virtualApiKey, payload.toString());
      JSONObject root = new JSONObject(response);
      String model = root.getString("model");      
      String object = root.getString("object");
      String id = root.getString("id");
      String system_fingerprint = root.getString("system_fingerprint");
      JSONArray choicesArray = root.getJSONArray("choices");
      JSONObject firstChoice = choicesArray.getJSONObject(0);
      String finishReason = firstChoice.getString("finish_reason");
      JSONObject message = firstChoice.getJSONObject("message");
      String content = message.getString("content");

      TokenUsage tokenUsage = TokenHelper.parseUsageFromResponse(response);
      JSONObject jsonObject = new JSONObject();
      jsonObject.put(InferenceConstants.RESPONSE, content);
      Map<String, String> responseAttributes = new HashMap<>();;
      responseAttributes.put(InferenceConstants.FINISH_REASON, finishReason); 
      responseAttributes.put(InferenceConstants.MODEL, model); 
      responseAttributes.put(InferenceConstants.OBJECT_STRING, object); 
      responseAttributes.put(InferenceConstants.ID_STRING, id); 
      responseAttributes.put(InferenceConstants.SYSTEM_FINGERPRINT, system_fingerprint); 

      LOGGER.debug("Chat completions result {}", response);

      return createLLMResponse(jsonObject.toString(), tokenUsage, responseAttributes);
     } catch (Exception e) {
      //throw new ModuleException("Unable to perform toxicity detection", MuleChainErrorType.AI_SERVICES_FAILURE, e);
      System.out.println(e.getMessage());

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
    conn.setRequestProperty("User-Agent", "Mozilla/5.0");  
    conn.setRequestProperty("Accept", "application/json");  
return conn;
  }

  private static String executeREST(String apiKey, String virtualApiKey, String payload) {

    try {
        URL url = new URL(InferenceConstants.BASE_URL + InferenceConstants.CHAT_COMPLETIONS);

        HttpURLConnection conn = getConnectionObject(url, apiKey, virtualApiKey);


        // Send the payload
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();  // Ensure data is sent
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

            try (BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine.trim());
                }
            } catch (IOException ex) {
                LOGGER.debug("Error reading error stream {}", ex.getMessage());

            }
            return "Error: " + responseCode;
        }
    } catch (Exception e) {
        e.printStackTrace();
        return "Exception occurred: " + e.getMessage();
    }
}


}
