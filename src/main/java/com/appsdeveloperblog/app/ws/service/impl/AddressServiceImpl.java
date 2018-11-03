package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.AddressRepository;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  AddressRepository addressRepository;

  @Autowired
  ModelMapper modelMapper;

  @Override
  public List<AddressDto> getAddressesByUserId(final String userId) {

    final List<AddressDto> returnValue = new ArrayList<AddressDto>();

    final UserEntity userEntity = userRepository.findByUserId(userId);
    if (userEntity == null) {
      return returnValue;
    }

    final Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);

    for (final AddressEntity addressEntity : addresses) {
      returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
    }

    return returnValue;
  }

  @Override
  public AddressDto getAddressByAddressId(final String addressId) {

    AddressDto returnValue = null;

    final AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

    if (addressEntity != null) {
      returnValue = modelMapper.map(addressEntity, AddressDto.class);
    }

    return returnValue;
  }
}
