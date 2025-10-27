package com.deveria.identity.service.mapper;

import com.deveria.identity.service.dto.request.UserCreateRequest;
import com.deveria.identity.service.dto.request.UserUpdateRequest;
import com.deveria.identity.service.dto.response.UserResponse;
import com.deveria.identity.service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") // This annotation indicates that this interface is a MapStruct mapper and should be managed by Spring
public interface UserMapper {
    User toUser(UserCreateRequest request); // Method to map UserCreateRequest to User entity
    UserResponse toUserResponse(User user); // Method to map User entity to UserResponse DTO
    @Mapping(target = "roles", ignore = true) // Ignore roles field during update
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
