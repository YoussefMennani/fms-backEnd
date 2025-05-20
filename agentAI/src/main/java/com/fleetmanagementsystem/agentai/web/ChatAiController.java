package com.fleetmanagementsystem.agentai.web;

import com.fleetmanagementsystem.agentai.agents.AgentAi;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class ChatAiController {


    private AgentAi agentAi;

    public ChatAiController(AgentAi agentAi) {
        this.agentAi = agentAi;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(defaultValue = "Bonjour") String question){
        return agentAi.chat(question);
    }
}