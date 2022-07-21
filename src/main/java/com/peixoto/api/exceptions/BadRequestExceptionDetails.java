package com.peixoto.api.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BadRequestExceptionDetails {

    private String title;

    private int status;

    private String details;

    private LocalDateTime timestamp;
}
