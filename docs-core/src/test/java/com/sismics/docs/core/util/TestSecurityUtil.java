package com.sismics.docs.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Test of the security utilities.
 *
 * @author Codex
 */
public class TestSecurityUtil {

    @Test
    public void skipAclCheckTest() {
        Assert.assertTrue(SecurityUtil.skipAclCheck(Collections.singletonList("admin")));
        Assert.assertTrue(SecurityUtil.skipAclCheck(Collections.singletonList("administrators")));
        Assert.assertFalse(SecurityUtil.skipAclCheck(Collections.singletonList("user1")));
        Assert.assertFalse(SecurityUtil.skipAclCheck(Arrays.asList("user1", "user2")));
    }
}
