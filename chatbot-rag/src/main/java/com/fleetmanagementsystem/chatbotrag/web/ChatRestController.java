package com.fleetmanagementsystem.chatbotrag.web;

import com.fleetmanagementsystem.chatbotrag.services.ChatAiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatRestController {

    private ChatAiService chatAiService;

    public ChatRestController(ChatAiService chatAiService) {
        this.chatAiService = chatAiService;
    }


    @GetMapping("/ask")
    public String ask(@RequestParam String question){
        return chatAiService.ragChat(question);
    }
}
