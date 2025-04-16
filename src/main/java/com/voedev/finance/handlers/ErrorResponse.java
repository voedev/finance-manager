package com.voedev.finance.handlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private int status;
    private String error;
    private Instant timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    private String path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> details;
}
