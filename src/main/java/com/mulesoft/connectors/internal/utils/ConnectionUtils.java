package com.mulesoft.connectors.internal.utils;

import com.mulesoft.connectors.internal.config.TextGenerationConfig;
import com.mulesoft.connectors.internal.connection.ChatCompletionBase;
import com.mulesoft.connectors.internal.constants.InferenceConstants;
import com.mulesoft.connectors.internal.operations.obsolete_InferenceOperations;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpRequestOptions;
import org.mule.runtime.http.api.domain.entity.ByteArrayHttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.request.HttpRequestBuilder;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import static com.mulesoft.connectors.internal.utils.obsolete_ResponseUtils.encodeImageToBase64;

/**
 * Utility class for HTTP connection operations using Mule's HttpClient.
 */
public class ConnectionUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(obsolete_InferenceOperations.class);
    private static HttpClient httpClient;


    /**
     * Build the HTTP request for the API call.
     * @param url the URL to connect to
     * @param configuration the connector configuration
     * @return the configured HttpRequest
     * @throws IOException if an error occurs during request setup
     */


    public static HttpRequest buildHttpRequest(URL url, TextGenerationConfig configuration, ChatCompletionBase connection) throws IOException {
        HttpRequestBuilder requestBuilder = HttpRequest.builder();
       String finalUri = url.toString();

        if ("VERTEX_AI_EXPRESS".equalsIgnoreCase(connection.getInferenceType())) {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("key", connection.getApiKey());
            finalUri = url.toString() + "?" + getQueryParams(queryParams);
        }


        LOGGER.debug("Request path: {}", finalUri);

        requestBuilder
                .uri(finalUri)
                .method("POST")
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "Mozilla/5.0")
                .addHeader("Accept", "application/json");

        switch (connection.getInferenceType()) {
            case "ANTHROPIC":
                requestBuilder
                        .addHeader("x-api-key", connection.getApiKey())
                        .addHeader("anthropic-version", "2023-06-01");
                break;
            case "PORTKEY":
                requestBuilder
                        .addHeader("x-portkey-api-key", connection.getApiKey())
                        .addHeader("x-portkey-virtual-key", configuration.getVirtualKey());
                break;
            case "AZURE_OPENAI":
                requestBuilder.addHeader("api-key", connection.getApiKey());
                break;
            case "VERTEX_AI_EXPRESS":
                // Query param already added
                break;
            case "AZURE_AI_FOUNDRY":
                requestBuilder.addHeader("api-key", connection.getApiKey());
                break;
            default:
                requestBuilder.addHeader("Authorization", "Bearer " + connection.getApiKey());
                break;
        }

        return requestBuilder.build();
    }

    /**
     * Get request options based on configuration.
     * @param configuration the connector configuration
     * @return the configured HttpRequestOptions
     */
    public static HttpRequestOptions getRequestOptions(TextGenerationConfig configuration, ChatCompletionBase connection) {
        return HttpRequestOptions.builder()
                .responseTimeout(String.valueOf(connection.getTimeout()) != null ? Integer.parseInt(String.valueOf(connection.getTimeout())) : 600000)
                .followsRedirect(true)
                .build();
    }

    /**
     * Get the static appropriate URL for chat completion based on the configuration.
     * @param configuration the connector configuration
     * @return the URL for the chat completion endpoint
     * @throws MalformedURLException if the URL is invalid
     */
    public static URL getConnectionURLChatCompletion(TextGenerationConfig configuration, ChatCompletionBase connection) throws MalformedURLException {
        switch (connection.getInferenceType()) {
            case "PORTKEY":
                return new URL(InferenceConstants.PORTKEY_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "GROQ":
                return new URL(InferenceConstants.GROQ_URL + InferenceConstants.CHAT_COMPLETIONS);
            case "HUGGING_FACE":
                return new URL(InferenceConstants.HUGGINGFACE_URL + "/models/" + connection.getModelName() + "/v1" + InferenceConstants.CHAT_COMPLETIONS);
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
                        .replace("{MODEL_ID}", connection.getModelName());
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
                throw new MalformedURLException("Unsupported inference type: " + connection.getInferenceType());
        }
    }

    /**
     * Get the appropriate URL for image generation based on the configuration.
     * @param connection the connector configuration
     * @return the URL for the image generation endpoint
     * @throws MalformedURLException if the URL is invalid
     */
    public static URL getConnectionURLImageGeneration(ChatCompletionBase connection) throws MalformedURLException {
        switch (connection.getInferenceType()) {
            case "OPENAI":
                return new URL(InferenceConstants.OPEN_AI_URL + InferenceConstants.OPENAI_GENERATE_IMAGES);
            case "HUGGING_FACE":
                return new URL(InferenceConstants.HUGGINGFACE_URL + "/models/" + connection.getModelName());
            default:
                throw new MalformedURLException("Unsupported inference type: " + connection.getInferenceType());
        }
    }

    /**
     * Execute a REST API call.
     * @param resourceUrl the URL to call
     * @param configuration the connector configuration
     * @param payload the payload to send
     * @return the response string
     * @throws IOException if an error occurs during the API call
     */
    public static String executeREST(URL resourceUrl, TextGenerationConfig configuration, ChatCompletionBase connection, String payload) throws IOException, TimeoutException {
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource URL cannot be null");
        }
        // Build initial request for headers and URI
        HttpRequest initialRequest = buildHttpRequest(resourceUrl, configuration, connection);
        // Convert MultiMap to Map
        MultiMap<String, String> headersMultiMap = initialRequest.getHeaders();
        Map<String, String> headersMap = new HashMap<>();
        headersMultiMap.forEach((key, values) -> {
            // Take the first value or concatenate if multiple values exist
            headersMap.put(key, String.join(",", values));
        });
        // Build final request with payload
        HttpRequestBuilder builder = HttpRequest.builder()
                .uri(initialRequest.getUri())
                .method(initialRequest.getMethod())
                .entity(new ByteArrayHttpEntity(payload.getBytes(StandardCharsets.UTF_8)));
        // Add headers individually
        headersMap.forEach(builder::addHeader);
        HttpRequest finalRequest = builder.build();
        HttpRequestOptions options = getRequestOptions(configuration, connection);

        HttpClient httpClient = connection.getHttpClient();
        if (httpClient == null) {
            throw new IllegalStateException("HttpClient is not initialized");
        }
        HttpResponse response = httpClient.send(finalRequest, options);
        return processResponse(response);
    }

