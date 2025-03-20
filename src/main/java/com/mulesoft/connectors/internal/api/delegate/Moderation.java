package com.mulesoft.connectors.internal.api.delegate;

import com.mulesoft.connectors.internal.config.InferenceConfiguration;
import com.mulesoft.connectors.internal.exception.InferenceErrorType;
import com.mulesoft.connectors.internal.utils.ConnectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Moderation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Moderation.class);
    protected final InferenceConfiguration configuration;

    private static Moderation instance; 

    /**
     * Constructor for the Moderation class.
     * @param configuration the configuration for the moderation.
     */
    protected Moderation(InferenceConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Get the instance of the Moderation class.
     * @param configuration the configuration for the moderation.
     * @return the instance of the Moderation class.
     */
    public static Moderation getInstance(InferenceConfiguration configuration) {
        if (instance == null) {
            instance = findInstance(configuration);
        }
        if (instance == null) {
            throw new IllegalArgumentException("No moderation provider found for the given configuration");
        }

        return instance;
    }

    private static Moderation findInstance(InferenceConfiguration configuration) {
        if ("OPENAI".equals(configuration.getInferenceType())) {
            return new OpenAIModeration(configuration);
        }
        return null;
    }   


    /**
     * Build the payload to be sent to the moderation API. 
     * @param text
     * @param images
     * @return
     */
    public String getRequestPayload(InputStream text, InputStream images) {
        
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

    public String getResponsePayload(String requestPayload) {
        try 
        {
            URL moderationURL = new URL(this.getAPIUrl());
            HttpURLConnection conn = this.getConnectionObject(moderationURL);
            String response = ConnectionUtils.executeREST(conn, configuration, requestPayload);
            LOGGER.debug("Moderation service - response from LLM: {}", response);
            return response;
        } catch (Exception e) {
            LOGGER.error("Error in tools use native template: {}", e.getMessage(), e);
            throw new ModuleException("MODERATION ERROR", InferenceErrorType.TEXT_MODERATION, e);
        }
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

    
    /* PRIVATE METHODS */

    public static boolean isValidJson(String input) {
        try {
            new JSONObject(input); // Try parsing as JSON object
            return true;
        } catch (Exception e) {
            return isJsonArray(input);
        }
    }

    public static boolean isJsonArray(String input) {
        try {
            new JSONArray(input); // Try parsing as JSON array
            return true;
        } catch (Exception ex) {
            return false; // It's not valid JSON
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
}
