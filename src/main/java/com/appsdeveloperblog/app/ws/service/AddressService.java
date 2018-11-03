package com.appsdeveloperblog.app.ws.service;

import java.util.List;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;

public interface AddressService {
  List<AddressDto> getAddressesByUserId(String userId);

  AddressDto getAddressByAddressId(String addressId);
}
