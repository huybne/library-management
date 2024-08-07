package com.ensas.librarymanagementsystem.dto.request;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class VerifyRequest {
    String token;
    boolean authenticated ;
}
