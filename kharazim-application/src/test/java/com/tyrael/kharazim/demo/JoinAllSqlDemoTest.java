package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/7/18
 */
public class JoinAllSqlDemoTest {

    @Test
    public void joinSql() throws IOException {
        String home = System.getProperty("user.home");
        String path = "IdeaProjects/tyrael/kharazim/kharazim-application/sql";
        File[] files = new File(home, path).listFiles();
        assert files != null;
        List<File> fileList = Arrays.asList(files);
        fileList.sort(Comparator.comparing(File::getName));
        for (File sqlFile : fileList) {
            Files.readAllLines(sqlFile.toPath())
                    .forEach(System.out::println);
        }
    }

}
