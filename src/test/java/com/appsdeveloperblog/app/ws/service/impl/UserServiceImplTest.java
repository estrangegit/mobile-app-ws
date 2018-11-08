package com.appsdeveloperblog.app.ws.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.shared.AmazonSES;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

class UserServiceImplTest {

  @InjectMocks
  UserServiceImpl userService;

  @Mock
  UserRepository userRepository;

  @Mock
  Utils utils;

  @Mock
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Mock
  AmazonSES amazonSES;

  String userId = "kjhHJHkjd8798dsf";
  String encryptedPassword = "jkhrgreg981gz9";
  UserEntity userEntity;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    userEntity = new UserEntity();
    userEntity.setId(1L);
    userEntity.setUserId(userId);
    userEntity.setFirstName("firstName");
    userEntity.setLastName("lastName");
    userEntity.setEncryptedPassword(encryptedPassword);
    userEntity.setEmail("test@test.com");
    userEntity.setEmailVerificationToken("llkjerf454SDF");
    userEntity.setAddresses(this.getAddressesEntity());
  }

  @Test
  final void testGetUser() {
    when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

    final UserDto userDto = userService.getUser("test@test.gmail");

    assertNotNull(userDto);
    assertEquals(userEntity.getFirstName(), userDto.getFirstName());
  }

  @Test
  final void testGetUser_UsernameNotFoundException() {
    when(userRepository.findByEmail(anyString())).thenReturn(null);

    assertThrows(UsernameNotFoundException.class, () -> {
      userService.getUser("test@test.gmail");
    });
  }

  @Test
  final void testCreateUserr_UserServiceException() {
    when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

    final UserDto userDto = new UserDto();
    userDto.setAddresses(getAddressesDto());
    userDto.setFirstName("firstName");
    userDto.setLastName("lastName");
    userDto.setPassword("123");
    userDto.setEmail("test@test.com");

    assertThrows(UserServiceException.class, () -> {
      userService.createUser(userDto);
    });
  }

  @Test
  final void testCreateUser() {
    when(userRepository.findByEmail(anyString())).thenReturn(null);
    when(utils.generateUserId(anyInt())).thenReturn("userId");
    when(utils.generateAddressId(anyInt())).thenReturn("qsdfqsJHKJHd546");
    when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
    when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
    doNothing().when(amazonSES).verifyEmail(any(UserDto.class));

    final UserDto userDto = new UserDto();
    userDto.setAddresses(getAddressesDto());
    userDto.setFirstName("firstName");
    userDto.setLastName("lastName");
    userDto.setPassword("123");
    userDto.setEmail("test@test.com");

    final UserDto storedUserDetails = userService.createUser(userDto);

    assertNotNull(storedUserDetails);
    assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
    assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
    assertNotNull(storedUserDetails.getUserId());
    assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
    verify(utils, times(storedUserDetails.getAddresses().size())).generateAddressId(30);
    verify(bCryptPasswordEncoder, times(1)).encode("123");
    verify(userRepository, times(1)).save(any(UserEntity.class));
  }

  private List<AddressDto> getAddressesDto() {
    final AddressDto shippingAddressDto = new AddressDto();
    shippingAddressDto.setType("shipping");
    shippingAddressDto.setCity("Vancouver");
    shippingAddressDto.setCountry("Canada");
    shippingAddressDto.setPostalCode("ABS123");
    shippingAddressDto.setStreetName("123 Street Name");

    final AddressDto billingAddressDto = new AddressDto();
    billingAddressDto.setType("billing");
    billingAddressDto.setCity("Vancouver");
    billingAddressDto.setCountry("Canada");
    billingAddressDto.setPostalCode("ABS123");
    billingAddressDto.setStreetName("123 Street Name");

    final List<AddressDto> addresses = new ArrayList<>();
    addresses.add(shippingAddressDto);
    addresses.add(billingAddressDto);

    return addresses;
  }

  private List<AddressEntity> getAddressesEntity() {

    final List<AddressDto> addresses = this.getAddressesDto();

    final Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
    return new ModelMapper().map(addresses, listType);
  }
}
