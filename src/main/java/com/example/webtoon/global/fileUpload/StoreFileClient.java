package com.example.webtoon.global.fileUpload;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StoreFileClient {

  /**
   * 해당 파일을 반환합니다.
   * @param filename
   * @return
   */
  Resource getFile(String filename);

  /**
   * 하나의 파일을 업로드 합니다.
   * @param multipartFile
   * @return
   */
  UploadFile storeFile(MultipartFile multipartFile, String name, String chapter);

  /**
   * 파일 여러개를 업로드 합니다.
   * @param multipartFiles
   * @return
   */
  List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, String name, String chapter);

  /**
   * 파일을 삭제합니다.
   * @param filename
   */
  void deleteFile(String filename);
}
