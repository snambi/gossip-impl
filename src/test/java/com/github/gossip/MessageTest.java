package com.github.gossip;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.List;

public class MessageTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  @Rule public final Timeout globalTimeout = new Timeout(10000);

  // Test written by Diffblue Cover.

  @Test
  public void constructorOutputVoid() {

    // Act, creating object to test constructor
    final Message message = new Message();

    // Assert side effects
    final ArrayList<String> arrayList = new ArrayList<String>();
    Assert.assertEquals(arrayList, message.getReceivers());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getContentOutputNull() {

    // Arrange
    final Message message = new Message();

    // Act and Assert result
    Assert.assertNull(message.getContent());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getIdOutputNull() {

    // Arrange
    final Message message = new Message();

    // Act and Assert result
    Assert.assertNull(message.getId());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getInetAddressOutputNull() {

    // Arrange
    final Message message = new Message();

    // Act and Assert result
    Assert.assertNull(message.getInetAddress());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getMessageTypeOutputNotNull() {

    // Arrange
    final Message message = new Message();

    // Act
    final MessageType actual = message.getMessageType();

    // Assert result
    Assert.assertEquals(MessageType.NORMAL, actual);
  }

  // Test written by Diffblue Cover.
  @Test
  public void getReceiversOutput0() {

    // Arrange
    final Message message = new Message();

    // Act
    final List<String> actual = message.getReceivers();

    // Assert result
    final ArrayList<String> arrayList = new ArrayList<String>();
    Assert.assertEquals(arrayList, actual);
  }

  // Test written by Diffblue Cover.
  @Test
  public void getSenderOutputNull() {

    // Arrange
    final Message message = new Message();

    // Act and Assert result
    Assert.assertNull(message.getSender());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getTimestampOutputNull() {

    // Arrange
    final Message message = new Message();

    // Act and Assert result
    Assert.assertNull(message.getTimestamp());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setContentInputNotNullOutputVoid() {

    // Arrange
    final Message message = new Message();

    // Act
    message.setContent("/");

    // Assert side effects
    Assert.assertEquals("/", message.getContent());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setIdInputNotNullOutputVoid() {

    // Arrange
    final Message message = new Message();

    // Act
    message.setId("/");

    // Assert side effects
    Assert.assertEquals("/", message.getId());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setMessageTypeInputNotNullOutputVoid() {

    // Arrange
    final Message message = new Message();
    final MessageType messageType = MessageType.LAST;

    // Act
    message.setMessageType(messageType);

    // Assert side effects
    Assert.assertEquals(MessageType.LAST, message.getMessageType());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setReceiversInputNotNullOutputVoid() {

    // Arrange
    final Message message = new Message();
    final ArrayList<String> receivers = new ArrayList<String>();

    // Act
    message.setReceivers(receivers);

    // Assert side effects
    final ArrayList<String> arrayList = new ArrayList<String>();
    Assert.assertEquals(arrayList, message.getReceivers());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setSenderInputNotNullOutputVoid() {

    // Arrange
    final Message message = new Message();

    // Act
    message.setSender("/");

    // Assert side effects
    Assert.assertEquals("/", message.getSender());
  }
}
