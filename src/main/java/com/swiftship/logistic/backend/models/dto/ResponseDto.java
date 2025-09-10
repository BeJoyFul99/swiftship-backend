package com.swiftship.logistic.backend.models.dto;


import com.swiftship.logistic.backend.models.enums.StatusCode;
import lombok.*;

import java.util.Optional;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private String message;
    private StatusCode code;

}
