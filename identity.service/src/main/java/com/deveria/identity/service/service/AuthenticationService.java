package com.deveria.identity.service.service;

import ch.qos.logback.classic.Logger;
import com.deveria.identity.service.dto.request.AuthenticationRequest;
import com.deveria.identity.service.dto.request.IntrospectRequest;
import com.deveria.identity.service.dto.request.LogoutRequest;
import com.deveria.identity.service.dto.request.RefreshRequest;
import com.deveria.identity.service.dto.response.AuthenticationResponse;
import com.deveria.identity.service.dto.response.IntrospectResponse;
import com.deveria.identity.service.entity.InvalidatedToken;
import com.deveria.identity.service.entity.User;
import com.deveria.identity.service.exception.AppException;
import com.deveria.identity.service.exception.ErrorCode;
import com.deveria.identity.service.repository.InvalidatedTokenRepository;
import com.deveria.identity.service.repository.UserRepository;
import com.deveria.identity.service.util.LogUtils;
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
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal   // because it's not injected via constructor
    @Value("${jwt.signer.key}")
    protected String SIGNER_KEY;

    @NonFinal   // because it's not injected via constructor
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal   // because it's not injected via constructor
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

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
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString()) // ID duy nhất cho token
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
        boolean isValid = true;

        try{
            verifyToken(token, false);
        }catch (AppException e){
           isValid = false;
        }


        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" "); // Vì theo chuẩn OAuth2, các scope của 1 user được phân tách bằng dấu cách
        if(!CollectionUtils.isEmpty(user.getRoles())) { // Kiểm tra xem user có role không. Nếu role của user không rỗng
            user.getRoles().forEach(role -> { // Duyệt qua từng role của user
                stringJoiner.add("ROLE_" + role.getName()); // Thêm tên role vào chuỗi scope và thêm chuỗi "ROLE_" vào trước tên role để phân biệt role với permission
                if(!CollectionUtils.isEmpty(role.getPermissions())) { // Kiểm tra xem role có permission không. Nếu permission của role không rỗng
                    role.getPermissions().forEach(permission -> {stringJoiner.add(permission.getName());}); // Duyệt qua từng permission của role và thêm tên permission vào chuỗi scope
                }
            });
        }
        return stringJoiner.toString();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try{
            var signedToken = verifyToken(request.getToken(), true);

            String jti = signedToken.getJWTClaimsSet().getJWTID();
            Date expirationTime = signedToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti)
                    .expirationTime(expirationTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        }catch (AppException e){
            LogUtils.logMethodInfo("Token already invalidated or expired.");
        }
    }

    // Hàm xác thực token
    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        // Tạo bộ xác thực JWS với khóa bí mật
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        // Kiểm tra điều kiện hết hạn token
        Date expiryTime = (isRefresh) // nếu là làm mới token
                ? new Date (signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()) // Thời gian hết hạn bằng thời gian phát hành + thời gian làm mới
                : signedJWT.getJWTClaimsSet().getExpirationTime(); // Ngược lại, thời gian hết hạn bằng thời gian hết hạn trong token

        var verified = signedJWT.verify(verifier);


        // Nếu token không hợp lệ hoặc đã hết hạn, ném ra ngoại lệ là user chưa được xác thực
        if(!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Nếu token đã bị thu hồi hoặc user giữ token này logout trước khi nó expire, ném ra ngoại lệ là user chưa được xác thực
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }


        return signedJWT;
    }

    // Hàm làm mới token
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        // Xác thực token hiện tại xem còn hợp lệ không
        var signedJWT = verifyToken(request.getToken(), true);

        // Lấy thông tin token hiện tại
        var jti = signedJWT.getJWTClaimsSet().getJWTID();
        var expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // Invalidate (thu hồi) token hiện tại bằng cách lưu jti của nó vào bảng invalidated_tokens
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expirationTime(expirationTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);

        // Lấy username từ token hiện tại
        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // Tạo token mới
        var newToken = generateToken(user);
        return AuthenticationResponse.builder()
                .token(newToken)
                .authenticated(true)
                .build();


    }
}
