package com.mulesoft.connectors.internal.api.delegate;

import java.io.InputStream;
import java.net.HttpURLConnection;

import org.json.JSONObject;

import com.mulesoft.connectors.internal.config.InferenceConfiguration;

public class OpenAIModeration extends Moderation {

    public OpenAIModeration(InferenceConfiguration configuration) {
        super(configuration);
    }
    
    public String getAPIUrl() {
        return "https://api.openai.com/v1/moderations";
    }

    @Override
    protected JSONObject handleModelSpecificRequestPayload(JSONObject payload, Object text, Object images) {
        payload.put("model", super.configuration.getModerationModelName());
        return payload;
    }

    @Override
    protected String getTextInputAttributeName() {
        return "input";
    }

    @Override
    public void addAuthHeaders(HttpURLConnection conn) {
        conn.setRequestProperty("Authorization", "Bearer " + configuration.getApiKey());
    }
}
