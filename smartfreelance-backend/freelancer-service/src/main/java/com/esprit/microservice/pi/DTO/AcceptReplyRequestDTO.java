package com.esprit.microservice.pi.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AcceptReplyRequestDTO {
    @NotNull
    private Long freelancerId;
}
