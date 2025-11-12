package com.deveria.identity.service.mapper;

import org.mapstruct.Mapper;

import com.deveria.identity.service.dto.request.PermissionRequest;
import com.deveria.identity.service.dto.response.PermissionResponse;
import com.deveria.identity.service.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
