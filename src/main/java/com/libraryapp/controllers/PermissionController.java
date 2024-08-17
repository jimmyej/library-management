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

import com.libraryapp.dtos.requests.PermissionRequest;
import com.libraryapp.entities.Permission;
import com.libraryapp.services.PermissionService;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    PermissionService permissionService;

    @Autowired
    PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/permissionNames/{permissionName}")
    ResponseEntity<Permission> getPermissionByName(@PathVariable String permissionName) {
        Permission permission = permissionService.getPermissionByName(permissionName);
        if (permission == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(permission);
    }

    @PostMapping("")
    ResponseEntity<Permission> registerPermission(@RequestBody PermissionRequest permission) {

        Permission newPermission = permissionService.registerPermission(permission);
        if (newPermission != null) {
            return new ResponseEntity<>(newPermission, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FOUND);

    }

    @PutMapping("")
    ResponseEntity<Permission> editPermission(PermissionRequest permission) {

        Permission newPermission = permissionService.editPermission(permission);
        if (newPermission == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newPermission);
    }

    @DeleteMapping("/ids/{permissionId}")
    ResponseEntity<Boolean> removePermissionById(@PathVariable int permissionId) {
        boolean removed = permissionService.removePermissionById(permissionId);
        if (!removed) {
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    ResponseEntity<List<Permission>> getPermissionsByIsActivated(String activated){
        List<Permission> permissions = permissionService.getPermissionsByIsActivated(activated);
        if (permissions.isEmpty()) {
            return ResponseEntity.noContent().build();

        }
        return ResponseEntity.ok(permissions);
    }

}