//    /**
//     * Execute a REST API call with an existing request.
//     * @param request the HttpRequest to execute
//     * @param payload the payload to send
//     * @param configuration the connector configuration
//     * @return the response string
//     * @throws IOException if an error occurs during the API call
//     */
//    public static String executeREST(HttpRequest request, String payload, TextGenerationConfig configuration, BaseConnection connection) throws IOException, TimeoutException {
//        request = HttpRequest.builder()
//                .entity(new ByteArrayHttpEntity(payload.getBytes(StandardCharsets.UTF_8)))
//                .build();
//        HttpRequestOptions options = getRequestOptions(configuration, connection);
//
//        HttpResponse response = httpClient.send(request, options);
//        return processResponse(response);
//    }

    /**
     * Execute a REST API call for Hugging Face image generation.
     * @param resourceUrl the URL to call
     * @param configuration the connector configuration
     * @param payload the payload to send
     * @return the response string
     * @throws IOException if an error occurs during the API call
     */
    public static String executeRESTHuggingFaceImage(URL resourceUrl, TextGenerationConfig configuration, ChatCompletionBase connection, String payload) throws IOException, TimeoutException {
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource URL cannot be null");
        }
        // Build initial request for headers and URI
        HttpRequest initialRequest = buildHttpRequest(resourceUrl, configuration, connection);
        // Convert MultiMap to Map
        MultiMap<String, String> headersMultiMap = initialRequest.getHeaders();
        Map<String, String> headersMap = new HashMap<>();
        headersMultiMap.forEach((key, values) -> {
            // Take the first value or concatenate if multiple values exist
            headersMap.put(key, String.join(",", values));
        });
        // Build final request with payload
        HttpRequestBuilder builder = HttpRequest.builder()
                .uri(initialRequest.getUri())
                .method(initialRequest.getMethod())
                .entity(new ByteArrayHttpEntity(payload.getBytes(StandardCharsets.UTF_8)));
        // Add headers individually
        headersMap.forEach(builder::addHeader);
        HttpRequest finalRequest = builder.build();
        HttpRequestOptions options = getRequestOptions(configuration, connection);

        HttpClient httpClient = connection.getHttpClient();
        if (httpClient == null) {
            throw new IllegalStateException("HttpClient is not initialized in BaseConnection");
        }
        HttpResponse response = httpClient.send(finalRequest, options);
        return processHuggingFaceImageResponse(response, payload);
    }
    /**
     * Process the HTTP response for standard REST calls.
     * @param response the HttpResponse to process
     * @return the response string
     * @throws IOException if the response indicates an error
     */
    private static String processResponse(HttpResponse response) throws IOException {
        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            return new String(response.getEntity().getBytes(), StandardCharsets.UTF_8);
        } else {
            String errorResponse = new String(response.getEntity().getBytes(), StandardCharsets.UTF_8);
            LOGGER.error("API request failed with status code: {} and message: {}", statusCode, errorResponse);
            throw new IOException("API request failed with status code: " + statusCode + " and message: " + errorResponse);
        }
    }

    /**
     * Process the HTTP response for Hugging Face image generation.
     * @param response the HttpResponse to process
     * @param payload the original payload for context
     * @return the response string in JSON format
     * @throws IOException if the response indicates an error
     */
    private static String processHuggingFaceImageResponse(HttpResponse response, String payload) throws IOException {
        int statusCode = response.getStatusCode();
        JSONObject responseWrapper = new JSONObject();

        if (statusCode == 200) {
            String contentType = response.getHeaderValue("Content-Type");

            if (contentType != null && contentType.startsWith("image/")) {
                byte[] responseBytes = response.getEntity().getBytes();
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
            String errorResponse = new String(response.getEntity().getBytes(), StandardCharsets.UTF_8);
            LOGGER.error("API request failed with status code: {} and message: {}", statusCode, errorResponse);
            throw new IOException("API request failed with status code: " + statusCode + " and message: " + errorResponse);
        }
    }

    /**
     * Utility method to encode query parameters.
     * @param params the query parameters
     * @return the encoded query string
     * @throws UnsupportedEncodingException if encoding fails
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

    /**
     * Read response bytes from an input stream.
     * @param inputStream the input stream
     * @return the byte array
     * @throws IOException if reading fails
     */
    public static byte[] readResponseBytes(java.io.InputStream inputStream) throws IOException {
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