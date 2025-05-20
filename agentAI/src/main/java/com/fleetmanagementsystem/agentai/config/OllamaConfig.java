package com.fleetmanagementsystem.agentai.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")  // Ollama API URL
                .modelName("mistral:instruct")             // Model you pulled
                .temperature(0.2)
                .build();
    }


    @Bean
    ChatMemoryProvider chatMemoryProvider(Tokenizer tokenizer){
        return chatId-> MessageWindowChatMemory.withMaxMessages(10);
    }





}
