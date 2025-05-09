package com.tyrael.kharazim.basicdata.app.service.file;

import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.basicdata.app.config.FileConfig;
import com.tyrael.kharazim.basicdata.app.constant.BasicDataBusinessIdConstants;
import com.tyrael.kharazim.basicdata.app.domain.file.FileDO;
import com.tyrael.kharazim.basicdata.app.dto.file.FileDTO;
import com.tyrael.kharazim.basicdata.app.dto.file.UploadFileRequest;
import com.tyrael.kharazim.basicdata.app.mapper.file.FileMapper;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author Tyrael Archangel
 * @since 2024/4/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;
    private final FileConfig fileConfig;
    private final IdGenerator idGenerator;

    @Override
    public FileDTO upload(UploadFileRequest fileVO, AuthUser currentUser) throws IOException {

        File root = fileRoot();
        File targetDir = nextDir(root);

        String fileName = fileVO.getFileName();
        String fileId = idGenerator.next(BasicDataBusinessIdConstants.FILE);
        File targetFile = new File(targetDir, fileId + "_" + fileName);

        try (InputStream inputStream = fileVO.getInput();
             FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            inputStream.transferTo(outputStream);
        }

        String path = targetFile.getAbsolutePath().substring(root.getAbsolutePath().length());
        if (path.startsWith(File.separator)) {
            path = path.substring(File.separator.length());
        }

        FileDO fileDO = new FileDO();
        fileDO.setId(fileId);
        fileDO.setName(fileName);
        fileDO.setPath(path);
        fileDO.setContentType(fileVO.getContentType());
        fileDO.setCreator(currentUser.getNickName());
        fileDO.setCreatorCode(currentUser.getCode());
        fileDO.setCreateTime(LocalDateTime.now());

        fileMapper.insert(fileDO);

        return new FileDTO(fileId, this.getUrl(fileId));
    }

    private File fileRoot() throws IOException {

        String fileHome = fileConfig.getHomePathOrDefault();
        File home = new File(fileHome);
        if (!home.exists()) {
            throw new FileNotFoundException("file home not exists");
        }
        File root = new File(home, fileConfig.getRootDir());
        if (root.exists()) {
            if (root.isFile()) {
                throw new NotDirectoryException("file root not directory");
            }
        } else {
            if (!root.mkdirs()) {
                throw new IOException("create file root error");
            }
        }
        return root;
    }

    private synchronized File nextDir(File root) throws IOException {
        File dir = root;
        for (int i = 0; i < fileConfig.getMaxDirLevel(); i++) {
            dir = lastSubDirOrCreate(dir);
        }

        int subFileCount = subFileCount(dir);
        if (subFileCount < fileConfig.getMaxFileCountAtLeafDir()) {
            return dir;
        }

        // 最末级目录下文件数超过最大值，创建新的目录来存放文件
        createNextDir(root, dir);

        File targetDir = root;
        for (int i = 0; i < fileConfig.getMaxDirLevel(); i++) {
            targetDir = lastSubDirOrCreate(targetDir);
        }
        return targetDir;
    }

    private void createNextDir(File root, File dir) throws IOException {
        File parentFile = dir.getParentFile();
        // 如果父目录已经是root了，就直接在父目录下创建子目录（所以root下的目录数量可能超过最大限制）
        // 否则就看父目录下的目录数量是否达到最大限制了，达到了，就再往上一级，没达到，就直接在父目录下创建子目录
        if (parentFile.equals(root) || subFileCount(parentFile) < fileConfig.getMaxDirCountEveryLevel()) {
            randomCreateSubDir(parentFile);
        } else {
            createNextDir(root, parentFile);
        }
    }

    private File lastSubDirOrCreate(File dir) throws IOException {
        File[] subDirs = dir.listFiles(File::isDirectory);
        if (subDirs == null || subDirs.length == 0) {
            return randomCreateSubDir(dir);
        }
        return Stream.of(subDirs)
                .max(Comparator.comparing(File::getName))
                .orElseThrow(FileNotFoundException::new);
    }

    private File randomCreateSubDir(File parent) throws IOException {
        int i = 10;
        while (i-- > 0) {
            File file = new File(parent, idGenerator.next(BasicDataBusinessIdConstants.FILE_DIR));
            if (!file.exists() && file.mkdirs()) {
                return file;
            }
        }
        throw new IOException("create file dir error");
    }

    private int subFileCount(File dir) {
        File[] subFiles = dir.listFiles();
        return subFiles == null ? 0 : subFiles.length;
    }

    @Override
    public void download(String fileId, HttpServletResponse httpServletResponse) throws IOException {
        FileDO fileDO = fileMapper.selectById(fileId);
        DomainNotFoundException.assertFound(fileDO, fileId);

        File root = fileRoot();
        File targetFile = new File(root, fileDO.getPath());
        try (FileInputStream fileInputStream = new FileInputStream(targetFile)) {

            httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    ContentDisposition.inline().filename(fileDO.getName()).build().toString());
            httpServletResponse.setHeader(HttpHeaders.CACHE_CONTROL,
                    CacheControl.maxAge(fileConfig.getMaxAge()).cachePublic().getHeaderValue());
            String contentType = fileDO.getContentType();
            if (StringUtils.isNotBlank(contentType)) {
                httpServletResponse.setContentType(contentType);
            }
            httpServletResponse.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
            httpServletResponse.setContentLengthLong(targetFile.length());

            fileInputStream.transferTo(httpServletResponse.getOutputStream());
        }
    }

    @Override
    public byte[] readBytes(String fileId) throws IOException {
        FileDO fileDO = fileMapper.selectById(fileId);
        DomainNotFoundException.assertFound(fileDO, fileId);

        File root = fileRoot();
        File targetFile = new File(root, fileDO.getPath());
        try (FileInputStream fileInputStream = new FileInputStream(targetFile)) {
            return fileInputStream.readAllBytes();
        }
    }

    @Override
    public String getUrl(String fileId) {
        if (StringUtils.isBlank(fileId)) {
            return null;
        }

//      String url = "http://localhost:9408/kharazim-api/system/file/fetch/" + fileId;
        return "/kharazim-api/system/file/fetch/" + fileId;
    }

}
