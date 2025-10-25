package com.deveria.identity.service.service;

import com.deveria.identity.service.dto.request.PermissionRequest;
import com.deveria.identity.service.dto.response.PermissionResponse;
import com.deveria.identity.service.entity.Permission;
import com.deveria.identity.service.mapper.PermissionMapper;
import com.deveria.identity.service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    // Create a new permission
    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    // Get all permissions
    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList(); // Convert to PermissionResponse if needed
    }

    // Delete a permission by name
    public void delete(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }
}
