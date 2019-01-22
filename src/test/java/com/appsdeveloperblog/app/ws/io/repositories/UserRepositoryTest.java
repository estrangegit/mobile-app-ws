package com.appsdeveloperblog.app.ws.io.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  static boolean recordsCreated = false;

  @BeforeEach
  void setUp() throws Exception {
    if (!recordsCreated) {
      createRecords();
    }
  }

  @Test
  void testGetVerifiedUsers() {
    Pageable pageableRequest = PageRequest.of(0, 1);
    Page<UserEntity> page = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);

    assertNotNull(page);

    List<UserEntity> userEntities = page.getContent();
    assertNotNull(userEntities);
    assertTrue(userEntities.size() == 1);
  }

  @Test
  final void testFindUserByFirstName() {
    String firstName = "Etienne";
    List<UserEntity> users = userRepository.findUserByFirstName(firstName);

    assertNotNull(users);
    assertTrue(users.size() == 2);

    UserEntity user = users.get(0);
    assertTrue(user.getFirstName().equals(firstName));
  }

  @Test
  final void testFindUserByLastName() {
    String lastName = "Estrangin";
    List<UserEntity> users = userRepository.findUserByLastName(lastName);

    assertNotNull(users);
    assertTrue(users.size() == 2);

    UserEntity user = users.get(0);
    assertTrue(user.getLastName().equals(lastName));
  }

  @Test
  final void testFindUserByKeyword() {
    String keyword = "Est";
    List<UserEntity> users = userRepository.findUserByKeyword(keyword);

    assertNotNull(users);
    assertTrue(users.size() == 2);

    UserEntity user = users.get(0);
    assertTrue(user.getLastName().contains(keyword) || user.getFirstName().contains(keyword));
  }

  @Test
  final void testFindUserFirstNameAndLastNameByKeyword() {
    String keyword = "Est";
    List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);

    assertNotNull(users);
    assertTrue(users.size() == 2);

    Object[] user = users.get(0);

    assertTrue(user.length == 2);

    String userFirstName = String.valueOf(user[0]);
    String userLastName = String.valueOf(user[1]);

    assertNotNull(userFirstName);
    assertNotNull(userLastName);
  }

  @Test
  final void testUpdateUserEmailVerificationStatus() {
    boolean newEmailVerificationStatus = false;
    String userId = "1Hjeu8";

    userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, userId);

    UserEntity storedUserDetails = userRepository.findByUserId(userId);

    boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();

    assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
  }

  @Test
  final void testFindUserEntityByUserId() {
    String userId = "1Hjeu8";
    UserEntity userEntity = userRepository.findUserEntityByUserId(userId);

    assertNotNull(userEntity);
    assertTrue(userEntity.getUserId().equals("1Hjeu8"));

  }

  private void createRecords() {
    // Prepare User Entity
    UserEntity userEntity = new UserEntity();
    userEntity.setFirstName("Etienne");
    userEntity.setLastName("Estrangin");
    userEntity.setUserId("1Hjeu8");
    userEntity.setEncryptedPassword("xxx");
    userEntity.setEmail("test@test.com");
    userEntity.setEmailVerificationStatus(true);

    // Prepare User Addresses
    AddressEntity shippingAddressEntity = new AddressEntity();
    shippingAddressEntity.setType("shipping");
    shippingAddressEntity.setAddressId("adjsn7UlO");
    shippingAddressEntity.setCity("Grenoble");
    shippingAddressEntity.setCountry("France");
    shippingAddressEntity.setPostalCode("38000");
    shippingAddressEntity.setStreetName("3 Rue Lesdiguières");

    AddressEntity billingAddressEntity = new AddressEntity();
    billingAddressEntity.setType("shipping");
    billingAddressEntity.setAddressId("adjsn7UlO");
    billingAddressEntity.setCity("Grenoble");
    billingAddressEntity.setCountry("France");
    billingAddressEntity.setPostalCode("38000");
    billingAddressEntity.setStreetName("3 Rue Lesdiguières");

    List<AddressEntity> addresses = new ArrayList<>();
    addresses.add(shippingAddressEntity);
    addresses.add(billingAddressEntity);

    userEntity.setAddresses(addresses);

    userRepository.save(userEntity);

    // Prepare User Entity
    UserEntity userEntity2 = new UserEntity();
    userEntity2.setFirstName("Etienne");
    userEntity2.setLastName("Estrangin");
    userEntity2.setUserId("1Hjeu82");
    userEntity2.setEncryptedPassword("xxx");
    userEntity2.setEmail("test@test.com");
    userEntity2.setEmailVerificationStatus(true);

    // Prepare User Addresses
    AddressEntity shippingAddressEntity2 = new AddressEntity();
    shippingAddressEntity2.setType("shipping");
    shippingAddressEntity2.setAddressId("adjsn7UlO2");
    shippingAddressEntity2.setCity("Grenoble");
    shippingAddressEntity2.setCountry("France");
    shippingAddressEntity2.setPostalCode("38000");
    shippingAddressEntity2.setStreetName("3 Rue Lesdiguières");

    AddressEntity billingAddressEntity2 = new AddressEntity();
    billingAddressEntity2.setType("shipping");
    billingAddressEntity2.setAddressId("adjsn7UlO2");
    billingAddressEntity2.setCity("Grenoble");
    billingAddressEntity2.setCountry("France");
    billingAddressEntity2.setPostalCode("38000");
    billingAddressEntity2.setStreetName("3 Rue Lesdiguières");

    List<AddressEntity> addresses2 = new ArrayList<>();
    addresses2.add(shippingAddressEntity2);
    addresses2.add(billingAddressEntity2);

    userEntity2.setAddresses(addresses2);

    userRepository.save(userEntity2);

    recordsCreated = true;
  }
}
