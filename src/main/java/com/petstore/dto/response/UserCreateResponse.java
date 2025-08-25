package com.petstore.dto.response;

import lombok.Data;

@Data
public class UserCreateResponse {
    private int code;
    private String type;
    private String message;

}
