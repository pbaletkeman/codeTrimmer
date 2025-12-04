package com.codetrimmer.config;

import com.codetrimmer.error.CodeTrimmerException;
import com.codetrimmer.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Service for loading and validating .trimmerrc configuration files.
 * Supports both YAML and JSON formats.
 */
@Service
public class ConfigurationLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationLoader.class);
    private static final String[] CONFIG_FILE_NAMES = {".trimmerrc", ".trimmerrc.yaml", ".trimmerrc.json"};

    private final ObjectMapper objectMapper;

    public ConfigurationLoader() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Loads configuration from default locations.
     * Searches for .trimmerrc, .trimmerrc.yaml, or .trimmerrc.json in the given directory.
     *
     * @param directory the directory to search for config files
     * @return loaded configuration or default if none found
     */
    public TrimmerConfig loadConfiguration(String directory) {
        Path dirPath = Paths.get(directory);

        for (String configName : CONFIG_FILE_NAMES) {
            Path configPath = dirPath.resolve(configName);
            if (Files.exists(configPath)) {
                LOGGER.info("Loading configuration from: {}", configPath);
                return loadFromFile(configPath);
            }
        }

        LOGGER.info("No configuration file found, using defaults");
        return new TrimmerConfig();
    }

    /**
     * Loads configuration from a specific file path.
     *
     * @param configPath the path to the config file
     * @return loaded configuration
     * @throws CodeTrimmerException if file cannot be loaded or is invalid
     */
    public TrimmerConfig loadFromFile(Path configPath) {
        if (!Files.exists(configPath)) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0002,
                "File not found: " + configPath,
                "Verify the configuration file exists at the specified path"
            );
        }

        try {
            String content = Files.readString(configPath);
            String filename = configPath.getFileName().toString().toLowerCase();

            TrimmerConfig config;
            if (filename.endsWith(".json")) {
                config = loadFromJson(content);
            } else {
                config = loadFromYaml(content);
            }

            validateConfiguration(config);
            return config;

        } catch (IOException e) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0011,
                "Cannot read configuration file: " + e.getMessage(),
                "Check file permissions and encoding"
            );
        }
    }

    /**
     * Loads configuration from JSON string.
     *
     * @param json the JSON content
     * @return parsed configuration
     */
    private TrimmerConfig loadFromJson(String json) {
        try {
            return objectMapper.readValue(json, TrimmerConfig.class);
        } catch (Exception e) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0001,
                "Invalid JSON format: " + e.getMessage(),
                "Verify JSON syntax using a validator"
            );
        }
    }

    /**
     * Loads configuration from YAML string.
     *
     * @param yaml the YAML content
     * @return parsed configuration
     */
    private TrimmerConfig loadFromYaml(String yaml) {
        try {
            Yaml yamlParser = new Yaml(new Constructor(TrimmerConfig.class,
                new org.yaml.snakeyaml.LoaderOptions()));
            TrimmerConfig config = yamlParser.load(yaml);
            return config != null ? config : new TrimmerConfig();
        } catch (YAMLException e) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0001,
                "Invalid YAML format: " + e.getMessage(),
                "Verify YAML syntax and indentation"
            );
        }
    }

    /**
     * Validates the loaded configuration.
     *
     * @param config the configuration to validate
     * @throws CodeTrimmerException if validation fails
     */
    public void validateConfiguration(TrimmerConfig config) {
        if (config.getMaxFileSize() < 0) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0003,
                "maxFileSize must be non-negative",
                "Set maxFileSize to a positive value or 0 for unlimited"
            );
        }

        if (config.getMaxFiles() < 0) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0003,
                "maxFiles must be non-negative",
                "Set maxFiles to a positive value or 0 for unlimited"
            );
        }

        if (config.getMaxConsecutiveBlankLines() < 0) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0003,
                "maxConsecutiveBlankLines must be non-negative",
                "Set maxConsecutiveBlankLines to 0 or higher"
            );
        }

        // Validate custom rules
        for (TrimmerConfig.TrimRule rule : config.getRules()) {
            validateRule(rule);
        }
    }

    /**
     * Validates a custom trimming rule.
     *
     * @param rule the rule to validate
     * @throws CodeTrimmerException if rule is invalid
     */
    private void validateRule(TrimmerConfig.TrimRule rule) {
        if (rule.getName() == null || rule.getName().trim().isEmpty()) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0004,
                "Rule name is required",
                "Add a 'name' field to the rule definition"
            );
        }

        if (rule.getPattern() == null || rule.getPattern().trim().isEmpty()) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0004,
                "Rule pattern is required for rule: " + rule.getName(),
                "Add a 'pattern' field with a valid regex"
            );
        }

        try {
            Pattern.compile(rule.getPattern());
        } catch (PatternSyntaxException e) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0005,
                "Invalid regex pattern in rule '" + rule.getName() + "': " + e.getMessage(),
                "Fix the regex pattern syntax"
            );
        }
    }

    /**
     * Applies configuration to the CodeTrimmerConfig instance.
     *
     * @param source the source configuration
     * @param target the target configuration to update
     */
    public void applyConfiguration(TrimmerConfig source, CodeTrimmerConfig target) {
        target.setInclude(source.getInclude());
        target.setExclude(source.getExclude());
        target.setIncludeHidden(source.isIncludeHidden());
        target.setFollowSymlinks(source.isFollowSymlinks());
        target.setMaxConsecutiveBlankLines(source.getMaxConsecutiveBlankLines());
        target.setEnsureFinalNewline(source.isEnsureFinalNewline());
        target.setTrimTrailingWhitespace(source.isTrimTrailingWhitespace());
        target.setMaxFileSize(source.getMaxFileSize());
        target.setMaxFiles(source.getMaxFiles());
        target.setNoLimits(source.isNoLimits());
        target.setDryRun(source.isDryRun());
        target.setCreateBackups(source.isCreateBackups());
        target.setFailFast(source.isFailFast());
        target.setVerbose(source.isVerbose());
        target.setQuiet(source.isQuiet());
        target.setNoColor(source.isNoColor());
    }
}
