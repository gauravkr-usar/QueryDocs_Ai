package com.substring.querydocs;

import com.substring.querydocs.config.CustomEmbeddingModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class QueryDocsBackendApplicationTests {

    @Test
    void testEmbeddingModelDirectly() {
        String geminiKey = System.getenv("GEMINI_API_KEY");
        if (geminiKey == null || geminiKey.isBlank()) {
            geminiKey = System.getProperty("GEMINI_API_KEY", "");
        }
        if (geminiKey.isBlank()) {
            return; // Skip if no key provided in CI/test environment
        }
        CustomEmbeddingModel model = new CustomEmbeddingModel(
                "https://generativelanguage.googleapis.com/v1beta/openai/v1",
                geminiKey,
                "gemini-embedding-001",
                384
        );

        float[] embedding = model.embed("test text");
        Assertions.assertNotNull(embedding);
        Assertions.assertEquals(384, embedding.length, "Embedding dimension must be 384");
    }
}
