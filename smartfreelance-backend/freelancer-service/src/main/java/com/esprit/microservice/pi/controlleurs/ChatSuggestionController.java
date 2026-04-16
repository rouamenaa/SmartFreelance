package com.esprit.microservice.pi.controlleurs;

import com.esprit.microservice.pi.services.ChatSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot/suggestions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatSuggestionController {

    private final ChatSuggestionService suggestionService;

    @GetMapping
    public Map<String, List<String>> getSuggestions(@RequestParam String q) {
        List<String> suggestions = suggestionService.getSuggestions(q);
        Map<String, List<String>> response = new HashMap<>();
        response.put("suggestions", suggestions);
        return response;
    }
}
