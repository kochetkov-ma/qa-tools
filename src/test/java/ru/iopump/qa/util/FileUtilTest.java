package ru.iopump.qa.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class FileUtilTest {

    @BeforeClass
    public static void before() {
        FileUtil.ENABLE_WATCHDOG = true;
    }

    @After
    public void after() {
        FileUtil.getFilesWatchdog().close();
    }

    @Test
    public void getClassPathMainDir() {
        final Path path = FileUtil.getClassPathMainDir();
        assertThat(path).endsWith(Paths.get("qa-tools/build/classes/java/test"));
    }

    @Test
    public void createFile() throws IOException {
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
    public void createDir() {
        Path base = FileUtil.getClassPathMainDir();

        Path path = base.resolve("file");
        FileUtil.createDir(path);
        assertThat(path).exists().isDirectory();
        boolean res = FileUtil.createDir(path, true);
        assertThat(path).exists().isDirectory();
        assertThat(res).isFalse();
        res = FileUtil.createDir(path);
        assertThat(path).exists().isDirectory();
        assertThat(res).isTrue();

        String uuid = UUID.randomUUID().toString();
        path = Paths.get(uuid);
        FileUtil.createDir(path);
        assertThat(path).exists().isDirectory();
        res = FileUtil.createDir(path, true);
        assertThat(path).exists().isDirectory();
        assertThat(res).isFalse();
        res = FileUtil.createDir(path);
        assertThat(path).exists().isDirectory();
        assertThat(res).isTrue();
    }

    @Test
    public void isAbsolute() {
        final Path path = FileUtil.getClassPathMainDir();
        assertThat(FileUtil.isAbsolute(path.toString())).isTrue();

        assertThat(FileUtil.isAbsolute(null)).isFalse();
        assertThat(FileUtil.isAbsolute("")).isFalse();
        assertThat(FileUtil.isAbsolute("not_exists")).isFalse();
        assertThat(FileUtil.isAbsolute("\\")).isFalse();
        assertThat(FileUtil.isAbsolute(path.getFileName().toString())).isFalse();
    }

    @Test
    public void isUserDirRelative() {
        final Path path = FileUtil.getClassPathMainDir();
        assertThat(FileUtil.isUserDirRelative("build")).isTrue();
        assertThat(FileUtil.isUserDirRelative("build/classes")).isTrue();

        assertThat(FileUtil.isAbsolute(null)).isFalse();
        assertThat(FileUtil.isAbsolute("")).isFalse();
        assertThat(FileUtil.isAbsolute("not_exists")).isFalse();
        assertThat(FileUtil.isAbsolute("\\")).isFalse();
        assertThat(FileUtil.isAbsolute(path.getFileName().toString())).isFalse();
    }

    @Test
    public void getUserDir() {
        final Path path = FileUtil.getUserDir();
        assertThat(path).endsWith(Paths.get("qa-tools"));
    }
}