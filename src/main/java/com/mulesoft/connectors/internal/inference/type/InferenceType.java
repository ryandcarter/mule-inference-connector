/**
 * (c) 2003-2024 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package com.mulesoft.connectors.internal.inference.type;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public enum InferenceType {
  PORTKEY("PORTKEY", getOpenAIModelNameStream(), LangchainLLMInitializerUtil::createOpenAiChatModel), GROQ("GROQ",
      OPENAI.getModelNameStream(), LangchainLLMInitializerUtil::createGroqOpenAiChatModel));

  private final String value;
  private final Stream<String> modelNameStream;

  private final BiFunction<ConfigExtractor, LangchainLLMConfiguration, ChatLanguageModel> configBiFunction;

  InferenceType(String value, Stream<String> modelNameStream,
                   BiFunction<ConfigExtractor, LangchainLLMConfiguration, ChatLanguageModel> configBiFunction) {
    this.value = value;
    this.modelNameStream = modelNameStream;
    this.configBiFunction = configBiFunction;
  }

  public String getValue() {
    return value;
  }

  public Stream<String> getModelNameStream() {
    return modelNameStream;
  }

  public BiFunction<ConfigExtractor, LangchainLLMConfiguration, ChatLanguageModel> getConfigBiFunction() {
    return configBiFunction;
  }

  private static Stream<String> getOpenAIModelNameStream() {
    return Stream.concat(Arrays.stream(OpenAiChatModelName.values()), Arrays.stream(OpenAiImageModelName.values()))
        .map(String::valueOf);
  }

  private static Stream<String> getMistralAIModelNameStream() {
    return Arrays.stream(MistralAiChatModelName.values()).map(String::valueOf);
  }

  private static Stream<String> getOllamaModelNameStream() {
    return Arrays.stream(OllamaModelName.values()).map(String::valueOf);
  }

  private static Stream<String> getHuggingFaceModelNameStream() {
    return Arrays.stream(HuggingFaceModelName.values()).map(String::valueOf);
  }

  private static Stream<String> getAnthropicModelNameStream() {
    return Arrays.stream(AnthropicChatModelName.values()).map(String::valueOf);
  }

  public static InferenceType fromValue(String value) {
    return Arrays.stream(InferenceType.values())
        .filter(langchainLLMType -> langchainLLMType.value.equals(value))
        .findFirst()
        .orElseThrow(() -> new ConfigValidationException("Unsupported LLM Type: " + value));
  }

  enum OllamaModelName {
    MISTRAL("mistral"), PHI3("phi3"), ORCA_MINI("orca-mini"), LLAMA2("llama2"), CODE_LLAMA("codellama"), TINY_LLAMA("tinyllama");

    private final String value;

    OllamaModelName(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return this.value;
    }
  }

  enum HuggingFaceModelName {
    TII_UAE_FALCON_7B_INSTRUCT("tiiuae/falcon-7b-instruct"), PHI3("microsoft/Phi-3.5-mini-instruct"), MISTRAL_7B_INSTRUCT_V03(
        "mistralai/Mistral-7B-Instruct-v0.3"), TINY_LLAMA("TinyLlama/TinyLlama-1.1B-Chat-v1.0");

    private final String value;

    HuggingFaceModelName(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return this.value;
    }
  }

}
