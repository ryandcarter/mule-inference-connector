package com.mulesoft.connectors.internal.api.delegate;

import com.mulesoft.connectors.internal.config.ModerationConfig;
import com.mulesoft.connectors.internal.connection.types.ModerationBase;
import com.mulesoft.connectors.internal.constants.InferenceConstants;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenAIModeration extends Moderation {
    protected OpenAIModeration(ModerationConfig configuration, ModerationBase connection) {
        super(configuration, connection);
    }

    @Override
    public String getAPIUrl() {
        return InferenceConstants.OPEN_AI_URL + InferenceConstants.MODERATIONS_PATH;
    }

    @Override
    protected JSONObject handleModelSpecificRequestPayload(JSONObject payload, Object text, Object images) {
        payload.put("model", connection.getModelName());
        return payload;
    }

    @Override
    protected String getTextInputAttributeName() {
        return "input";
    }

    @Override
    protected boolean isFlagged(JSONObject llmResponseObject) {
        JSONArray results = llmResponseObject.getJSONArray("results");
        boolean isFlagged = false;
        for (Object result : results) {
            JSONObject resultObject = (JSONObject) result;
            isFlagged = resultObject.getBoolean("flagged") || isFlagged;
        }
        return isFlagged;
    }
}