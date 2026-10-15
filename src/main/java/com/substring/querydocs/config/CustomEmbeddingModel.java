package com.substring.querydocs.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Primary
public class CustomEmbeddingModel implements EmbeddingModel {

    private static final Logger log = LoggerFactory.getLogger(CustomEmbeddingModel.class);

    private final RestClient restClient;
    private final String apiKey;
    private final String model;
    private final int dimensions;

    public CustomEmbeddingModel(
            @Value("${spring.ai.openai.embedding.base-url:https://generativelanguage.googleapis.com/v1beta/openai/v1}") String baseUrl,
            @Value("${spring.ai.openai.embedding.api-key:${GEMINI_API_KEY:${OPENAI_API_KEY:}}}") String apiKey,
            @Value("${spring.ai.openai.embedding.model:gemini-embedding-001}") String model,
            @Value("${spring.ai.openai.embedding.options.dimensions:384}") int dimensions
    ) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.model = model;
        this.dimensions = dimensions;
        log.info("Initialized CustomEmbeddingModel with model={}, dimensions={}, baseUrl={}", model, dimensions, baseUrl);
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        List<String> inputs = request.getInstructions();
        if (inputs == null || inputs.isEmpty()) {
            return new EmbeddingResponse(List.of());
        }

        Map<String, Object> payload = Map.of(
                "model", model,
                "input", inputs,
                "dimensions", dimensions
        );

        try {
            OpenAiEmbeddingResponse response = restClient.post()
                    .uri("/embeddings")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(OpenAiEmbeddingResponse.class);

            if (response == null || response.data == null) {
                throw new RuntimeException("Empty response from embedding API");
            }

            List<Embedding> embeddings = new ArrayList<>();
            for (int i = 0; i < response.data.size(); i++) {
                EmbeddingData data = response.data.get(i);
                float[] floats = new float[data.embedding.length];
                for (int j = 0; j < data.embedding.length; j++) {
                    floats[j] = (float) data.embedding[j];
                }
                embeddings.add(new Embedding(floats, i));
            }
            return new EmbeddingResponse(embeddings);
        } catch (Exception e) {
            log.error("Failed to generate embeddings: {}", e.getMessage(), e);
            throw new RuntimeException("Embedding API call failed: " + e.getMessage(), e);
        }
    }

    @Override
    public float[] embed(Document document) {
        return embed(document.getText());
    }

    @Override
    public float[] embed(String text) {
        List<String> list = List.of(text);
        EmbeddingResponse res = call(new EmbeddingRequest(list, null));
        if (res.getResults().isEmpty()) {
            return new float[dimensions];
        }
        return res.getResults().get(0).getOutput();
    }

    @Override
    public int dimensions() {
        return dimensions;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpenAiEmbeddingResponse {
        @JsonProperty("data")
        public List<EmbeddingData> data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class EmbeddingData {
        @JsonProperty("embedding")
        public double[] embedding;
    }
}
