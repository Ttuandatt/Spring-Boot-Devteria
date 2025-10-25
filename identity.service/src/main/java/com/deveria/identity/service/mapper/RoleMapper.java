package com.deveria.identity.service.mapper;

import com.deveria.identity.service.dto.request.RoleRequest;
import com.deveria.identity.service.dto.response.RoleResponse;
import com.deveria.identity.service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true) // Vì ở RoleRequest thì permissions là Set<String>, còn khi mapping sang Role thì cần Set<Permission>. Nên để đơn giản ta ignore trường này.
    Role toRole(RoleRequest roleRequest);
    RoleResponse toRoleResponse(Role role);
}
