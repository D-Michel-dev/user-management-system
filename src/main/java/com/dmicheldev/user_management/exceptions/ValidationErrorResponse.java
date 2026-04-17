package com.dmicheldev.user_management.exceptions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timeStamp;
    private Map<String, List<String>> validationErrors;

}
