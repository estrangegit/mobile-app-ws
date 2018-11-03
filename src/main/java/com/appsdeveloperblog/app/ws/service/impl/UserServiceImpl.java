package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  Utils utils;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public UserDto createUser(final UserDto user) {

    if (userRepository.findByEmail(user.getEmail()) != null) {
      throw new RuntimeException("Record already exists");
    }

    for (final AddressDto addressDto : user.getAddresses()) {
      addressDto.setUserDetails(user);
      addressDto.setAddressId(utils.generateAddressId(30));
    }

    final ModelMapper modelMapper = new ModelMapper();
    final UserEntity userEntity = modelMapper.map(user, UserEntity.class);

    final String publicUserId = utils.generateUserId(30);
    userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userEntity.setUserId(publicUserId);

    final UserEntity storedUserDetails = userRepository.save(userEntity);

    final UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);

    return returnValue;
  }

  @Override
  public UserDto getUser(final String email) {
    final UserEntity userEntity = userRepository.findByEmail(email);

    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }

    final UserDto returnValue = new UserDto();
    BeanUtils.copyProperties(userEntity, returnValue);
    return returnValue;
  }

  @Override
  public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
    final UserEntity userEntity = userRepository.findByEmail(email);

    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }

    return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
  }

  @Override
  public UserDto getUserByUserId(final String userId) {

    final UserEntity userEntity = userRepository.findByUserId(userId);

    if (userEntity == null) {
      throw new UsernameNotFoundException("User with ID: " + userId + " not found");
    }

    final UserDto returnValue = new UserDto();
    BeanUtils.copyProperties(userEntity, returnValue);
    return returnValue;
  }

  @Override
  public UserDto updateUser(final String id, final UserDto user) {

    final UserDto returnValue = new UserDto();
    final UserEntity userEntity = userRepository.findByUserId(id);

    if (userEntity == null) {
      throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
    }

    userEntity.setFirstName(user.getFirstName());
    userEntity.setLastName(user.getLastName());

    final UserEntity updatedUserDetails = userRepository.save(userEntity);

    BeanUtils.copyProperties(updatedUserDetails, returnValue);

    return returnValue;
  }

  @Override
  public void deleteUser(final String id) {

    final UserDto returnValue = new UserDto();
    final UserEntity userEntity = userRepository.findByUserId(id);

    if (userEntity == null) {
      throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
    }

    userRepository.delete(userEntity);
  }

  @Override
  public List<UserDto> getUsers(int page, final int limit) {

    final List<UserDto> returnValue = new ArrayList<>();

    if (page > 0) {
      page = page - 1;
    }

    final Pageable pageableRequest = PageRequest.of(page, limit);

    final Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);

    final List<UserEntity> users = usersPage.getContent();

    for (final UserEntity userEntity : users) {
      final UserDto userDto = new UserDto();
      BeanUtils.copyProperties(userEntity, userDto);
      returnValue.add(userDto);
    }

    return returnValue;
  }
}
