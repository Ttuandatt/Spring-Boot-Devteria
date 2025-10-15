package com.deveria.identity.service.service;

import com.deveria.identity.service.dto.request.UserCreateRequest;
import com.deveria.identity.service.dto.request.UserUpdateRequest;
import com.deveria.identity.service.dto.response.UserResponse;
import com.deveria.identity.service.entity.User;
import com.deveria.identity.service.exception.AppException;
import com.deveria.identity.service.exception.ErrorCode;
import com.deveria.identity.service.mapper.UserMapper;
import com.deveria.identity.service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    // Tạo mới người dùng
    public UserResponse createUser(UserCreateRequest request) {
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXIST);

        // Thay vì tự tạo đối tượng User và gán từng trường một,
        // ta sử dụng UserMapper để chuyển đổi từ UserCreateRequest sang User.
        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Lấy danh sách tất cả người dùng
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // Lấy thông tin người dùng theo ID
    public UserResponse getUser(String userId) {
        // findById() trả về Optional<User>
        // → orElseThrow() dùng để "mở" Optional, trả về đối tượng User nếu có,
        // hoặc ném ra RuntimeException nếu không tìm thấy người dùng.
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

}
