package com.deveria.identity.service.mapper;

import com.deveria.identity.service.dto.request.PermissionRequest;
import com.deveria.identity.service.dto.response.PermissionResponse;
import com.deveria.identity.service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
