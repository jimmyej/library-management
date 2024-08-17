package com.libraryapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libraryapp.dtos.requests.RolePermissionRequest;
import com.libraryapp.entities.RolePermission;
import com.libraryapp.services.RolePermissionService;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/v1/rolesPermissions")
public class RolePermissionController {

    RolePermissionService rolePermissionService;

    @Autowired
    RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping("/idRoles/{roleId}")
    ResponseEntity<List<RolePermission>> getRolePermissionByRoleId(@PathVariable int roleId) {
        List<RolePermission> rolePermissions = rolePermissionService.getRolePermissionByPermissionId(roleId);
        if (rolePermissions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rolePermissions);
    }

    @GetMapping("/idPermissions/{permissionId}")
    ResponseEntity<List<RolePermission>> getRolePermissionByPermissionId(@PathVariable int permissionId) {
        List<RolePermission> rolePermissions = rolePermissionService.getRolePermissionByPermissionId(permissionId);
        if (rolePermissions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rolePermissions);
    }

    @PostMapping("")
    ResponseEntity<RolePermission> registerRolePermission(RolePermissionRequest rolePermission){
        RolePermission newRolePermission = rolePermissionService.registerRolePermission(rolePermission);
        if (newRolePermission !=null) {
            return new ResponseEntity<>(newRolePermission,HttpStatus.CREATED);
            
        }
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @PutMapping("")
    ResponseEntity<RolePermission> editRolePermission(@RequestBody RolePermissionRequest rolePermission) {
        RolePermission newRolePermission = rolePermissionService.editRolePermission(rolePermission);
        if (newRolePermission == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newRolePermission);
    }

    @DeleteMapping("/ids/{rolePermissionId}")
    ResponseEntity<Boolean> removeRolePermissionById(@PathVariable int rolePermissionId){
    boolean removed = rolePermissionService.removeRolePermissionById(rolePermissionId);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}
