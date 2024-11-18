# MAC INFERENCE CONNECTOR
[MAC Inference Connector Overview](https://mac-project.ai/docs/mac-inference/connector-overview)

MAC Inference Connector provides access to Inference Offering for Large Language Models i.e. Groq, Hugging Face, Github Models, etc.

## Supported Inference Providers
The MAC Inference Connector supports the following Inference Offerings:

- [GitHub Models](https://docs.github.com/en/github-models)
- [Hugging Face](https://huggingface.co/)
- [Ollama](https://ollama.com/)
- [Groq AI](https://console.groq.com/)
- [Portkey](https://portkey.ai/)
- [OpenRouter](https://openrouter.ai/)
- [Cerebras](https://cerebras.ai/inference)


### Installation (building locally)

To use this connector, first [build and install](https://mac-project.ai/docs/mac-inference/getting-started) the connector into your local maven repository.
Then add the following dependency to your application's `pom.xml`:

```xml
<dependency>
    <groupId>com.mulesoft.connectors</groupId>
    <artifactId>mac-inference-chain</artifactId>
    <version>0.1.0</version>
    <classifier>mule-plugin</classifier>
</dependency>
```

### Installation into private Anypoint Exchange

You can also make this connector available as an asset in your Anyooint Exchange.

This process will require you to build the connector as above, but additionally you will need
to make some changes to the `pom.xml`.  For this reason, we recommend you fork the repository.

Then, follow the MuleSoft [documentation](https://docs.mulesoft.com/exchange/to-publish-assets-maven) to modify and publish the asset.

### Documentation 
- Check out the complete documentation in [mac-project.ai](https://mac-project.ai/docs/mulechain-vectors)
- Learn from the [Getting Started YouTube Playlist](https://www.youtube.com/playlist?list=PLnuJGpEBF6ZAV1JfID1SRKN6OmGORvgv6)

---

### Stay tuned!

- 🌐 **Website**: [mac-project.ai](https://mac-project.ai)
- 📺 **YouTube**: [@MuleSoft-MAC-Project](https://www.youtube.com/@MuleSoft-MAC-Project)
- 💼 **LinkedIn**: [MAC Project Group](https://lnkd.in/gW3eZrbF)

