package ru.iopump.qa.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import ru.iopump.qa.exception.QaUtilException;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@UtilityClass
public class FileUtil {
    /**
     * Enable FilesWatchdog function.
     * Default is false.
     */
    public static boolean ENABLE_WATCHDOG = false;
    private static volatile FilesWatchdog filesWatchdog;

    /**
     * Get single {@link FilesWatchdog} for all create operation in this utility class {@link FileUtil}.
     * Enable before getting {@link #ENABLE_WATCHDOG}.
     * When watchdog is disabled {@link #ENABLE_WATCHDOG} this method returns dummy class with no functions.
     *
     * @see FilesWatchdog
     * @see #ENABLE_WATCHDOG
     */
    public static FilesWatchdog getFilesWatchdog() {
        if (!ENABLE_WATCHDOG) return new FilesWatchdog() {
            @Override
            public void close() {}

            @Override
            public Map<Path, Throwable> getError() {
                return Collections.emptyMap();
            }

            @Override
            public Collection<Path> getAll() {
                return Collections.emptyList();
            }
        };
        synchronized (FileUtil.class) {
            if (filesWatchdog == null || filesWatchdog.closed) {
                filesWatchdog = new FilesWatchdog();
            }
        }
        return filesWatchdog;
    }

    /**
     * Get root directory with classes.
     *
     * @return root directory with classes.
     */
    @NonNull
    public static Path getClassPathMainDir() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final URL tmp = loader.getResource("");
        Preconditions.checkState(tmp != null, "Cannot get classpath dir");
        try {
            return Paths.get(tmp.toURI());
        } catch (URISyntaxException e) {
            throw new QaUtilException(e);
        }
    }

    /**
     * Create all necessary directories and file.
     *
     *
     * @param path file to create.
     * @param checkExisting true - check if exists before creating / null or false - don't check
     * @return true - File has been created / false - not
     */
    @NonNull
    public static boolean createFile(@NonNull Path path, boolean ... checkExisting) {
        try {
            if (Files.notExists(path) || !(ArrayUtils.isNotEmpty(checkExisting) && checkExisting[0])) {
                Optional.ofNullable(path.getParent()).ifPresent(FileUtil::createDir);
                Files.deleteIfExists(path);
                getFilesWatchdog().add(Files.createFile(path));
                return true;
            }
        } catch (IOException e) {
            throw new QaUtilException(e);
        }
        return false;
    }

    /**
     * Create all necessary directories if not exists.
     *
     * @param path directory to create.
     * @param checkExisting true - check if exists before creating / null or false - don't check
     * @return true - Directory has been created / false - not
     */
    @NonNull
    public static boolean createDir(@NonNull Path path, boolean ... checkExisting) {
        try {
            if (Files.notExists(path) || !(ArrayUtils.isNotEmpty(checkExisting) && checkExisting[0])) {
                if (Files.isRegularFile(path)) Files.deleteIfExists(path);
                getFilesWatchdog().add(Files.createDirectories(path));
                return true;
            }
        } catch (IOException e) {
            throw new QaUtilException(e);
        }
        return false;
    }

    /**
     * Check if candidate string is valid absolute path in current file system. Null safe.
     *
     * @param candidate absolute path.
     * @return true - it's absolute path.
     */
    public static boolean isAbsolute(@Nullable String candidate) {
        return Paths.get(candidate == null ? "" : candidate).isAbsolute();
    }

    /**
     * Check if candidate string is valid path from user directory. Null safe.
     *
     * @param candidate relative path from user directory.
     * @return true - it's path from user directory.
     */
    public static boolean isUserDirRelative(@Nullable String candidate) {
        return Files.exists(getUserDir().resolve(candidate == null ? "" : candidate));
    }

    /**
     * Get User directory. It's root project directory or directory where this application has been started.
     *
     * @return Path to user directory.
     */
    public static Path getUserDir() {
        return Paths.get("");
    }

    /**
     * It stores all created files and directory.
     * You can get all watched files or remove all {@link #close()}.
     * Also you can get all errors after closing.
     * Enable it before {@link #ENABLE_WATCHDOG}.
     * Then get it {@link #getFilesWatchdog()}.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FilesWatchdog implements Closeable {
        private Collection<Path> watchedPaths = new ConcurrentLinkedQueue<>();
        private Map<Path, Throwable> errors = new HashMap<>();
        private boolean closed;

        /**
         * Delete all files created via {@link FileUtil}.
         * Enable it before {@link #ENABLE_WATCHDOG}.
         * Then get it {@link #getFilesWatchdog()}.
         */
        @Override
        public void close() {
            if (closed) return;
            synchronized (FileUtil.class) {
                watchedPaths.parallelStream().forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        errors.put(path, e);
                    }
                });
                closed = true;
            }
        }

        /**
         * Get all errors after closing.
         * Enable it before {@link #ENABLE_WATCHDOG}.
         * Then get it {@link #getFilesWatchdog()}.
         */
        public Map<Path, Throwable> getError() {
            return ImmutableMap.copyOf(errors);
        }

        /**
         * Get all created files and dirs.
         * Enable it before {@link #ENABLE_WATCHDOG}.
         * Then get it {@link #getFilesWatchdog()}.
         */
        public Collection<Path> getAll() {
            return Collections.unmodifiableCollection(watchedPaths);
        }

        private void add(Path path) {
            synchronized (FileUtil.class) {
                Preconditions.checkState(!closed, "FilesWatchdog already closed");
                Optional.ofNullable(path).ifPresent(p -> watchedPaths.add(p));
            }
        }
    }
}