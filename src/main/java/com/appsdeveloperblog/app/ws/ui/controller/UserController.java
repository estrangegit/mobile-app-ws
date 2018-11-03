package com.appsdeveloperblog.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressRest;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {

  @Autowired
  UserService userService;

  @Autowired
  AddressService addressService;

  @Autowired
  ModelMapper modelMapper;

  @GetMapping(path = "/{id}",
      produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public UserRest getUser(@PathVariable final String id) {

    final UserRest returnValue = new UserRest();

    final UserDto userDto = userService.getUserByUserId(id);

    BeanUtils.copyProperties(userDto, returnValue);

    return returnValue;
  }

  @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public UserRest createUser(@RequestBody final UserDetailsRequestModel userDetails)
      throws Exception {

    UserRest returnValue = new UserRest();

    if (userDetails.getFirstName().isEmpty()) {
      throw new NullPointerException("The object is null");
    }

    final UserDto userDto = modelMapper.map(userDetails, UserDto.class);

    final UserDto createUser = userService.createUser(userDto);

    returnValue = modelMapper.map(createUser, UserRest.class);

    return returnValue;
  }

  @PutMapping(path = "/{id}",
      consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public UserRest updateUser(@PathVariable final String id,
      @RequestBody final UserDetailsRequestModel userDetails) {
    final UserRest returnValue = new UserRest();

    final UserDto userDto = new UserDto();

    BeanUtils.copyProperties(userDetails, userDto);

    final UserDto updateUser = userService.updateUser(id, userDto);

    BeanUtils.copyProperties(updateUser, returnValue);

    return returnValue;
  }

  @DeleteMapping(path = "/{id}",
      produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public OperationStatusModel deleteUser(@PathVariable final String id) {

    final OperationStatusModel returnValue = new OperationStatusModel();

    userService.deleteUser(id);

    returnValue.setOperationName(RequestOperationName.DELETE.name());
    returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

    return returnValue;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") final int page,
      @RequestParam(value = "limit", defaultValue = "2") final int limit) {

    final List<UserRest> returnValue = new ArrayList<>();

    final List<UserDto> users = userService.getUsers(page, limit);

    for (final UserDto userDto : users) {
      final UserRest userModel = new UserRest();
      BeanUtils.copyProperties(userDto, userModel);
      returnValue.add(userModel);
    }

    return returnValue;
  }

  // http://localhost:8080/mobile-app-ws/users/{userId}/addresses
  @GetMapping(path = "/{id}/addresses",
      produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public List<AddressRest> getUserAddresses(@PathVariable final String id) {

    List<AddressRest> returnValue = new ArrayList<AddressRest>();

    final List<AddressDto> addressesDto = addressService.getAddressesByUserId(id);

    if ((addressesDto != null) && !addressesDto.isEmpty()) {
      final Type listType = new TypeToken<List<AddressRest>>() {}.getType();
      returnValue = modelMapper.map(addressesDto, listType);
    }

    return returnValue;
  }

  @GetMapping(path = "/{userId}/addresses/{addressId}",
      produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public AddressRest getUserAddress(@PathVariable final String addressId) {

    final AddressDto addressesDto = addressService.getAddressByAddressId(addressId);
    return modelMapper.map(addressesDto, AddressRest.class);
  }
}
