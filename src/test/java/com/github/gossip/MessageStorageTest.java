package com.github.gossip;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

public class MessageStorageTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  @Rule public final Timeout globalTimeout = new Timeout(10000);

  // Test written by Diffblue Cover.
  @Test
  public void addInputNotNullOutputNullPointerException() {

    // Arrange
    final MessageStorage messageStorage = new MessageStorage();
    final Message message = new Message();

    // Act
    thrown.expect(NullPointerException.class);
    messageStorage.add(message);

    // The method is not expected to return due to exception thrown
  }

  // Test written by Diffblue Cover.
  @Test
  public void containsMessageInputNotNullOutputNullPointerException() {

    // Arrange
    final MessageStorage messageStorage = new MessageStorage();
    final Message message = new Message();

    // Act
    thrown.expect(NullPointerException.class);
    messageStorage.containsMessage(message);

    // The method is not expected to return due to exception thrown
  }
}
