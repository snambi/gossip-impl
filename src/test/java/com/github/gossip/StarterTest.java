package com.github.gossip;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

public class StarterTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  @Rule public final Timeout globalTimeout = new Timeout(10000);

  // Test written by Diffblue Cover.
  @Test
  public void getMessageRouterOutputNull() {

    // Act and Assert result
    Assert.assertNull(Starter.getMessageRouter());
  }

  // Test written by Diffblue Cover.
  @Test
  public void isDebugOutputFalse() {

    // Act and Assert result
    Assert.assertFalse(Starter.isDebug());
  }
}
