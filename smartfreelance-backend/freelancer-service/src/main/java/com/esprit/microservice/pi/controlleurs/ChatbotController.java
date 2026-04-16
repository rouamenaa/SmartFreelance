package com.esprit.microservice.pi.controlleurs;

import com.esprit.microservice.pi.DTO.ChatbotResponseDTO;
import com.esprit.microservice.pi.services.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*") // Permettre l'accès depuis Angular
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/recommend")
    public List<ChatbotResponseDTO> recommend(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        return chatbotService.recommendFreelancers(message);
    }
}
