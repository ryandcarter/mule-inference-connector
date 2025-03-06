/**
 * (c) 2003-2024 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package com.mulesoft.connectors.internal.constants;

public class InferenceConstants {

  private InferenceConstants() {}

  public static final String PORTKEY_URL = "https://api.portkey.ai/v1";
  public static final String HUGGINGFACE_URL = "https://api-inference.huggingface.co";
  public static final String GROQ_URL = "https://api.groq.com/openai/v1";
  public static final String OPENROUTER_URL ="https://openrouter.ai/api/v1";
  public static final String GITHUB_MODELS_URL = "https://models.inference.ai.azure.com";
  public static final String CEREBRAS_URL = "https://api.cerebras.ai/v1";
  public static final String NVIDIA_URL = "https://integrate.api.nvidia.com/v1";
  public static final String FIREWORKS_URL = "https://api.fireworks.ai/inference/v1";
  public static final String TOGETHER_URL = "https://api.together.xyz/v1";
  public static final String DEEPINFRA_URL = "https://api.deepinfra.com/v1/openai";
  public static final String PERPLEXITY_URL = "https://api.perplexity.ai";
  public static final String X_AI_URL = "https://api.x.ai/v1";
  public static final String OPEN_AI_URL = "https://api.openai.com/v1";
  public static final String MISTRAL_AI_URL = "https://api.mistral.ai/v1";

  // Resources
  public static final String CHAT_COMPLETIONS = "/chat/completions";
  public static final String CHAT_COMPLETIONS_OLLAMA = "/chat";
  public static final String GENERATION_OLLAMA = "/generate";

  // Configuration Parameters
  public static final String MAX_TOKENS = "max_tokens";
  public static final String MAX_COMPLETION_TOKENS = "max_completion_tokens";
  public static final String TEMPERATURE = "temperature";
  public static final String TOP_P = "top_p";
  
  public static final String RESPONSE = "response";
  public static final String FINISH_REASON = "finish_reason";
  public static final String MODEL = "model";
  public static final String ID_STRING = "id";
  public static final String MESSAGES = "messages";
  public static final String TOOLS = "tools";
  public static final String TOOL_CALLS = "tool_calls";
  

  


}

