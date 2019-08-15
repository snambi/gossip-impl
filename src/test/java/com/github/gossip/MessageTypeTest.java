package com.github.gossip;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

public class MessageTypeTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  @Rule public final Timeout globalTimeout = new Timeout(10000);

  // Test written by Diffblue Cover.
  @Test
  public void valueOfInputNotNullOutputIllegalArgumentException() {

    // Act
    thrown.expect(IllegalArgumentException.class);
    MessageType.valueOf("/");

    // The method is not expected to return due to exception thrown
  }
}
