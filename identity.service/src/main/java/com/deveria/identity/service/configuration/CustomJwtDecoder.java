package com.deveria.identity.service.configuration;

import com.deveria.identity.service.dto.request.IntrospectRequest;
import com.deveria.identity.service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signer.key}")
    private String signerKey;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;


    // This method is called to decode and validate a JWT token.
    @Override
    public Jwt decode(String token) throws JwtException {
        try{ // test if token is still valid
            var response = authenticationService.introspect(
                    IntrospectRequest.builder()
                            .token(token)
                            .build());

            if(!response.isValid()){
                throw new JwtException("Invalid token");
            }
        }catch(JOSEException | ParseException e){   // token không hợp lệ
            throw new JwtException(e.getMessage());
        }

        // lazy init
        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HmacSHA256"); // Create SecretKeySpec from signerKey
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
