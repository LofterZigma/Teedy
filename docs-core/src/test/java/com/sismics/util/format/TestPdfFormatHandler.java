package com.sismics.util.format;

import com.sismics.BaseTest;
import com.sismics.docs.core.util.format.PdfFormatHandler;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Test of {@link PdfFormatHandler}
 *
 * @author bgamard
 */
public class TestPdfFormatHandler extends BaseTest {
    /**
     * Test related to https://github.com/sismics/docs/issues/373.
     */
    @Test
    public void testIssue373() throws Exception {
        PdfFormatHandler formatHandler = new PdfFormatHandler();
        String content = formatHandler.extractContent("deu", Paths.get(getResource("issue373.pdf").toURI()));
        Assert.assertNotNull(content);

        if (isTesseractAvailable()) {
            Assert.assertTrue(content.contains("Aufrechterhaltung"));
            Assert.assertTrue(content.contains("Außentemperatur"));
            Assert.assertTrue(content.contains("Grundumsatzmessungen"));
            Assert.assertTrue(content.contains("ermitteln"));
        } else {
            Assert.assertTrue(content.trim().isEmpty());
        }
    }

    private boolean isTesseractAvailable() {
        try {
            Process process = new ProcessBuilder("tesseract", "--version").start();
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
