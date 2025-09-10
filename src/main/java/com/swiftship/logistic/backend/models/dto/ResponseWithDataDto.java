package com.swiftship.logistic.backend.models.dto;


import com.swiftship.logistic.backend.models.enums.StatusCode;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWithDataDto {
    private String message;
    private Object data;
    private StatusCode code;
}
