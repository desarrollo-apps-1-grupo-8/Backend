package ar.edu.uade.desa1.service;

import ar.edu.uade.desa1.domain.request.AuthLoginRequest;
import ar.edu.uade.desa1.domain.request.AuthRegisterRequest;
import ar.edu.uade.desa1.domain.request.SendVerificationCodeRequest;
import ar.edu.uade.desa1.domain.response.AuthLoginResponse;
import ar.edu.uade.desa1.domain.request.VerifyEmailRequest;
import ar.edu.uade.desa1.domain.response.AuthRegisterResponse;
import ar.edu.uade.desa1.domain.response.SendVerificationCodeResponse;
import ar.edu.uade.desa1.domain.response.VerifyEmailResponse;

public interface AuthService {
    AuthRegisterResponse register(AuthRegisterRequest request);
    AuthLoginResponse login(AuthLoginRequest request);
    VerifyEmailResponse verifyEmail(VerifyEmailRequest request);
    SendVerificationCodeResponse sendVerificationCode(SendVerificationCodeRequest request);
}
