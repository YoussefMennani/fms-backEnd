package com.fleetmanagementsystem.agentai.agents;

import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AiTools {

    @Tool("Get all Transaction")
    public List<String> getAllTransaction(){
        return List.of("ABCD","1234");
    }

}
