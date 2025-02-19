package com.tyrael.kharazim.test.mock;

import jakarta.servlet.ServletOutputStream;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
@Getter
public class MockFileHttpServletResponse extends MockHttpServletResponse {

    private final File file;

    public MockFileHttpServletResponse(File file) {
        this.file = file;
    }

    public MockFileHttpServletResponse(String fileName, String fileDir) {
        this(new File(fileDir, fileName));
    }

    public MockFileHttpServletResponse(String fileName) {
        this(fileName, defaultFileDir());
    }

    private static String defaultFileDir() {
        String kharazimExportDir = System.getenv("KHARAZIM_EXPORT_DIR");
        if (kharazimExportDir != null) {
            return kharazimExportDir;
        }
        return new File(System.getProperty("java.io.tmpdir"), "kharazim_export").getAbsolutePath();
    }

    @Override
    @NonNull
    public ServletOutputStream getOutputStream() {
        try {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                Files.createDirectory(parentFile.toPath());
            }
            return new DelegatingServletOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
