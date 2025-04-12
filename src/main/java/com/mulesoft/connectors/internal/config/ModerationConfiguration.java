package com.mulesoft.connectors.internal.config;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;

import com.mulesoft.connectors.internal.models.moderation.ModerationNameProvider;
import com.mulesoft.connectors.internal.models.moderation.ModerationTypeProvider;
import com.mulesoft.connectors.internal.operations.ModerationOperations;

@Operations(ModerationOperations.class)
@Configuration(name="moderation-config-old")
public class ModerationConfiguration {
 
    @Parameter
    @Placement(order = 1, tab = Placement.DEFAULT_TAB)
    @Expression(ExpressionSupport.SUPPORTED)
    @DisplayName("Inference Type")
    @OfValues(ModerationTypeProvider.class)
    private String inferenceType;

    public String getInferenceType() {
        return inferenceType;
    }
    
    @Parameter
    @Expression(ExpressionSupport.SUPPORTED)
    @DisplayName("API Key")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    @Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@OfValues(ModerationNameProvider.class)
	private String moderationModelName;

	public String getModerationModelName() {
		return moderationModelName;
	}


}
