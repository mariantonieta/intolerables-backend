package anto.es.intolerables.controllers;

import anto.es.intolerables.dto.ChatRequestDTO;
import anto.es.intolerables.dto.ChatResponseDTO;
import anto.es.intolerables.services.GroqService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private GroqService groqService;

    @InjectMocks
    private ChatController chatController;

    private ChatRequestDTO chatRequest;
    private final String testResponse = "Respuesta simulada del servicio";

    @BeforeEach
    void setUp() {
        chatRequest = new ChatRequestDTO();
        chatRequest.setMessage("Mensaje de prueba");
    }

    @Test
    void chat_ShouldReturnValidResponse() {
        // Arrange
        when(groqService.getGroqResponse(anyString())).thenReturn(testResponse);

        // Act
        ChatResponseDTO response = chatController.chat(chatRequest);

        // Assert
        assertNotNull(response);
        assertEquals(testResponse, response.getResponse());
    }

    @Test
    void chat_ShouldCallServiceWithCorrectMessage() {
        // Arrange
        String expectedMessage = "Mensaje específico";
        chatRequest.setMessage(expectedMessage);
        when(groqService.getGroqResponse(anyString())).thenReturn(testResponse);

        // Act
        chatController.chat(chatRequest);

        // Assert
        verify(groqService, times(1)).getGroqResponse(expectedMessage);
    }

    @Test
    void chat_ShouldHandleEmptyMessage() {
        // Arrange
        chatRequest.setMessage("");
        when(groqService.getGroqResponse(anyString())).thenReturn(testResponse);

        // Act
        ChatResponseDTO response = chatController.chat(chatRequest);

        // Assert
        assertNotNull(response);
        assertEquals(testResponse, response.getResponse());
        verify(groqService).getGroqResponse("");
    }


    @Test
    void chat_ShouldReturnNewResponseDTOInstance() {
        // Arrange
        when(groqService.getGroqResponse(anyString())).thenReturn(testResponse);

        // Act
        ChatResponseDTO response = chatController.chat(chatRequest);

        // Assert
        assertNotNull(response);
        assertEquals(ChatResponseDTO.class, response.getClass());
    }

    @Test
    void chat_ShouldUseConstructorInjection() {
        // Arrange
        GroqService testService = mock(GroqService.class);
        ChatController controller = new ChatController(testService);
        when(testService.getGroqResponse(anyString())).thenReturn(testResponse);

        // Act
        ChatResponseDTO response = controller.chat(chatRequest);

        // Assert
        assertNotNull(response);
        verify(testService).getGroqResponse(chatRequest.getMessage());
    }
}