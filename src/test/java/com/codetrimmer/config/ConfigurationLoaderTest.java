package com.codetrimmer.config;

import static org.junit.jupiter.api.Assertions.*;

import com.codetrimmer.error.CodeTrimmerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for ConfigurationLoader service.
 */
class ConfigurationLoaderTest {

    private ConfigurationLoader loader;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        loader = new ConfigurationLoader();
    }

    @Test
    void testLoadDefaultConfiguration() {
        TrimmerConfig config = loader.loadConfiguration(tempDir.toString());
        assertNotNull(config);
        assertEquals("*", config.getInclude());
        assertTrue(config.isCreateBackups());
    }

    @Test
    void testLoadYamlConfiguration() throws IOException {
        String yaml = """
            include: "java,py"
            exclude: "test"
            maxFiles: 100
            createBackups: false
            """;
        Path configPath = tempDir.resolve(".trimmerrc");
        Files.writeString(configPath, yaml);

        TrimmerConfig config = loader.loadConfiguration(tempDir.toString());
        assertEquals("java,py", config.getInclude());
        assertEquals("test", config.getExclude());
        assertEquals(100, config.getMaxFiles());
        assertFalse(config.isCreateBackups());
    }

    @Test
    void testLoadJsonConfiguration() throws IOException {
        String json = """
            {
                "include": "js,ts",
                "maxFileSize": 1000000,
                "verbose": true
            }
            """;
        Path configPath = tempDir.resolve(".trimmerrc.json");
        Files.writeString(configPath, json);

        TrimmerConfig config = loader.loadConfiguration(tempDir.toString());
        assertEquals("js,ts", config.getInclude());
        assertEquals(1000000, config.getMaxFileSize());
        assertTrue(config.isVerbose());
    }

    @Test
    void testLoadFromSpecificFile() throws IOException {
        String yaml = """
            include: "md"
            dryRun: true
            """;
        Path configPath = tempDir.resolve("custom.yml");
        Files.writeString(configPath, yaml);

        TrimmerConfig config = loader.loadFromFile(configPath);
        assertEquals("md", config.getInclude());
        assertTrue(config.isDryRun());
    }

    @Test
    void testInvalidYamlThrowsException() throws IOException {
        String invalidYaml = """
            include: [invalid yaml
            """;
        Path configPath = tempDir.resolve(".trimmerrc");
        Files.writeString(configPath, invalidYaml);

        assertThrows(CodeTrimmerException.class, () -> {
            loader.loadFromFile(configPath);
        });
    }

    @Test
    void testInvalidJsonThrowsException() throws IOException {
        String invalidJson = """
            {"include": "java"
            """;
        Path configPath = tempDir.resolve(".trimmerrc.json");
        Files.writeString(configPath, invalidJson);

        assertThrows(CodeTrimmerException.class, () -> {
            loader.loadFromFile(configPath);
        });
    }

    @Test
    void testFileNotFoundThrowsException() {
        Path nonExistent = tempDir.resolve("nonexistent.yml");
        assertThrows(CodeTrimmerException.class, () -> {
            loader.loadFromFile(nonExistent);
        });
    }

    @Test
    void testValidateConfigurationNegativeMaxFiles() {
        TrimmerConfig config = new TrimmerConfig();
        config.setMaxFiles(-1);

        assertThrows(CodeTrimmerException.class, () -> {
            loader.validateConfiguration(config);
        });
    }

    @Test
    void testValidateConfigurationNegativeMaxFileSize() {
        TrimmerConfig config = new TrimmerConfig();
        config.setMaxFileSize(-1);

        assertThrows(CodeTrimmerException.class, () -> {
            loader.validateConfiguration(config);
        });
    }

    @Test
    void testValidateRuleWithMissingName() {
        TrimmerConfig config = new TrimmerConfig();
        TrimmerConfig.TrimRule rule = new TrimmerConfig.TrimRule();
        rule.setPattern("test");
        config.getRules().add(rule);

        assertThrows(CodeTrimmerException.class, () -> {
            loader.validateConfiguration(config);
        });
    }

    @Test
    void testValidateRuleWithMissingPattern() {
        TrimmerConfig config = new TrimmerConfig();
        TrimmerConfig.TrimRule rule = new TrimmerConfig.TrimRule();
        rule.setName("test-rule");
        config.getRules().add(rule);

        assertThrows(CodeTrimmerException.class, () -> {
            loader.validateConfiguration(config);
        });
    }

    @Test
    void testValidateRuleWithInvalidRegex() {
        TrimmerConfig config = new TrimmerConfig();
        TrimmerConfig.TrimRule rule = new TrimmerConfig.TrimRule();
        rule.setName("test-rule");
        rule.setPattern("[invalid");
        config.getRules().add(rule);

        assertThrows(CodeTrimmerException.class, () -> {
            loader.validateConfiguration(config);
        });
    }

    @Test
    void testApplyConfiguration() {
        TrimmerConfig source = new TrimmerConfig();
        source.setInclude("java");
        source.setMaxFiles(200);
        source.setVerbose(true);

        CodeTrimmerConfig target = new CodeTrimmerConfig();
        loader.applyConfiguration(source, target);

        assertEquals("java", target.getInclude());
        assertEquals(200, target.getMaxFiles());
        assertTrue(target.isVerbose());
    }

    @Test
    void testLoadConfigurationWithRules() throws IOException {
        String yaml = """
            rules:
              - name: "test-rule"
                description: "Test description"
                pattern: "\\\\s+$"
                replacement: ""
                enabled: true
            """;
        Path configPath = tempDir.resolve(".trimmerrc");
        Files.writeString(configPath, yaml);

        TrimmerConfig config = loader.loadConfiguration(tempDir.toString());
        assertEquals(1, config.getRules().size());
        assertEquals("test-rule", config.getRules().get(0).getName());
    }
}
