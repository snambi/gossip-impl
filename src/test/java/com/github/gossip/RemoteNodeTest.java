package com.github.gossip;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.List;

public class RemoteNodeTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  @Rule public final Timeout globalTimeout = new Timeout(10000);

  // Test written by Diffblue Cover.

  @Test
  public void constructorInputNotNullNegativeOutputVoid() {

    // Arrange
    final String host = "foo";
    final int port = -999_998;

    // Act, creating object to test constructor
    final RemoteNode remoteNode = new RemoteNode(host, port);

    // Assert side effects
    Assert.assertEquals("foo", remoteNode.getHost());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getHostOutputNotNull() {

    // Arrange
    final RemoteNode remoteNode = new RemoteNode("/", 2);

    // Act and Assert result
    Assert.assertEquals("/", remoteNode.getHost());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getNameOutputNull() {

    // Arrange
    final RemoteNode remoteNode = new RemoteNode("/", 2);

    // Act and Assert result
    Assert.assertNull(remoteNode.getName());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getPortOutputPositive() {

    // Arrange
    final RemoteNode remoteNode = new RemoteNode("/", 2);

    // Act and Assert result
    Assert.assertEquals(2, remoteNode.getPort());
  }

  // Test written by Diffblue Cover.
  @Test
  public void parseMultiHostsInputNotNullOutput0() {

    // Act
    final List<RemoteNode> actual = RemoteNode.parseMultiHosts("");

    // Assert result
    final ArrayList<RemoteNode> arrayList = new ArrayList<RemoteNode>();
    Assert.assertEquals(arrayList, actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void parseMultiHostsInputNotNullOutput1() {

    // Arrange
    final String data = "????????????????????????????????????????????????????????????????";

    // Act
    final List<RemoteNode> actual = RemoteNode.parseMultiHosts(data);

    // Assert result
    Assert.assertNotNull(actual);
    Assert.assertEquals(1, actual.size());
    Assert.assertNotNull(actual.get(0));
    Assert.assertEquals(9002, actual.get(0).getPort());
    Assert.assertNull(actual.get(0).getName());
    Assert.assertEquals("????????????????????????????????????????????????????????????????",
                        actual.get(0).getHost());
  }

  // Test written by Diffblue Cover.
  @Test
  public void parseMultiHostsInputNotNullOutput02() {

    // Act
    final List<RemoteNode> actual = RemoteNode.parseMultiHosts(
        "\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d\uf62d,");

    // Assert result
    final ArrayList<RemoteNode> arrayList = new ArrayList<RemoteNode>();
    Assert.assertEquals(arrayList, actual);
  }

  // Test written by Diffblue Cover.
  @Test
  public void parseMultiHostsInputNullOutput0() {

    // Act
    final List<RemoteNode> actual = RemoteNode.parseMultiHosts(null);

    // Assert result
    final ArrayList<RemoteNode> arrayList = new ArrayList<RemoteNode>();
    Assert.assertEquals(arrayList, actual);
  }

  // Test written by Diffblue Cover.
  @Test
  public void parseStrInputNotNullOutputNotNull() {

    // Act
    final RemoteNode actual = RemoteNode.parseStr("1216");

    // Assert result
    Assert.assertNotNull(actual);
    Assert.assertEquals(9002, actual.getPort());
    Assert.assertNull(actual.getName());
    Assert.assertEquals("1216", actual.getHost());
  }

  // Test written by Diffblue Cover.

  @Test
  public void parseStrInputNotNullOutputNotNull2() {

    // Arrange
    final String remoteNodeStr = "3:12";

    // Act
    final RemoteNode actual = RemoteNode.parseStr(remoteNodeStr);

    // Assert result
    Assert.assertNotNull(actual);
    Assert.assertEquals(12, actual.getPort());
    Assert.assertNull(actual.getName());
    Assert.assertEquals("3", actual.getHost());
  }

  // Test written by Diffblue Cover.

  @Test
  public void parseStrInputNotNullOutputNull() {

    // Arrange
    final String remoteNodeStr = "9:::";

    // Act
    final RemoteNode actual = RemoteNode.parseStr(remoteNodeStr);

    // Assert result
    Assert.assertNull(actual);
  }

  // Test written by Diffblue Cover.
  @Test
  public void parseStrInputNotNullOutputNull2() {

    // Act and Assert result
    Assert.assertNull(RemoteNode.parseStr(""));
  }

  // Test written by Diffblue Cover.
  @Test
  public void parseStrInputNullOutputNull() {

    // Act and Assert result
    Assert.assertNull(RemoteNode.parseStr(null));
  }

  // Test written by Diffblue Cover.
  @Test
  public void setNameInputNotNullOutputVoid() {

    // Arrange
    final RemoteNode remoteNode = new RemoteNode("/", 2);

    // Act
    remoteNode.setName("foo");

    // Assert side effects
    Assert.assertEquals("foo", remoteNode.getName());
  }
}
