package com.deveria.identity.service.service;

import com.deveria.identity.service.dto.request.UserCreateRequest;
import com.deveria.identity.service.dto.request.UserUpdateRequest;
import com.deveria.identity.service.entity.User;
import com.deveria.identity.service.exception.AppException;
import com.deveria.identity.service.exception.ErrorCode;
import com.deveria.identity.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Tạo mới người dùng
    public User createUser(UserCreateRequest request) {
        User user = new User();

        if(userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("ErrorCode.USER_EXIST");

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userRepository.save(user);
    }

    // Lấy danh sách tất cả người dùng
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // Lấy thông tin người dùng theo ID
    public User getUser(String userId) {
        // findById() trả về Optional<User>
        // → orElseThrow() dùng để "mở" Optional, trả về đối tượng User nếu có,
        // hoặc ném ra RuntimeException nếu không tìm thấy người dùng.
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Cập nhật thông tin người dùng
    public User updateUser(String userId, UserUpdateRequest request) {
        // orElseThrow() giúp tránh NullPointerException khi không tìm thấy User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userRepository.save(user);
    }

    // Xóa người dùng
    public void deleteUser(String userId) {
        // Nếu không có user tương ứng, orElseThrow() sẽ ném lỗi ngay lập tức
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

}
