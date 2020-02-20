package ru.iopump.qa.util;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class FileUtilTest {

    @BeforeClass
    public static void before() {
        FileUtil.ENABLE_WATCHDOG = true;
    }

    @AfterClass
    public static void after() {
        FileUtil.getFilesWatchdog().close();
    }

    @Test
    public void getClassPathMainDir() {
        final Path path = FileUtil.getClassPathMainDir();
        assertThat(path).endsWith(Paths.get("qa-tools/build/classes/java/test"));
    }

    @Test
    public void createFileIfNotExists() throws IOException {
        Path base = FileUtil.getClassPathMainDir();

        Path path = base.resolve("file/test.txt");
        FileUtil.createFile(path);
        Files.writeString(path, "1");
        assertThat(path).hasContent("1").exists();
        boolean res = FileUtil.createFile(path, true);
        assertThat(path).hasContent("1").exists();
        assertThat(res).isFalse();
        FileUtil.createFile(path);
        assertThat(path).hasContent("").exists();


        path = Paths.get("test.txt");
        FileUtil.createFile(path);
        Files.writeString(path, "2");
        assertThat(path).hasContent("2").exists();
        res = FileUtil.createFile(path, true);
        assertThat(path).hasContent("2").exists();
        assertThat(res).isFalse();
        FileUtil.createFile(path);
        assertThat(path).hasContent("").exists();
    }

    @Test
    public void createDirIfNotExists() {
    }

    @Test
    public void isAbsolute() {
    }

    @Test
    public void isUserDirRelative() {
    }

    @Test
    public void getUserDir() {
    }
}