package com.example.webtoon.global.fileUpload;

import com.example.webtoon.webtoon.domain.model.WebtoonImg;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {
  private String uploadFileName;
  private String storeFileName;

  public UploadFile(String uploadFileName, String storeFileName) {
    this.uploadFileName = uploadFileName;
    this.storeFileName = storeFileName;
  }

  public static UploadFile of(String uploadFileName, String storeFileName) {
    return new UploadFile(uploadFileName, storeFileName);
  }

  public WebtoonImg toWebtoonImage() {
    return WebtoonImg.of(storeFileName);
  }
}
