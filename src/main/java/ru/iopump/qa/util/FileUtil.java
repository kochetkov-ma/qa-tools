package ru.iopump.qa.util;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.iopump.qa.exception.QaUtilException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class FileUtil {

    public Path getClassPathMainDir() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final URL tmp = loader.getResource("");
        Preconditions.checkState(tmp != null, "Cannot get classpath dir");
        try {
            return Paths.get(tmp.toURI());
        } catch (URISyntaxException e) {
            throw new QaUtilException(e);
        }
    }


    public Path createFileIfNotExists(@NonNull Path path) {
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new QaUtilException(e);
        }
        return path;
    }

    public Path createDirIfNotExists(@NonNull Path path) {
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new QaUtilException(e);
        }
        return path;
    }

    public boolean isAbsolute(@NonNull String candidate) {
        return Paths.get(candidate).isAbsolute();
    }

    public boolean isUserDirRelative(@NonNull String candidate) {
        return Files.exists(getUserDir().resolve(candidate));
    }

    public Path getUserDir() {
        return Paths.get("");
    }
}