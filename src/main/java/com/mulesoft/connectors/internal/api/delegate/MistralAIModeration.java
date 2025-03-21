package com.mulesoft.connectors.internal.api.delegate;

import static com.mulesoft.connectors.internal.api.delegate.Moderation.convertStreamToString;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import com.mulesoft.connectors.internal.config.ModerationConfiguration;
import com.mulesoft.connectors.internal.constants.InferenceConstants;

public class MistralAIModeration extends Moderation {

    protected MistralAIModeration(ModerationConfiguration configuration) {
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
