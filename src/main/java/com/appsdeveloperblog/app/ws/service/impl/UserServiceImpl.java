package com.appsdeveloperblog.app.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.appsdeveloperblog.app.ws.UserRepository;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

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

    final UserEntity userEntity = new UserEntity();

    BeanUtils.copyProperties(user, userEntity);

    final String publicUserId = utils.generatedUserId(30);
    userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userEntity.setUserId(publicUserId);

    final UserEntity storedUserDetails = userRepository.save(userEntity);


    final UserDto returnValue = new UserDto();

    BeanUtils.copyProperties(storedUserDetails, returnValue);

    return returnValue;
  }

  @Override
  public UserDetails loadUserByUsername(final String arg0) throws UsernameNotFoundException {
    // TODO Auto-generated method stub
    return null;
  }
}
