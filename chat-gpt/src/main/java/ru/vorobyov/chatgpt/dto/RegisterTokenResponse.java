package ru.vorobyov.chatgpt.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user_id",
        "chat_id"
})
@Generated("jsonschema2pojo")
public class RegisterTokenResponse {

    public RegisterTokenResponse() {
    }

    public RegisterTokenResponse(String userId, String chatId) {
        this.userId = userId;
        this.chatId = chatId;
    }

    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("chat_id")
    private String chatId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("chat_id")
    public String getChatId() {
        return chatId;
    }

    @JsonProperty("chat_id")
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
