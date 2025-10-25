package com.deveria.identity.service.service;

import com.deveria.identity.service.dto.request.UserCreateRequest;
import com.deveria.identity.service.dto.request.UserUpdateRequest;
import com.deveria.identity.service.dto.response.UserResponse;
import com.deveria.identity.service.entity.User;
import com.deveria.identity.service.enums.Role;
import com.deveria.identity.service.exception.AppException;
import com.deveria.identity.service.exception.ErrorCode;
import com.deveria.identity.service.mapper.UserMapper;
import com.deveria.identity.service.repository.UserRepository;
import com.deveria.identity.service.util.LogUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    // Tạo mới người dùng
    public UserResponse createUser(UserCreateRequest request) {
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXIST);

        // Thay vì tự tạo đối tượng User và gán từng trường một,
        // ta sử dụng UserMapper để chuyển đổi từ UserCreateRequest sang User.
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Mặc định gán vai trò USER cho người dùng mới là USER
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.toString());
//        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Lấy danh sách tất cả người dùng
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") // Chỉ cho phép truy cập nếu người dùng có vai trò ADMIN. Sẽ kiểm tra trước khi thực thi method này, nếu không đủ quyền sẽ ném lỗi 403 Forbidden ngay trước khi vào method này.
    // Trong video số 12. thì Devteria dùng là @PreAuthorize("hasRole('ADMIN')") do video trước đó ảnh có customize cái prefix "ROLE_" nên mới dùng hasRole('ADMIN'), còn ở đây mình dùng mặc định của Spring Security nên phải dùng hasAuthority('SCOPE_ADMIN')
    public List<UserResponse> getUsers() {
        LogUtils.logMethodInfo("Fetching all users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).collect(Collectors.toList()); // Sử dụng method reference để chuyển đổi từng User sang UserResponse
    }

    // Lấy thông tin người dùng theo ID
//    @PostAuthorize("hasAuthority('SCOPE_ADMIN')") // Chỉ cho phép truy cập nếu người dùng có vai trò ADMIN. Sẽ kiểm tra sau khi thực thi method này, nếu không đủ quyền sẽ ném lỗi 403 Forbidden ngay sau khi vào method này.
    @PostAuthorize("returnObject.username == authentication.name")  // Chỉ cho phép truy cập nếu username trong UserResponse trùng với tên người dùng đã xác thực. Sẽ kiểm tra sau khi thực thi method này, nếu không đủ quyền sẽ ném lỗi 403 Forbidden ngay sau khi vào method này.
    public UserResponse getUser(String userId) {
        // findById() trả về Optional<User>
        // → orElseThrow() dùng để "mở" Optional, trả về đối tượng User nếu có,
        // hoặc ném ra RuntimeException nếu không tìm thấy người dùng.
        LogUtils.logMethodInfo("Fetching user with id: " + userId);

        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    // Cập nhật thông tin người dùng
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        // orElseThrow() giúp tránh NullPointerException khi không tìm thấy User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Sử dụng UserMapper để cập nhật thông tin người dùng từ UserUpdateRequest
        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Xóa người dùng
    public void deleteUser(String userId) {
        // Nếu không có user tương ứng, orElseThrow() sẽ ném lỗi ngay lập tức
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        return userMapper.toUserResponse(user); // Trả về thông tin người dùng đã được chuyển đổi sang UserResponse
    }

}
