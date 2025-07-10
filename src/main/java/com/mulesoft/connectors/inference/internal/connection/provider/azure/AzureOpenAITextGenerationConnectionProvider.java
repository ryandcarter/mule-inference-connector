package com.mulesoft.connectors.inference.internal.connection.provider.azure;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;

import com.mulesoft.connectors.inference.internal.connection.parameters.TextGenerationConnectionParameters;
import com.mulesoft.connectors.inference.internal.connection.provider.TextGenerationConnectionProvider;
import com.mulesoft.connectors.inference.internal.connection.types.azure.AzureOpenAITextGenerationConnection;
import com.mulesoft.connectors.inference.internal.dto.ParametersDTO;
import com.mulesoft.connectors.inference.internal.llmmodels.azure.providers.AzureOpenAITextGenerationModelNameProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alias("azure-openai")
@DisplayName("Azure OpenAI")
public class AzureOpenAITextGenerationConnectionProvider extends TextGenerationConnectionProvider {

  private static final Logger logger = LoggerFactory.getLogger(AzureOpenAITextGenerationConnectionProvider.class);

  @Parameter
  @Placement(order = 1)
  @Expression(ExpressionSupport.SUPPORTED)
  @OfValues(AzureOpenAITextGenerationModelNameProvider.class)
  private String azureModelName;

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional
  @DisplayName("[Azure OpenAI] Resource Name")
  @Placement(order = 2)
  private String azureOpenaiResourceName;

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional
  @DisplayName("[Azure OpenAI] Deployment ID")
  @Placement(order = 3)
  private String azureOpenaiDeploymentId;

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional
  @DisplayName("[Azure OpenAI] User")
  @Placement(order = 4)
  private String azureOpenaiUser;

  @ParameterGroup(name = Placement.CONNECTION_TAB)
  private TextGenerationConnectionParameters textGenerationConnectionParameters;

  @Override
  public AzureOpenAITextGenerationConnection connect() throws ConnectionException {
    logger.debug("AzureTextGenerationConnection connect ...");
    return new AzureOpenAITextGenerationConnection(
                                                   getHttpClient(), getObjectMapper(),
                                                   new ParametersDTO(azureModelName,
                                                                     textGenerationConnectionParameters.getApiKey(),
                                                                     textGenerationConnectionParameters.getMaxTokens(),
                                                                     textGenerationConnectionParameters.getTemperature(),
                                                                     textGenerationConnectionParameters.getTopP(),
                                                                     textGenerationConnectionParameters.getTimeout()),
                                                   azureOpenaiResourceName, azureOpenaiDeploymentId, azureOpenaiUser,
                                                   textGenerationConnectionParameters.getMcpSseServers());
  }
}
