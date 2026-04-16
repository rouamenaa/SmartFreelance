package com.esprit.microservice.pi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChatSuggestionServiceTest {

    @InjectMocks
    private ChatSuggestionService service;

    @Test
    void shouldReturnSuggestions_whenSkillFound() {
        // WHEN
        List<String> result = service.getSuggestions("Ang");

        // THEN
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(s -> s.contains("Angular")));
    }

    @Test
    void shouldReturnGenericSuggestions_whenNoSkillFound() {
        // WHEN
        List<String> result = service.getSuggestions("How are you");

        // THEN
        assertFalse(result.isEmpty());
        assertTrue(result.contains("Je cherche un freelancer..."));
    }

    @Test
    void shouldReturnEmpty_whenQueryTooShort() {
        // WHEN
        List<String> result = service.getSuggestions("a");

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmpty_whenQueryIsNull() {
        // WHEN
        List<String> result = service.getSuggestions(null);

        // THEN
        assertTrue(result.isEmpty());
    }
}
