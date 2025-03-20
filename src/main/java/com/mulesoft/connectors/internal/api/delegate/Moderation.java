package com.mulesoft.connectors.internal.api.delegate;

import com.mulesoft.connectors.internal.api.metadata.LLMResponseAttributes;
import com.mulesoft.connectors.internal.config.ModerationConfiguration;
import com.mulesoft.connectors.internal.exception.InferenceErrorType;
import com.mulesoft.connectors.internal.utils.ConnectionUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.io.IOUtils.toInputStream;

public abstract class Moderation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Moderation.class);
    protected final ModerationConfiguration configuration;

    private static Moderation instance; 

    /**
     * Constructor for the Moderation class.
     * @param configuration the configuration for the moderation.
     */
    protected Moderation(ModerationConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Get the instance of the Moderation class.
     * @param configuration the configuration for the moderation.
     * @return the instance of the Moderation class.
     */
    public static Moderation getInstance(ModerationConfiguration configuration) {
        if (instance == null) {
            instance = findInstance(configuration);
        }
        if (instance == null) {
            throw new IllegalArgumentException("No moderation provider found for the given configuration");
        }

        return instance;
    }

    private static Moderation findInstance(ModerationConfiguration configuration) {
        if ("OPENAI".equals(configuration.getInferenceType())) {
            return new OpenAIModeration(configuration);
        }
        return null;
    }   




   /**
    * Perform the moderation task according to the concrete implementation of the Moderation class.
    * @param text
    * @return
    */
    public Result<InputStream, LLMResponseAttributes> moderate(InputStream text) {
        String payload = this.getRequestPayload(text, null);
        LOGGER.debug("Moderation payload that will be sent to the LLM {}", payload);
        String response = this.getResponsePayload(payload);
        LOGGER.debug("Moderation service - response from LLM: {}", response);
        return this.processResponse(response);
    }


    /******************** */
    /*  ABSTRACT METHODS  */
    /******************** */

    /**
     * This method is a placeholder for any specific attributes that needs to be added by the model provider.
     * If no specific attributes are needed, the payload should be returned as is.
     * @param payload
     * @param text
     * @param images
     * @return
     */
    protected abstract JSONObject handleModelSpecificRequestPayload(JSONObject payload, Object text, Object images);
    
    /**
     * Return the name of the attribute used in the API Payload to specify the text that needs to be moderated. For example, "input" for OpenAI or "text" for Azure Content Safety
     * @return
     */
    protected abstract String getTextInputAttributeName();

    /**
     * Return the URL of the API to be used for moderation.
     * @return
     */
    public abstract String getAPIUrl();

    /**
     * Add the authentication headers to the HTTP connection.
     * @param conn
     */
    public abstract void addAuthHeaders(HttpURLConnection conn);

    /**
     * Subclasses should interpret the response from the moderation API and return true if the text is flagged as
     * inappropriate. The logic will be strictly dependent on the model provider.
     * @param response
     * @return
     */
    protected abstract boolean isFlagged(JSONObject response);

    /**
     * Subclasses should interpret the response from the moderation API and 
     * return a list of maps of categories and their confidence scores.
     * @param llmResponseObject
     * @return
     */
    protected abstract List<Map<String, Double>> getCategories(JSONObject llmResponseObject);
    
    
    /********************************* */
    /* PRIVATE and PROTECTED METHODS */
    /********************************* */

    
    /**
     * Build the payload to be sent to the moderation API. 
     * @param text
     * @param images
     * @return
     */
    protected String getRequestPayload(InputStream text, InputStream images) {
        
        String inputString = convertStreamToString(text);
        JSONObject payload = new JSONObject();
        if (isJsonArray(inputString)) {
            //We assume an array of strings. 
            StringBuffer payloadText = new StringBuffer();
            JSONArray inputArray = new JSONArray(inputString);
            inputArray.forEach(item -> payloadText.append(item.toString() + " "));
            inputArray.remove(inputArray.length() - 1); //Remove the last space

            payload.put(getTextInputAttributeName(), payloadText.toString());
        } else  {
            payload.put(getTextInputAttributeName(), inputString);
        }

        payload = handleModelSpecificRequestPayload(payload, text, images);
        return payload.toString();

    }

    /**
     * Send the request payload to the moderation API and return the response.
     * @param requestPayload
     * @return
     */
    protected String getResponsePayload(String requestPayload) {
        try 
        {
            URL moderationURL = new URL(this.getAPIUrl());
            HttpURLConnection conn = this.getConnectionObject(moderationURL);
            return ConnectionUtils.executeREST(conn, requestPayload);
        } catch (Exception e) {
            LOGGER.error("Error in tools use native template: {}", e.getMessage(), e);
            throw new ModuleException("MODERATION ERROR", InferenceErrorType.TEXT_MODERATION, e);
        }
    }

    /**
     * Process the response from the moderation API and return the result that will be returned by the connector. 
     * No further processing should be necessary at this point
     * @param llmResponse
     * @return
     * @throws ModuleException
     */
    protected Result<InputStream, LLMResponseAttributes> processResponse(String llmResponse) throws ModuleException {
        try {
            JSONObject responseObject = new JSONObject();
            JSONObject llmResponseObject = new JSONObject(llmResponse);
            responseObject.put("flagged", isFlagged(llmResponseObject));
            
            List<Map<String, Double>> categories = getCategories(llmResponseObject);
            JSONArray categoriesArray = new JSONArray();
            categories.forEach(category -> categoriesArray.put(new JSONObject(category)));
            responseObject.put("categories", categoriesArray);
            
            return Result.<InputStream, LLMResponseAttributes>builder()
            //.attributes(new LLMResponseAttributes(tokenUsage, (HashMap<String, String>) responseAttributes))
                .attributesMediaType(org.mule.runtime.api.metadata.MediaType.APPLICATION_JAVA)
                .output(toInputStream(responseObject.toString(), StandardCharsets.UTF_8))
                .mediaType(org.mule.runtime.api.metadata.MediaType.APPLICATION_JSON)
                .build();
        } catch (Exception e) {
            LOGGER.error("Error processing moderation response: {}", e.getMessage(), e);
            throw new ModuleException("MODERATION ERROR", InferenceErrorType.TEXT_MODERATION, e);
        }
   }

    private HttpURLConnection getConnectionObject(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept", "application/json");

        this.addAuthHeaders(conn);
        return conn;
    }

            
    protected static boolean isJsonArray(String input) {
        try {
            new JSONArray(input); // Try parsing as JSON array
            return true;
        } catch (Exception ex) {
            return false; // It's not valid JSON
        }
    }            

    protected static boolean isValidJson(String input) {
        try {
            new JSONObject(input); // Try parsing as JSON object
            return true;
        } catch (Exception e) {
            return isJsonArray(input);
        }
    }

    private static String convertStreamToString(InputStream inputStream) {
        
        if (inputStream == null) {
            return "";
        }
    
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(reader)) {
    
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
    
            return stringBuilder.toString().trim();

        } catch (IOException e) {
            LOGGER.error("Error converting stream to string. Returning empty string", e);
            return "";
        }
    } 


 
}
