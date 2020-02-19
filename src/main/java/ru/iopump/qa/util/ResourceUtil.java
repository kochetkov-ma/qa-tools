package ru.iopump.qa.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.iopump.qa.exception.QaUtilException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public InputStream getResourceAsStream(@NonNull String relativeOrAbsoluteOrClasspath) {
        final Path file;
        InputStream resultStream;
        if (FileUtil.isAbsolute(relativeOrAbsoluteOrClasspath)) {
            file = Paths.get(relativeOrAbsoluteOrClasspath);
        } else if (FileUtil.isUserDirRelative(relativeOrAbsoluteOrClasspath)) {
            file = FileUtil.getUserDir().resolve(relativeOrAbsoluteOrClasspath);
        } else {
            file = null;
        }
        if (file != null && Files.exists(file)) {
            try {
                resultStream = Files.newInputStream(file);
            } catch (IOException e) {
                throw new QaUtilException("Cannot read resource '" + relativeOrAbsoluteOrClasspath +
                        "'. It is a file. But cannot be read", e);
            }
        } else {
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                resultStream = cl.getResourceAsStream(relativeOrAbsoluteOrClasspath);
            } else {
                resultStream = ClassLoader.getSystemResourceAsStream(relativeOrAbsoluteOrClasspath);
            }
            if (resultStream == null) {
                resultStream = ResourceUtil.class.getResourceAsStream(relativeOrAbsoluteOrClasspath);
            }
        }

        if (resultStream == null) {
            throw new QaUtilException("Cannot find resource '" + relativeOrAbsoluteOrClasspath +
                    "'. It is not absolute path, not relative path, not classpath resource");
        }
        return resultStream;
    }
}
