package com.codetrimmer.model;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BinaryFileDetectorTest {

  @TempDir Path tempDir;

  @Test
  void testIsBinaryWithTextFile() throws Exception {
    Path textFile = tempDir.resolve("test.txt");
    Files.write(textFile, "Hello, World!".getBytes());

    assertFalse(BinaryFileDetector.isBinary(textFile));
  }

  @Test
  void testIsBinaryWithBinaryFile() throws Exception {
    Path binaryFile = tempDir.resolve("test.bin");
    byte[] binaryContent = {0x00, 0x01, 0x02, 0x03};
    Files.write(binaryFile, binaryContent);

    assertTrue(BinaryFileDetector.isBinary(binaryFile));
  }

  @Test
  void testIsBinaryWithNullByteInMiddle() throws Exception {
    Path file = tempDir.resolve("mixed.bin");
    byte[] content = "Hello".getBytes();
    byte[] binaryContent = new byte[content.length + 5];
    System.arraycopy(content, 0, binaryContent, 0, content.length);
    binaryContent[content.length] = 0x00; // Null byte
    Files.write(file, binaryContent);

    assertTrue(BinaryFileDetector.isBinary(file));
  }

  @Test
  void testIsBinaryWithUTF8Text() throws Exception {
    Path textFile = tempDir.resolve("utf8.txt");
    Files.write(textFile, "Hello, 世界! 🌍".getBytes());

    assertFalse(BinaryFileDetector.isBinary(textFile));
  }

  @Test
  void testIsBinaryWithEmptyFile() throws Exception {
    Path emptyFile = tempDir.resolve("empty.txt");
    Files.createFile(emptyFile);

    assertFalse(BinaryFileDetector.isBinary(emptyFile));
  }

  @Test
  void testIsBinaryWithMultipleNullBytes() throws Exception {
    Path file = tempDir.resolve("test.bin");
    byte[] content = {0x00, 0x00, 0x00, (byte) 0xFF};
    Files.write(file, content);

    assertTrue(BinaryFileDetector.isBinary(file));
  }

  @Test
  void testIsBinaryWithLargeTextFile() throws Exception {
    Path largeFile = tempDir.resolve("large.txt");
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10000; i++) {
      sb.append("This is line ").append(i).append("\n");
    }
    Files.write(largeFile, sb.toString().getBytes());

    assertFalse(BinaryFileDetector.isBinary(largeFile));
  }

  @Test
  void testIsBinaryWithSpecialCharacters() throws Exception {
    Path file = tempDir.resolve("special.txt");
    String content = "\t\n\r\f\b";
    Files.write(file, content.getBytes());

    assertFalse(BinaryFileDetector.isBinary(file));
  }

  @Test
  void testIsBinaryByExtensionJpg() {
    Path path = Paths.get("image.jpg");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionJpeg() {
    Path path = Paths.get("photo.jpeg");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionPng() {
    Path path = Paths.get("screenshot.png");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionGif() {
    Path path = Paths.get("animation.gif");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionMp3() {
    Path path = Paths.get("song.mp3");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionMp4() {
    Path path = Paths.get("video.mp4");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionExe() {
    Path path = Paths.get("program.exe");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionZip() {
    Path path = Paths.get("archive.zip");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionJar() {
    Path path = Paths.get("library.jar");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionTxt() {
    Path path = Paths.get("file.txt");
    assertFalse(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionJava() {
    Path path = Paths.get("Main.java");
    assertFalse(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionXml() {
    Path path = Paths.get("config.xml");
    assertFalse(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionJson() {
    Path path = Paths.get("data.json");
    assertFalse(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionCaseInsensitive() {
    Path pathLower = Paths.get("image.jpg");
    Path pathUpper = Paths.get("IMAGE.JPG");
    Path pathMixed = Paths.get("Photo.JpG");

    assertTrue(BinaryFileDetector.isBinaryByExtension(pathLower));
    assertTrue(BinaryFileDetector.isBinaryByExtension(pathUpper));
    assertTrue(BinaryFileDetector.isBinaryByExtension(pathMixed));
  }

  @Test
  void testIsBinaryByExtensionWithPath() {
    Path path = Paths.get("/home/user/documents/image.jpg");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionWithComplexPath() {
    Path path = Paths.get("C:/Users/Documents/My Images/photo.jpeg");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionNoExtension() {
    Path path = Paths.get("README");
    assertFalse(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionMultipleDots() {
    Path path = Paths.get("archive.tar.gz");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionWebp() {
    Path path = Paths.get("image.webp");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionPdf() {
    Path path = Paths.get("document.pdf");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionDocx() {
    Path path = Paths.get("report.docx");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionClass() {
    Path path = Paths.get("Main.class");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryWithNonExistentFile() {
    Path nonExistent = tempDir.resolve("nonexistent.bin");
    // Should return true since we can't read it (safe assumption)
    assertTrue(BinaryFileDetector.isBinary(nonExistent));
  }

  @Test
  void testIsBinaryByExtensionDll() {
    Path path = Paths.get("library.dll");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionSo() {
    Path path = Paths.get("library.so");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionMsi() {
    Path path = Paths.get("installer.msi");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryWithHighByteValues() throws Exception {
    Path file = tempDir.resolve("test.bin");
    byte[] content = {(byte) 0xFF, (byte) 0xFE, (byte) 0xFD};
    Files.write(file, content);

    // High byte values without null bytes should return false
    assertFalse(BinaryFileDetector.isBinary(file));
  }

  @Test
  void testIsBinaryWithMixedContent() throws Exception {
    Path file = tempDir.resolve("mixed.txt");
    String textContent = "This is text with special chars: \t\n\r";
    Files.write(file, textContent.getBytes());

    assertFalse(BinaryFileDetector.isBinary(file));
  }

  @Test
  void testIsBinaryByExtensionLock() {
    Path path = Paths.get("file.lock");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionCache() {
    Path path = Paths.get("data.cache");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionPyc() {
    Path path = Paths.get("script.pyc");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionGit() {
    Path path = Paths.get(".git");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionMkv() {
    Path path = Paths.get("video.mkv");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionWar() {
    Path path = Paths.get("application.war");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionCsv() {
    Path path = Paths.get("data.csv");
    assertFalse(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionPython() {
    Path path = Paths.get("script.py");
    assertFalse(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionJavaScript() {
    Path path = Paths.get("app.js");
    assertFalse(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionRar() {
    Path path = Paths.get("archive.rar");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionSevenZ() {
    Path path = Paths.get("archive.7z");
    assertTrue(BinaryFileDetector.isBinaryByExtension(path));
  }

  @Test
  void testIsBinaryByExtensionRc() {
    Path path = Paths.get("config.rc");
    assertFalse(BinaryFileDetector.isBinaryByExtension(path));
  }
}
