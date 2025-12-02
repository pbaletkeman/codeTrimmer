package com.codetrimmer.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ColorOutputTest {

  @Test
  void testColorOutputWithColorEnabled() {
    ColorOutput colorOutput = new ColorOutput(true);

    String successMessage = colorOutput.success("Success");
    assertTrue(successMessage.contains(ColorOutput.GREEN));
    assertTrue(successMessage.contains("Success"));
    assertTrue(successMessage.contains(ColorOutput.RESET));
  }

  @Test
  void testColorOutputWithColorDisabled() {
    ColorOutput colorOutput = new ColorOutput(false);

    String successMessage = colorOutput.success("Success");
    assertEquals("Success", successMessage);
    assertFalse(successMessage.contains(ColorOutput.GREEN));
    assertFalse(successMessage.contains(ColorOutput.RESET));
  }

  @Test
  void testSuccessMethodWithColor() {
    ColorOutput colorOutput = new ColorOutput(true);
    String result = colorOutput.success("All tests passed");

    assertEquals(ColorOutput.GREEN + "All tests passed" + ColorOutput.RESET, result);
  }

  @Test
  void testSuccessMethodWithoutColor() {
    ColorOutput colorOutput = new ColorOutput(false);
    String result = colorOutput.success("All tests passed");

    assertEquals("All tests passed", result);
  }

  @Test
  void testErrorMethodWithColor() {
    ColorOutput colorOutput = new ColorOutput(true);
    String result = colorOutput.error("Error occurred");

    assertEquals(ColorOutput.RED + "Error occurred" + ColorOutput.RESET, result);
  }

  @Test
  void testErrorMethodWithoutColor() {
    ColorOutput colorOutput = new ColorOutput(false);
    String result = colorOutput.error("Error occurred");

    assertEquals("Error occurred", result);
  }

  @Test
  void testWarningMethodWithColor() {
    ColorOutput colorOutput = new ColorOutput(true);
    String result = colorOutput.warning("Warning message");

    assertEquals(ColorOutput.YELLOW + "Warning message" + ColorOutput.RESET, result);
  }

  @Test
  void testWarningMethodWithoutColor() {
    ColorOutput colorOutput = new ColorOutput(false);
    String result = colorOutput.warning("Warning message");

    assertEquals("Warning message", result);
  }

  @Test
  void testInfoMethodWithColor() {
    ColorOutput colorOutput = new ColorOutput(true);
    String result = colorOutput.info("Info message");

    assertEquals(ColorOutput.CYAN + "Info message" + ColorOutput.RESET, result);
  }

  @Test
  void testInfoMethodWithoutColor() {
    ColorOutput colorOutput = new ColorOutput(false);
    String result = colorOutput.info("Info message");

    assertEquals("Info message", result);
  }

  @Test
  void testDebugMethodWithColor() {
    ColorOutput colorOutput = new ColorOutput(true);
    String result = colorOutput.debug("Debug message");

    assertEquals(ColorOutput.BLUE + "Debug message" + ColorOutput.RESET, result);
  }

  @Test
  void testDebugMethodWithoutColor() {
    ColorOutput colorOutput = new ColorOutput(false);
    String result = colorOutput.debug("Debug message");

    assertEquals("Debug message", result);
  }

  @Test
  void testColorConstantsNotNull() {
    assertNotNull(ColorOutput.RESET);
    assertNotNull(ColorOutput.RED);
    assertNotNull(ColorOutput.GREEN);
    assertNotNull(ColorOutput.YELLOW);
    assertNotNull(ColorOutput.BLUE);
    assertNotNull(ColorOutput.CYAN);
  }

  @Test
  void testColorConstantsContainAnsiSequence() {
    assertTrue(ColorOutput.RED.contains("\u001B"));
    assertTrue(ColorOutput.GREEN.contains("\u001B"));
    assertTrue(ColorOutput.YELLOW.contains("\u001B"));
    assertTrue(ColorOutput.BLUE.contains("\u001B"));
    assertTrue(ColorOutput.CYAN.contains("\u001B"));
  }

  @Test
  void testResetConstant() {
    assertEquals("\u001B[0m", ColorOutput.RESET);
  }

  @Test
  void testRedConstant() {
    assertEquals("\u001B[31m", ColorOutput.RED);
  }

  @Test
  void testGreenConstant() {
    assertEquals("\u001B[32m", ColorOutput.GREEN);
  }

  @Test
  void testYellowConstant() {
    assertEquals("\u001B[33m", ColorOutput.YELLOW);
  }

  @Test
  void testBlueConstant() {
    assertEquals("\u001B[34m", ColorOutput.BLUE);
  }

  @Test
  void testCyanConstant() {
    assertEquals("\u001B[36m", ColorOutput.CYAN);
  }

  @Test
  void testSuccessWithEmptyString() {
    ColorOutput colorOutput = new ColorOutput(true);
    String result = colorOutput.success("");

    assertEquals(ColorOutput.GREEN + "" + ColorOutput.RESET, result);
  }

  @Test
  void testSuccessWithSpecialCharacters() {
    ColorOutput colorOutput = new ColorOutput(true);
    String message = "Test: @#$%^&*()";
    String result = colorOutput.success(message);

    assertEquals(ColorOutput.GREEN + message + ColorOutput.RESET, result);
  }

  @Test
  void testSuccessWithUnicode() {
    ColorOutput colorOutput = new ColorOutput(true);
    String message = "Test: 你好世界 🌍";
    String result = colorOutput.success(message);

    assertEquals(ColorOutput.GREEN + message + ColorOutput.RESET, result);
  }

  @Test
  void testSuccessWithNewlines() {
    ColorOutput colorOutput = new ColorOutput(true);
    String message = "Line1\nLine2";
    String result = colorOutput.success(message);

    assertEquals(ColorOutput.GREEN + message + ColorOutput.RESET, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"Success", "Error", "Warning", "Info", "Debug"})
  void testAllColorMethods(String message) {
    ColorOutput colorOutput = new ColorOutput(true);

    String success = colorOutput.success(message);
    String error = colorOutput.error(message);
    String warning = colorOutput.warning(message);
    String info = colorOutput.info(message);
    String debug = colorOutput.debug(message);

    assertTrue(success.contains(message));
    assertTrue(error.contains(message));
    assertTrue(warning.contains(message));
    assertTrue(info.contains(message));
    assertTrue(debug.contains(message));
  }

  @Test
  void testIsTerminalMethodExists() {
    // Test that isTerminal method can be called without throwing exception
    boolean result = ColorOutput.isTerminal();
    assertNotNull(result);
  }

  @Test
  void testMultipleInstances() {
    ColorOutput colorWithColor = new ColorOutput(true);
    ColorOutput colorWithoutColor = new ColorOutput(false);

    String message = "Test";
    String withColor = colorWithColor.success(message);
    String withoutColor = colorWithoutColor.success(message);

    assertNotEquals(withColor, withoutColor);
    assertTrue(withColor.contains(ColorOutput.GREEN));
    assertFalse(withoutColor.contains(ColorOutput.GREEN));
  }

  @Test
  void testErrorMessageLength() {
    ColorOutput colorOutput = new ColorOutput(true);
    String message = "Error";
    String result = colorOutput.error(message);

    // With color codes, the result should be longer than the original message
    assertTrue(result.length() > message.length());
  }

  @Test
  void testNoColorMessageLength() {
    ColorOutput colorOutput = new ColorOutput(false);
    String message = "Error";
    String result = colorOutput.error(message);

    // Without color codes, the result should be the same as the original message
    assertEquals(result.length(), message.length());
  }

  @Test
  void testAllMethodsReturnString() {
    ColorOutput colorOutput = new ColorOutput(true);

    assertNotNull(colorOutput.success("test"));
    assertNotNull(colorOutput.error("test"));
    assertNotNull(colorOutput.warning("test"));
    assertNotNull(colorOutput.info("test"));
    assertNotNull(colorOutput.debug("test"));
  }

  @Test
  void testAllMethodsReturnStringWithoutColor() {
    ColorOutput colorOutput = new ColorOutput(false);

    assertNotNull(colorOutput.success("test"));
    assertNotNull(colorOutput.error("test"));
    assertNotNull(colorOutput.warning("test"));
    assertNotNull(colorOutput.info("test"));
    assertNotNull(colorOutput.debug("test"));
  }
}
