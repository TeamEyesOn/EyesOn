package com.backend.eyeson.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLoginDto {
    private TokenDto tokenDto;
    private String role;
    private char gender;
}
