package com.example.openaiclient.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RequestBean {

    @SerializedName("api_key")
    public String apiKey;
    @SerializedName("max_tokens")
    public Integer maxTokens;
    @SerializedName("contents")
    public List<ContentsDTO> contents;

    public static class ContentsDTO {
        @SerializedName("role")
        public String role;
        @SerializedName("content")
        public String content;
    }
}
