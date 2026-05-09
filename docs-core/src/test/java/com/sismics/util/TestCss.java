package com.sismics.util;

import org.junit.Assert;
import org.junit.Test;

import com.sismics.util.css.Selector;

/**
 * Test of CSS utilities.
 * 
 * @author bgamard
 */
public class TestCss {
    @Test
    public void testBuildCss() {
        Selector selector = new Selector(".test")
            .rule("background-color", "yellow")
            .rule("font-family", "Comic Sans");
        Assert.assertEquals(".test {\n  background-color: yellow;\n  font-family: Comic Sans;\n}\n", selector.toString());
    }
}
