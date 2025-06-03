package anto.es.intolerables.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GroqServiceTest {

    @InjectMocks
    private GroqService groqService;

    @Mock
    private RestTemplate restTemplate;

    @Captor
    private ArgumentCaptor<HttpEntity<Map<String, Object>>> entityCaptor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(groqService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(groqService, "restTemplate", restTemplate);
    }

    @Test
    void getGroqResponse_returnsExpectedMessage() throws Exception {
        String mockJson = """
        {
            "choices": [
                {
                    "message": {
                        "content": "Hola, esta es la respuesta del modelo."
                    }
                }
            ]
        }
        """;

        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockJson, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        String result = groqService.getGroqResponse("Hola modelo");
        assertThat(result).isEqualTo("Hola, esta es la respuesta del modelo.");
    }

    @Test
    void getGroqResponse_returnsErrorMessageOnException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Fallo de conexión"));

        String result = groqService.getGroqResponse("Hola modelo");
        assertThat(result).contains("Error al comunicarse con Groq");
    }

    @Test
    void buscarRestaurantesIA_returnsErrorOnException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Error inesperado"));

        List<Map<String, String>> result = groqService.buscarRestaurantesIA("lactosa", "italiana", "Barcelona");

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).containsKey("error");
        assertThat(result.get(0).get("error")).contains("Error al comunicarse con Groq");
    }
}
