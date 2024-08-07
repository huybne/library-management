package com.ensas.librarymanagementsystem.dto.response;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class VerifyResponse {
    boolean valid;
}
