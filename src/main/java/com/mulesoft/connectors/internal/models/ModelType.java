/**
 * (c) 2003-2024 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package com.mulesoft.connectors.internal.models;


import java.util.Arrays;
import java.util.stream.Stream;

import com.mulesoft.connectors.internal.exception.error.ConfigValidationException;

public enum ModelType {
       HUGGING_FACE("HUGGING_FACE", getHuggingFaceModelNameStream()),
       GROQ("GROQ", getGroqModelNameStream()),
       PORTKEY("PORTKEY", getPortkeyModelNameStream());

  private final String value;
  private final Stream<String> modelNameStream;

  ModelType(String value, Stream<String> modelNameStream) {
    this.value = value;
    this.modelNameStream = modelNameStream;
  }

  public String getValue() {
    return value;
  }

  public Stream<String> getModelNameStream() {
    return modelNameStream;
  }

  private static Stream<String> getHuggingFaceModelNameStream() {
    return Arrays.stream(HuggingFaceModelName.values()).map(String::valueOf);
  }

  private static Stream<String> getGroqModelNameStream() {
    return Arrays.stream(GroqModelName.values()).map(String::valueOf);
  }


  private static Stream<String> getPortkeyModelNameStream() {
    return Arrays.stream(PortkeyModelName.values()).map(String::valueOf);
  }

  public static ModelType fromValue(String value) {
    return Arrays.stream(ModelType.values())
        .filter(langchainLLMType -> langchainLLMType.value.equals(value))
        .findFirst()
        .orElseThrow(() -> new ConfigValidationException("Unsupported LLM Type: " + value));
  }

  enum GroqModelName {
    MIXTRAL_8x7b_32768("mixtral-8x7b-32768"),
    LLAMA_3_2_3b_PREVIEW("llama-3.2-3b-preview"),
    LLAMA_3_70b_8192("llama3-70b-8192"),
    LLAMA_3_2_90b_VISION_PREVIEW("llama-3.2-90b-vision-preview"),
    LLAMA_3_2_11b_TEXT_PREVIEW("llama-3.2-11b-text-preview"),
    LLAMA_3_2_1b_PREVIEW("llama-3.2-1b-preview"),
    GEMMA2_9b_IT("gemma2-9b-it"),
    LLAMA3_GROQ_8b_8192_TOOL_USE_PREVIEW("llama3-groq-8b-8192-tool-use-preview"),
    LLAVA_V1_5_7b_4096_PREVIEW("llava-v1.5-7b-4096-preview"),
    LLAMA_3_2_11b_VISION_PREVIEW("llama-3.2-11b-vision-preview"),
    LLAMA_3_2_90b_TEXT_PREVIEW("llama-3.2-90b-text-preview"),
    LLAMA_3_1_8b_INSTANT("llama-3.1-8b-instant"),
    LLAMA3_8b_8192("llama3-8b-8192"),
    LLAMA3_GROQ_70b_8192_TOOL_USE_PREVIEW("llama3-groq-70b-8192-tool-use-preview"),
    LLAMA_3_1_70b_VERSATILE("llama-3.1-70b-versatile"),
    GEMMA_7b_IT("gemma-7b-it"),
    LLAMA_GUARD_3_8b("llama-guard-3-8b");
    
    private final String value;

    GroqModelName(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return this.value;
    }
  }

  enum HuggingFaceModelName {
    TII_UAE_FALCON_7B_INSTRUCT("tiiuae/falcon-7b-instruct"), 
    PHI3("microsoft/Phi-3.5-mini-instruct"), 
    MISTRAL_7B_INSTRUCT_V03("mistralai/Mistral-7B-Instruct-v0.3"), 
    TINY_LLAMA("TinyLlama/TinyLlama-1.1B-Chat-v1.0");

    private final String value;

    HuggingFaceModelName(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return this.value;
    }
  }

  enum PortkeyModelName {
    GPT_4O("gpt-4o"),
    CHATGPT_4O_LATEST("chatgpt-4o-latest"),
    GPT_4O_MINI("gpt-4o-mini"),
    MISTRAL_LARGE_LATEST("mistral-large-latest"),
    MISTRAL_SMALL_LATEST("mistral-small-latest"),
    GPT_4_TURBO("gpt-4-turbo"),
    GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview"),
    GPT_4("gpt-4"),
    GPT_3_5_TURBO("gpt-3.5-turbo");
    
    private final String value;

    PortkeyModelName(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return this.value;
    }
  }
}
