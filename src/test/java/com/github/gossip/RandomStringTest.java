package com.github.gossip;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.SecureRandom;

@RunWith(PowerMockRunner.class)
public class RandomStringTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  @Rule public final Timeout globalTimeout = new Timeout(10000);

  // Test written by Diffblue Cover.
  @PrepareForTest(RandomString.class)
  @Test
  public void constructorInputNegativeOutputIllegalArgumentException() throws Exception {

    // Arrange
    final int length = -21;
    final SecureRandom secureRandom = PowerMockito.mock(SecureRandom.class);
    PowerMockito.whenNew(SecureRandom.class).withNoArguments().thenReturn(secureRandom);

    // Act, creating object to test constructor
    thrown.expect(IllegalArgumentException.class);
    final RandomString randomString = new RandomString(length);
  }

  // Test written by Diffblue Cover.
  @Test
  public void generateStringInputNegativeOutputIllegalArgumentException() {

    // Act
    thrown.expect(IllegalArgumentException.class);
    RandomString.generateString(-21);

    // The method is not expected to return due to exception thrown
  }
}
