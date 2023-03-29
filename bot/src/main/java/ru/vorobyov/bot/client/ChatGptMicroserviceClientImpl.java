package ru.vorobyov.bot.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.vorobyov.bot.dto.ChatMessageRequest;
import ru.vorobyov.bot.dto.ChatMessageResponse;
import ru.vorobyov.bot.dto.RegisterTokenRequest;
import ru.vorobyov.bot.dto.RegisterTokenResponse;

public class ChatGptMicroserviceClientImpl implements ChatGptMicroserviceClient{
    private static final String DEFAULT_URL = "http://localhost:8083/";

    private final WebClient webClient;

    public ChatGptMicroserviceClientImpl(String baseUrl) {
        String url = baseUrl == null || baseUrl.isBlank() ? DEFAULT_URL : baseUrl;
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    @Override
    public RegisterTokenResponse registerToken(String token) {
        RegisterTokenRequest request = new RegisterTokenRequest(token);
        return webClient.post()
                .uri("/auth")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(request), RegisterTokenRequest.class)
                .retrieve()
                .bodyToMono(RegisterTokenResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                        RegisterTokenResponse response = new RegisterTokenResponse();
                        response.setAdditionalProperty("error", "400");
                        return Mono.just(response);
                    } else if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                        RegisterTokenResponse response = new RegisterTokenResponse();
                        response.setAdditionalProperty("error", "401");
                        return Mono.just(response);
                    } else if (e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                        RegisterTokenResponse response = new RegisterTokenResponse();
                        response.setAdditionalProperty("error", "500");
                        return Mono.just(response);
                    } else {
                        return Mono.error(e);
                    }
                })
                .block();
    }

    @Override
    public ChatMessageResponse sendMessage(String gptId, String gptChatId, String message) {
        ChatMessageRequest request = new ChatMessageRequest(gptId, gptChatId, message);
        return webClient.post()
                .uri("/chat")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(request), ChatMessageRequest.class)
                .retrieve()
                .bodyToMono(ChatMessageResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                        ChatMessageResponse response = new ChatMessageResponse();
                        response.setAdditionalProperty("error", "400");
                        return Mono.just(response);
                    } else if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                        ChatMessageResponse response = new ChatMessageResponse();
                        response.setAdditionalProperty("error", "401");
                        return Mono.just(response);
                    } else if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                        ChatMessageResponse response = new ChatMessageResponse();
                        response.setAdditionalProperty("error", "404");
                        return Mono.just(response);
                    } else if (e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                        ChatMessageResponse response = new ChatMessageResponse();
                        response.setAdditionalProperty("error", "500");
                        return Mono.just(response);
                    } else {
                        return Mono.error(e);
                    }
                })
                .block();
    }
}
