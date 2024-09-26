package com.tyrael.kharazim.mock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
@SuppressWarnings("unused")
public class MockMultipartFile extends org.springframework.mock.web.MockMultipartFile {

    public MockMultipartFile(File file) throws IOException {
        this(file.getName(), Files.newInputStream(file.toPath()));
    }

    public MockMultipartFile(String name, InputStream contentStream) throws IOException {
        super(name, contentStream);
    }

}
