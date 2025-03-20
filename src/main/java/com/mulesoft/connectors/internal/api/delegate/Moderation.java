package com.mulesoft.connectors.internal.api.delegate;

import com.mulesoft.connectors.internal.config.InferenceConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
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

    protected abstract JSONObject handleModelSpecificRequestPayload(JSONObject payload, Object text, Object images);
    
    /**
     * Return the name of the attribute used in the API Payload to specify the text that needs to be moderated. For example, "input" for OpenAI or "text" for Azure Content Safety
     * @return
     */
    protected abstract String getTextInputAttributeName();

    protected abstract String getAPIUrl();


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
}
