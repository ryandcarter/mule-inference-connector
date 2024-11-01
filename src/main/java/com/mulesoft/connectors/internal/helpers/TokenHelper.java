package com.mulesoft.connectors.internal.helpers;

import org.json.JSONObject;

import com.mulesoft.connectors.internal.api.metadata.TokenUsage;

public class TokenHelper {

    public static TokenUsage parseUsageFromResponse(String jsonResponse) throws Exception {
        JSONObject root = new JSONObject(jsonResponse);
        JSONObject usageNode = root.getJSONObject("usage");

        // Extract main token counts
        int promptTokens = usageNode.getInt("prompt_tokens");
        int completionTokens = usageNode.getInt("completion_tokens");
        int totalTokens = usageNode.getInt("total_tokens");


        // Create and populate the Usage object
        TokenUsage usage = new TokenUsage(promptTokens, completionTokens, totalTokens);

        return usage;
    }
}
