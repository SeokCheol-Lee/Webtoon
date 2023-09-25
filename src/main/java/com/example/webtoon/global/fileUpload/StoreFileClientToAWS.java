package com.example.webtoon.global.fileUpload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.webtoon.global.exception.ErrorCode;
import com.example.webtoon.global.exception.GlobalException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreFileClientToAWS implements StoreFileClient {

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  private final AmazonS3 s3Client;
  private static final String CATEGORY_PREFIX = "/";
  private static final String TIME_SEPARATOR = "_";
  @Override
  public Resource getFile(String filename) {
    S3Object s3Object = s3Client.getObject(bucketName, filename);
    S3ObjectInputStream inputStream = s3Object.getObjectContent();
    return new InputStreamResource(inputStream);
  }
  @Override
  public UploadFile storeFile(MultipartFile multipartFile, String name, String chapter) {
    String storeFileName = createStoreFileName(multipartFile.getOriginalFilename(), name, chapter);
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(multipartFile.getContentType());

    try (InputStream inputStream = multipartFile.getInputStream()) {
      s3Client.putObject(new PutObjectRequest(bucketName, storeFileName, inputStream, objectMetadata));
    } catch (IOException exception) {
      throw new GlobalException(ErrorCode.FAIL_TO_UPLOAD_FILE);
    }
    String url = s3Client.getUrl(bucketName,storeFileName).toString();
    return UploadFile.of(multipartFile.getOriginalFilename(), url);
  }

  @Override
  public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, String name
      , String chapter) {
    if (multipartFiles == null || multipartFiles.isEmpty()) {
      return List.of();
    }
    return multipartFiles.stream().map(file -> storeFile
        (file, name, chapter)).collect(Collectors.toList());
  }

  @Override
  public void deleteFile(String filename) {
    s3Client.deleteObject(bucketName, filename);
  }

  private String createStoreFileName(String originalFilename, String webtoonName,
      String webtoonChapter) {
    String ext = extractExt(originalFilename);
    String uuid = UUID.randomUUID().toString();
    return webtoonName + CATEGORY_PREFIX + webtoonChapter + CATEGORY_PREFIX + uuid + "." + ext;
  }

  private String extractExt(String originalFilename) {
    int pos = originalFilename.lastIndexOf(".");
    return originalFilename.substring(pos + 1);
  }
}
