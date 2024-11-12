package com.thegame.business.dto;

import java.util.List;

public class ResponseDto {
    private Long id;
    private String message;

    public ResponseDto() {
    }

    public ResponseDto(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
