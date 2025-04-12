package com.mulesoft.connectors.internal.extension;

import com.mulesoft.connectors.internal.config.*;
import com.mulesoft.connectors.internal.connection.types.VisionProvider;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.sdk.api.annotation.JavaVersionSupport;

import static org.mule.sdk.api.meta.JavaVersion.JAVA_17;

/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "mac-inference")
@Extension(name = "MuleSoft Inference")
@JavaVersionSupport({JAVA_17})
//@Configurations(value = {
//        InferenceConfiguration.class,
//        ModerationConfiguration.class,
//        VisionConfiguration.class,
//        ImageGenerationConfiguration.class
//})

@Configurations({TextGenerationConfig.class, VisionConfig.class})
public class InferenceExtension {

}
