package com.appsdeveloperblog.app.ws.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

class UserControllerTest {

  @InjectMocks
  UserController userController;

  @Mock
  UserService mockUserService;

  UserDto userDto;
  final String USER_ID = "qsdfdqf5454DFGd";

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    userDto = new UserDto();
    userDto.setFirstName("firstName");
    userDto.setLastName("lastName");
    userDto.setEmail("test@test.com");
    userDto.setEmailVerificationStatus(Boolean.FALSE);
    userDto.setEmailVerificationToken(null);
    userDto.setUserId(USER_ID);
    userDto.setAddresses(getAddressesDto());
    userDto.setEncryptedPassword("fdg54Sgh5DFG");
  }

  @Test
  final void testGetUser() {
    when(mockUserService.getUserByUserId(anyString())).thenReturn(userDto);

    final UserRest userRest = userController.getUser(USER_ID);

    assertNotNull(userRest);
    assertEquals(USER_ID, userRest.getUserId());
    assertEquals(userDto.getFirstName(), userRest.getFirstName());
    assertEquals(userDto.getLastName(), userRest.getLastName());
    assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
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
}
