package com.mulesoft.connectors.internal.helpers;

import org.json.JSONObject;

import com.mulesoft.connectors.internal.api.metadata.TokenUsage;

public class TokenHelper {

    public static TokenUsage parseUsageFromResponse(String jsonResponse) throws Exception {
        JSONObject root = new JSONObject(jsonResponse);

        int promptTokens;
        int completionTokens;
        int totalTokens;


        if (root.has("usage")) {
            JSONObject usageNode = root.getJSONObject("usage");
            
            if (usageNode.has("prompt_tokens") && usageNode.has("completion_tokens")) {
                promptTokens = usageNode.getInt("prompt_tokens");
                completionTokens = usageNode.getInt("completion_tokens");
                totalTokens = usageNode.getInt("total_tokens");
            } 
            else if (root.has("prompt_eval_count") && root.has("eval_count")) {
                promptTokens = root.getInt("prompt_eval_count");
                completionTokens = root.getInt("eval_count");
                totalTokens = promptTokens + completionTokens;
            } else {
                promptTokens = 0;
                completionTokens = 0;
                totalTokens = 0;
            }
        } else {
            promptTokens = 0;
            completionTokens = 0;
            totalTokens = 0;
        }

        // Create and populate the Usage object
        TokenUsage usage = new TokenUsage(promptTokens, completionTokens, totalTokens);

        return usage;
    }
}
