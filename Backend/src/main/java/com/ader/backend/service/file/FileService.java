package com.ader.backend.service.file;

import com.ader.backend.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileService {

    byte[] compressBytes(byte[] data);

    byte[] decompressBytes(byte[] data);

    File compressFile(File file);

    List<File> compressFile(List<File> files);

    File decompressFile(File file);

    List<File> decompressFile(List<File> files);

    List<File> getAllFiles();

    File findByUuid(UUID uuid);

    List<File> getAllFilesForOffer(Long offerId);

    List<File> getAllFilesForUser(String userEmail, String offerIsNull);

    File uploadFile(MultipartFile file) throws IOException;

    String deleteFile(UUID uuid);

    File findByNameAndTypeAndUserId(String fileName, String fileType, Long userId);
}
