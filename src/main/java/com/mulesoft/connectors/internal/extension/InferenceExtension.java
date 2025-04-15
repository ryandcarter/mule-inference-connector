package com.mulesoft.connectors.internal.extension;

import com.mulesoft.connectors.internal.api.proxy.DefaultNtlmProxyConfig;
import com.mulesoft.connectors.internal.api.proxy.DefaultProxyConfig;
import com.mulesoft.connectors.internal.config.*;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.SubTypeMapping;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.sdk.api.annotation.JavaVersionSupport;
import com.mulesoft.connectors.internal.api.proxy.HttpProxyConfig;
import static org.mule.sdk.api.meta.JavaVersion.JAVA_17;

/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "mac-inference")
@Extension(name = "MuleSoft Inference")
@JavaVersionSupport({JAVA_17})
@SubTypeMapping(baseType = HttpProxyConfig.class, subTypes = {DefaultProxyConfig.class, DefaultNtlmProxyConfig.class})
@Configurations({
        TextGenerationConfig.class,
        VisionConfig.class,
        ImageGenerationConfig.class,
        ModerationConfig.class
})
public class InferenceExtension {

}
