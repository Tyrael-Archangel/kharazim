package com.tyrael.kharazim.mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
public class MockMultipartFile extends org.springframework.mock.web.MockMultipartFile {

    public MockMultipartFile(File file) throws IOException {
        super(file.getName(), Files.newInputStream(file.toPath()));
    }

}
