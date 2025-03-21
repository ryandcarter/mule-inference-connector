package com.mulesoft.connectors.internal.api.delegate;

import java.net.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mulesoft.connectors.internal.config.ModerationConfiguration;
import com.mulesoft.connectors.internal.constants.InferenceConstants;

public class OpenAIModeration extends Moderation {

    protected OpenAIModeration(ModerationConfiguration configuration) {
        super(configuration);
    }
    
    public String getAPIUrl() {
        return InferenceConstants.OPEN_AI_URL + InferenceConstants.MODERATIONS_PATH;
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

    @Override
    protected boolean isFlagged(JSONObject llmResponseObject) {
        
        JSONArray results = llmResponseObject.getJSONArray("results");
        Boolean isFlagged = false;
        for (Object result : results) {
            JSONObject resultObject = (JSONObject) result;
            isFlagged = resultObject.getBoolean("flagged") || isFlagged;
        }
        return isFlagged;
    }

    
}
