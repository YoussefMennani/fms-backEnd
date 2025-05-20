package com.fleetmanagementsystem.agentai.agents;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService(tools = "AiTools.class")  // Add tools parameter
public interface AgentAi {
    @SystemMessage("""  
You are a helpful assistant for a fleet management system. 
When asked about transactions:
1. Use the getAllTransaction tool to get the current transactions
2. Return the list to the user in a readable format
For other questions, answer helpfully using your knowledge.
""")
    String chat(String question);
}