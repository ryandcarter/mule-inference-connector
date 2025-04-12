package com.mulesoft.connectors.internal.utils;

import com.mulesoft.connectors.internal.config.obsolete_InferenceConfiguration;
import com.mulesoft.connectors.internal.constants.InferenceConstants;
import com.mulesoft.connectors.internal.operations.obsolete_InferenceOperations;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mulesoft.connectors.internal.utils.obsolete_ResponseUtils.encodeImageToBase64;

/**
 * Utility class for HTTP connection operations.
 */
public class obsolete_ConnectionUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(obsolete_InferenceOperations.class);

    //private static final java.util.logging.Logger LOGGER = LoggerFactory.getLogger(ConnectionUtils.class);

    /**
     * Build the HTTP connection for the API request
     * @param url the URL to connect to
     * @param configuration the connector configuration
     * @return the configured HTTP connection
     * @throws IOException if an error occurs during connection setup
     */
    public static HttpURLConnection getConnectionObject(URL url, obsolete_InferenceConfiguration configuration) throws IOException {
    	
        HttpURLConnection conn;

        if ("VERTEX_AI_EXPRESS".equalsIgnoreCase(configuration.getInferenceType())) {
        	Map<String, String> queryParams = new HashMap<>();
        	queryParams.put("key", configuration.getApiKey());
        	
        	// Append query parameters to the base URL
            String fullUrl = url.toString() + "?" + getQueryParams(queryParams);
         
            // Open connection with the modified URL
            conn = (HttpURLConnection) new URL(fullUrl).openConnection();
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }

        LOGGER.debug("path : ", conn.getURL().getPath());
        
        //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept", "application/json");

        // Set appropriate timeouts
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(configuration.getTimeout() != null ? Integer.parseInt(configuration.getTimeout()) : 600000);

        switch (configuration.getInferenceType()) {
            case "ANTHROPIC":
                conn.setRequestProperty("x-api-key", configuration.getApiKey());
                conn.setRequestProperty("anthropic-version", "2023-06-01");
                break;
            case "PORTKEY":
                conn.setRequestProperty("x-portkey-api-key", configuration.getApiKey());
                conn.setRequestProperty("x-portkey-virtual-key", configuration.getVirtualKey());
                break;
            case "AZURE_OPENAI":
                conn.setRequestProperty("api-key", configuration.getApiKey());
                break;
            case "VERTEX_AI_EXPRESS":
                //do nothing for Vertex AI
                break;
            case "AZURE_AI_FOUNDRY":
                conn.setRequestProperty("api-key", configuration.getApiKey());
                break;
            default:
                conn.setRequestProperty("Authorization", "Bearer " + configuration.getApiKey());
                break;
        }

        return conn;
    }

    /**
     * Get the appropriate URL for chat completion based on the configuration
     * @param configuration the connector configuration
     * @return the URL for the chat completion endpoint
     * @throws MalformedURLException if the URL is invalid
     */
    public static URL getConnectionURLChatCompletion(obsolete_InferenceConfiguration configuration) throws MalformedURLException {
        switch (configuration.getInferenceType()) {
            case "PORTKEY":
                return new URL(InferenceConstants.PORTKEY_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "GROQ":
                return new URL(InferenceConstants.GROQ_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "HUGGING_FACE":
                return new URL(InferenceConstants.HUGGINGFACE_URL + "/models/" + configuration.getModelName() + "/v1" + InferenceConstants.CHAT_COMPLETIONS);
            case "OPENROUTER":
                return new URL(InferenceConstants.OPENROUTER_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "GITHUB":
                return new URL(InferenceConstants.GITHUB_MODELS_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "OLLAMA":
                return new URL(configuration.getOllamaUrl() + InferenceConstants.CHAT_COMPLETIONS_OLLAMA);
            case "XINFERENCE":
                return new URL(configuration.getxinferenceUrl() + InferenceConstants.CHAT_COMPLETIONS);
            case "CEREBRAS":
                return new URL(InferenceConstants.CEREBRAS_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "NVIDIA":
                return new URL(InferenceConstants.NVIDIA_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "FIREWORKS":
                return new URL(InferenceConstants.FIREWORKS_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "TOGETHER":
                return new URL(InferenceConstants.TOGETHER_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "DEEPINFRA":
                return new URL(InferenceConstants.DEEPINFRA_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "PERPLEXITY":
                return new URL(InferenceConstants.PERPLEXITY_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "XAI":
                return new URL(InferenceConstants.X_AI_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "OPENAI":
                return new URL(InferenceConstants.OPEN_AI_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "MISTRAL":
                return new URL(InferenceConstants.MISTRAL_AI_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "ANTHROPIC":
                return new URL(InferenceConstants.ANTHROPIC_URL + "/" + InferenceConstants.MESSAGES);
            case "AI21LABS":
                return new URL(InferenceConstants.AI21LABS_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "COHERE":
                return new URL(InferenceConstants.COHERE_URL + InferenceConstants.CHAT_COMPLETIONS_OLLAMA);
            case "AZURE_OPENAI":
                String urlStr = InferenceConstants.AZURE_OPENAI_URL + InferenceConstants.CHAT_COMPLETIONS_AZURE;
                urlStr = urlStr
                    .replace("{resource-name}", configuration.getAzureOpenaiResourceName())
                    .replace("{deployment-id}", configuration.getAzureOpenaiDeploymentId());
                return new URL(urlStr);
            case "VERTEX_AI_EXPRESS":
                String vertexAIUrlStr = InferenceConstants.VERTEX_AI_EXPRESS_URL + InferenceConstants.GENERATE_CONTENT_VERTEX_AI;
                vertexAIUrlStr = vertexAIUrlStr
                    .replace("{MODEL_ID}", configuration.getModelName());
                return new URL(vertexAIUrlStr);
            case "AZURE_AI_FOUNDRY":
                String aifurlStr = InferenceConstants.AZURE_AI_FOUNDRY_URL + InferenceConstants.CHAT_COMPLETIONS_AZURE_AI_FOUNDRY;
                aifurlStr = aifurlStr
                    .replace("{resource-name}", configuration.getAzureAIFoundryResourceName())
                    .replace("{api-version}", configuration.getAzureAIFoundryApiVersion());
                return new URL(aifurlStr);
            case "GPT4ALL":
                return new URL(configuration.getGpt4All() + InferenceConstants.CHAT_COMPLETIONS);
            case "LMSTUDIO":
                return new URL(configuration.getLmStudio() + InferenceConstants.CHAT_COMPLETIONS);
            case "DOCKER_MODELS":
                return new URL(configuration.getDockerModelUrl() + "/engines/llama.cpp/v1" + InferenceConstants.CHAT_COMPLETIONS);
            case "DEEPSEEK":
                return new URL(InferenceConstants.DEEPSEEK_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "ZHIPU_AI":
                return new URL(InferenceConstants.ZHIPU_AI_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "OPENAI_COMPATIBLE_ENDPOINT":
                return new URL(configuration.getOpenAICompatibleURL() + InferenceConstants.CHAT_COMPLETIONS);
            default:
                throw new MalformedURLException("Unsupported inference type: " + configuration.getInferenceType());
        }
    }



    /**
     * Get the appropriate URL for chat completion based on the configuration
     * @param configuration the connector configuration
     * @return the URL for the chat completion endpoint
     * @throws MalformedURLException if the URL is invalid
     */
    public static URL getConnectionURLImageGeneration(obsolete_InferenceConfiguration configuration) throws MalformedURLException {
        switch (configuration.getInferenceType()) {
            case "OPENAI":
                return new URL(InferenceConstants.OPEN_AI_URL + InferenceConstants.OPENAI_GENERATE_IMAGES);
            case "HUGGING_FACE":
                return new URL(InferenceConstants.HUGGINGFACE_URL + "/models/" + configuration.getModelName());
            default:
                throw new MalformedURLException("Unsupported inference type: " + configuration.getInferenceType());
        }
    }
    /**
     * Execute a REST API call
     * @param resourceUrl the URL to call
     * @param configuration the connector configuration
     * @param payload the payload to send
     * @return the response string
     * @throws IOException if an error occurs during the API call
     */
    public static String executeREST(URL resourceUrl, obsolete_InferenceConfiguration configuration, String payload) throws IOException {
        HttpURLConnection conn = getConnectionObject(resourceUrl, configuration);

        return executeREST(conn, payload);
    }

        /**
     * Execute a REST API call
     * @param conn the URL to call
     * @param payload the payload to send
     * @return the response string
     * @throws IOException if an error occurs during the API call
     */
    public static String executeREST(HttpURLConnection conn, String payload) throws IOException {

        // Send the payload
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read successful response
            return readResponseStream(conn.getInputStream());
        } else {
            // Read error response
            String errorResponse = readResponseStream(conn.getErrorStream());

            LOGGER.error("API request failed with status code: {} and message: {}", responseCode, errorResponse);
            throw new IOException("API request failed with status code: " + responseCode +
                    " and message: " + errorResponse);
        }
    }


    public static String executeRESTHuggingFaceImage(URL resourceUrl, obsolete_InferenceConfiguration configuration, String payload) throws IOException {
        HttpURLConnection conn = getConnectionObject(resourceUrl, configuration);

        // Send the payload
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        int responseCode = conn.getResponseCode();

        JSONObject responseWrapper = new JSONObject();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String contentType = conn.getContentType();

            if (contentType != null && contentType.startsWith("image/")) {
                byte[] responseBytes = readResponseBytes(conn.getInputStream());

                String base64Image = encodeImageToBase64(responseBytes);

                JSONObject base64Object = new JSONObject();
                base64Object.put("b64_json", base64Image);

                JSONObject revisedPrompt = new JSONObject(payload);
                revisedPrompt.put("revised_prompt", revisedPrompt.getString("inputs"));

                JSONArray dataArray = new JSONArray();
                dataArray.put(base64Object);
                dataArray.put(revisedPrompt);

                responseWrapper.put("data", dataArray);

            }

            return responseWrapper.toString();

        } else {
            String errorResponse = readResponseStream(conn.getErrorStream());
            LOGGER.error("API request failed with status code: {} and message: {}", responseCode, errorResponse);
            throw new IOException("API request failed with status code: " + responseCode +
                    " and message: " + errorResponse);
        }
    }


    /**
     * Read data from an input stream into a string
     * @param stream the input stream to read from
     * @return the stream contents as a string
     * @throws IOException if an error occurs during reading
     */
    public static String readResponseStream(InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine);
            }
            return response.toString();
        }
    }
    
    /**
     * Utility method to encode query parameters
     */
    public static String getQueryParams(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (query.length() > 0) {
                query.append("&");
            }
            query.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                 .append("=")
                 .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return query.toString();
    }

    public static byte[] readResponseBytes(InputStream inputStream) throws IOException {

        if (inputStream == null) return new byte[0];

        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] data = new byte[4096];
            int nRead;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }
} 