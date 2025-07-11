package ar.edu.uade.desa1.service;

import ar.edu.uade.desa1.domain.request.*;
import ar.edu.uade.desa1.domain.response.*;

import javax.management.relation.RoleNotFoundException;

public interface AuthService {

    AuthRegisterResponse register(AuthRegisterRequest request) throws RoleNotFoundException;

    AuthLoginResponse login(AuthLoginRequest request);

    VerifyCodeResponse verifyCode(VerifyCodeRequest request);

    SendVerificationCodeResponse sendVerificationCode(SendVerificationCodeRequest request);

    ValidateResetTokenResponse validateResetToken(ValidateResetTokenRequest request);

    void resetPassword(PasswordResetRequest request);

}
