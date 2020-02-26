package ru.iopump.qa.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.iopump.qa.exception.QaUtilException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@UtilityClass
public class ResourceUtil {

    /**
     * Create new InputStream of expected resource. Dont forget to close this stream !!!
     * Resource from Relative path from user/current/active directory or Absolute path or File in classpath or File from resource dir.
     * Support Jar file and deps projects.
     *
     * @param relativeOrAbsoluteOrClasspath Relative path from user/current/active directory. Absolute path. File in classpath. File from resource dir.
     */
    @NonNull
    public static InputStream getResourceAsStream(@NonNull String relativeOrAbsoluteOrClasspath) {
        return ifFileFromFileSystem(relativeOrAbsoluteOrClasspath)
                .filter(file -> Files.exists(file))
                .map(file -> {
                    try {
                        return Files.newInputStream(file);
                    } catch (IOException e) {
                        throw new QaUtilException("Cannot read resource '{}'. It is a file. But cannot be read",
                                e, relativeOrAbsoluteOrClasspath);
                    }
                })
                .orElseGet(() -> ofNullable(Thread.currentThread().getContextClassLoader())
                        .map(cl -> cl.getResourceAsStream(relativeOrAbsoluteOrClasspath))
                        .orElseGet(() -> ofNullable(ClassLoader.getSystemResourceAsStream(relativeOrAbsoluteOrClasspath))
                                .orElseGet(() -> ofNullable(ResourceUtil.class.getResourceAsStream(relativeOrAbsoluteOrClasspath))
                                        .orElseThrow(() -> new QaUtilException(
                                                "Cannot find resource '{}'. It is not absolute path, not relative path, not classpath resource",
                                                relativeOrAbsoluteOrClasspath
                                        ))
                                )
                        )
                );
    }

    private static Optional<Path> ifFileFromFileSystem(String relativeOrAbsoluteOrClasspath) {
        if (FileUtil.isAbsolute(relativeOrAbsoluteOrClasspath)) {
            return Optional.of(Paths.get(relativeOrAbsoluteOrClasspath));
        } else if (FileUtil.isUserDirRelative(relativeOrAbsoluteOrClasspath)) {
            return Optional.of(FileUtil.getUserDir().resolve(relativeOrAbsoluteOrClasspath));
        }
        return Optional.empty();
    }
}
