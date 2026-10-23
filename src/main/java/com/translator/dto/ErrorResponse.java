package com.translator.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponse(
        String message,
        List<String> details,
        OffsetDateTime timestamp
) {
}