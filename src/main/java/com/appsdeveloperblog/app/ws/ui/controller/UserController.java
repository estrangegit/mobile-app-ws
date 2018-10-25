package com.appsdeveloperblog.app.ws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {

  @Autowired
  UserService userService;

  @GetMapping
  public String getUser() {
    return "getUser was called";
  }

  @PostMapping
  public UserRest createUser(@RequestBody final UserDetailsRequestModel userDetails) {

    final UserRest returnValue = new UserRest();

    final UserDto userDto = new UserDto();

    BeanUtils.copyProperties(userDetails, userDto);

    final UserDto createUser = userService.createUser(userDto);

    BeanUtils.copyProperties(createUser, returnValue);

    return returnValue;
  }

  @PutMapping
  public String updateUser() {
    return "updateUser was called";
  }

  @DeleteMapping
  public String deleteUser() {
    return "deleteUser was called";
  }
}