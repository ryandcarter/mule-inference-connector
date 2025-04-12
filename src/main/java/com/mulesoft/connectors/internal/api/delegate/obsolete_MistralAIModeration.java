package com.mulesoft.connectors.internal.api.delegate;

import java.net.HttpURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mulesoft.connectors.internal.config.ModerationConfiguration;
import com.mulesoft.connectors.internal.constants.InferenceConstants;

public class obsolete_MistralAIModeration extends obsolete_Moderation {

    protected obsolete_MistralAIModeration(ModerationConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String getAPIUrl() {
        return InferenceConstants.MISTRAL_AI_URL + InferenceConstants.MODERATIONS_PATH;
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
        conn.addRequestProperty("Authorization", "Bearer " + configuration.getApiKey());
    }

    @Override
    protected boolean isFlagged(JSONObject response) {
        boolean isFlagged = false;
        //Scan all the categories for all the results to see if there's at least one that is true.
        JSONArray results = response.getJSONArray("results");
        for (Object result : results) {
            JSONObject resultObject = (JSONObject) result;
            JSONObject categories = resultObject.getJSONObject("categories");
            for (String key : categories.keySet()) {
                if (categories.getBoolean(key)) {
                    isFlagged = true;
                    break;
                }
            }
        }

        return isFlagged;
    }



}
