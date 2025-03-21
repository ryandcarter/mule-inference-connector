package com.mulesoft.connectors.internal.api.delegate;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * This method should return a list like this:
     * [
     *  {
     *      "harassment": 0.99,
     *      "self_harm": 0.98,
     *      "sexual": 0.97,
     *      "violence": 0.96
     *  } 
     * given the following JSON response:
     * {
     *  "results": [
     *      {
     *          "categories": {
     *              "harassment": true,
     *              "self_harm": true,
     *              "sexual": true,
     *              "violence": true
     *          },
     *          "category_scores": {
     *              "harassment": 0.99,
     *              "self_harm": 0.98,
     *              "sexual": 0.97,
     *              "violence": 0.96
     *          }
     *      }
     * ]
     * ]
     */
    @Override
    protected List<Map<String, Double>> getCategories(JSONObject llmResponseObject) {
        JSONArray results = llmResponseObject.getJSONArray("results");
        List<Map<String, Double>> returnValueList = new ArrayList<>();
        for (Object result : results) {
            Map<String, Double> categoriesMap = new HashMap<>();
            JSONObject resultObject = (JSONObject) result;
            JSONObject categoriesObject = resultObject.getJSONObject("categories");
            JSONObject categoryScoresObject = resultObject.getJSONObject("category_scores");
            //For each field in categoriesObject, add the field name and the value in categoryScoresObject
            for (String key : categoriesObject.keySet()) {
                categoriesMap.put(key, categoryScoresObject.getDouble(key));
            }
            returnValueList.add(categoriesMap);
        }
        return returnValueList;
    }
}
