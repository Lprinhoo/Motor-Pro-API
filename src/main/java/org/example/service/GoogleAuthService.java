package org.example.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.example.exception.ApiException; // Certifique-se de que esta exceção está no seu projeto

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleAuthService {

    private final String googleClientId;
    private final GoogleIdTokenVerifier verifier;

    public GoogleAuthService(@Value("${google.client.id}") String googleClientId) {
        this.googleClientId = googleClientId;

        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                // Se você tiver um domínio específico para seu aplicativo, pode adicioná-lo aqui
                // .setHostedDomain("example.com")
                .build();
    }

    /**
     * Verifica um ID Token do Google e retorna as informações do payload.
     *
     * @param idTokenString O ID Token recebido do cliente.
     * @return O payload do ID Token contendo informações do usuário.
     * @throws ApiException se o token for inválido ou ocorrer um erro na verificação.
     */
    public GoogleIdToken.Payload verifyGoogleIdToken(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "ID Token do Google inválido.");
            }
            return idToken.getPayload();
        } catch (GeneralSecurityException | IOException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Erro ao verificar ID Token do Google: " + e.getMessage());
        }
    }
}
