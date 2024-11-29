package com.sjsu.cmpe272.prodwell.dto;

import lombok.Data;

@Data
public class LlmRequest {
    private String model;
    private Message[] messages;

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}