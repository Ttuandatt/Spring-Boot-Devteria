package com.deveria.identity.service.service;

import ch.qos.logback.classic.Logger;
import com.deveria.identity.service.dto.request.AuthenticationRequest;
import com.deveria.identity.service.dto.request.IntrospectRequest;
import com.deveria.identity.service.dto.response.AuthenticationResponse;
import com.deveria.identity.service.dto.response.IntrospectResponse;
import com.deveria.identity.service.entity.User;
import com.deveria.identity.service.exception.AppException;
import com.deveria.identity.service.exception.ErrorCode;
import com.deveria.identity.service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal   // because it's not injected via constructor
    @Value("${jwt.signer.key}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        // So sánh mật khẩu đã mã hóa trong DB với mật khẩu người dùng nhập vào
        boolean authenticated = passwordEncoder.matches((request.getPassword()), user.getPassword());

        // Nếu không khớp, ném ra ngoại lệ
        if(!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Tạo token JWT
        var token = generateToken(user);
        // Trả về token trong response
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }

    public String generateToken(User user){
        // Tạo header với thuật toán HS256
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // Tạo payload với các thông tin cần thiết
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())  // Thông tin về người dùng
                .issuer("danielpc.com") // Thông tin về nhà phát hành token
                .issueTime(new Date()) // Thời gian phát hành token
                .expirationTime(new Date(   // Thời gian hết hạn token
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))   // Thông tin về phạm vi (scope) của token. Cho biết quyền hạn (roles) của user đang sở hữu token này.
                .build();
        // Tạo payload từ JWTClaimsSet
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Tạo JWSObject từ header và payload
        JWSObject jwsObject = new JWSObject(header, payload);

        // Ký JWSObject với khóa bí mật
        try {
            System.out.println("Key length (bytes): " + SIGNER_KEY.getBytes().length);
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            throw new RuntimeException("Can not generate token",e);
        }
    }



    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    public String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" "); // Vì theo chuẩn OAuth2, các scope của 1 user được phân tách bằng dấu cách
        user.getRoles().forEach(stringJoiner::add); // Thêm từng role vào chuỗi scope
        return stringJoiner.toString();
    }
}
