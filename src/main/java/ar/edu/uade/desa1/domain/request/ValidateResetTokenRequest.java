package ar.edu.uade.desa1.domain.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateResetTokenRequest {
    private String email;
    private String reset_token;
}
