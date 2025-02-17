package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.UserDTO;
import com.alkemy.ong.dto.UserDTORequest;
import com.alkemy.ong.exception.CloudStorageClientException;
import com.alkemy.ong.exception.CorruptedFileException;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.utility.GlobalConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(GlobalConstants.Endpoints.USER)
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping
    public String deleteUser(@RequestParam("id") String id, HttpServletResponse httpServletResponse) {
        try {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return userService.delete(id);
        } catch (NotFoundException e) {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return e.getMessage();
        }
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll(){

        return new ResponseEntity<>(userService.getAll(),HttpStatus.OK);

    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTORequest userDTORequest) throws CloudStorageClientException, CorruptedFileException {
        try {
            return new ResponseEntity<>(userService.updateUser(userDTORequest), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
