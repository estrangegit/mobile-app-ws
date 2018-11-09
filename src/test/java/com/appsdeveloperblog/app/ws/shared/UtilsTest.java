package com.appsdeveloperblog.app.ws.shared;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

  @Autowired
  private Utils utils;

  @BeforeEach
  void setUp() throws Exception {}

  @Test
  final void testGenerateUserId() {
    final String userId1 = utils.generateUserId(30);
    final String userId2 = utils.generateUserId(30);

    assertNotNull(userId1);
    assertNotNull(userId2);

    assertTrue(userId1.length() == 30);
    assertTrue(!userId1.equalsIgnoreCase(userId2));
  }

  @Test
  final void testHasTokenNotExpired() {
    final String token = utils.generateEmailVerificationToken("54fdsgfdGG4dfF");
    assertNotNull(token);

    final boolean hasTokenExpired = utils.hasTokenExpired(token);
    assertFalse(hasTokenExpired);
  }

  @Test
  final void testHasTokenExpired() {

    final String token =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJldGllbm5lLmVzdHJhbmdpbkBnbWFpbC5jb20iLCJleHAiOjE1NDE3NTMxNjN9.aTZSA6dPZZ1WP297ndSw-nGsIOmQClibga47Z8pSEpsP_PwFViyUY4_22UasUoksbrddhKxS-d9eaI1u9pzp9A";

    final boolean hasTokenExpired = utils.hasTokenExpired(token);
    assertTrue(hasTokenExpired);
  }
}
