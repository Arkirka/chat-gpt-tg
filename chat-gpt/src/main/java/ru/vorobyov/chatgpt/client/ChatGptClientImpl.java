package ru.vorobyov.chatgpt.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.vorobyov.chatgpt.dto.ChatCompletionsRequest;
import ru.vorobyov.chatgpt.dto.ChatCompletionsResponse;
import ru.vorobyov.chatgpt.dto.Message;

import java.util.List;

public class ChatGptClientImpl implements ChatGptClient {
    private static final String DEFAULT_URL = "https://api.openai.com/v1/";

    private final WebClient webClient;

    public ChatGptClientImpl(String baseUrl) {
        String url = baseUrl == null || baseUrl.isBlank() ? DEFAULT_URL : baseUrl;
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    @Override
    public ChatCompletionsResponse sendChatMessage(String token, ChatCompletionsRequest request) {
        ChatCompletionsRequest temp = new ChatCompletionsRequest();
        temp.setModel("gpt-3.5-turbo");
        temp.setMessages(List.of(new Message()));
        return webClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .body(Mono.just(request), ChatCompletionsRequest.class)
                .retrieve()
                .bodyToMono(ChatCompletionsResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                        ChatCompletionsResponse response = new ChatCompletionsResponse();
                        response.setAdditionalProperty("error", "401");
                        return Mono.just(response);
                    } else {
                        return Mono.error(e);
                    }
                })
                .block();
    }

}
