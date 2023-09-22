package com.example.webtoon.webtoon.service;

import com.example.webtoon.global.exception.ErrorCode;
import com.example.webtoon.global.exception.GlobalException;
import com.example.webtoon.global.fileUpload.StoreFile;
import com.example.webtoon.global.fileUpload.UploadFile;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.user.domain.repository.UserRepository;
import com.example.webtoon.webtoon.domain.dto.CreateWebtoonRequest;
import com.example.webtoon.webtoon.domain.dto.UploadWebtoonRequest;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.domain.model.WebtoonChapter;
import com.example.webtoon.webtoon.domain.model.WebtoonHashtag;
import com.example.webtoon.webtoon.domain.model.WebtoonImg;
import com.example.webtoon.webtoon.domain.repository.WebtoonChapterRepository;
import com.example.webtoon.webtoon.domain.repository.WebtoonRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class WebtoonManageService {

    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;
    private final WebtoonChapterRepository webtoonChapterRepository;
    private final StoreFile storeFile;

    @Transactional
    public Webtoon createWebtoon(CreateWebtoonRequest request, String email){
        User user = this.userRepository.findUserByEmail(email)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        Set<WebtoonHashtag> hashtags = request.getHashtags().stream()
            .map(WebtoonHashtag::of)
            .collect(Collectors.toSet());
        return webtoonRepository.save(
            Webtoon.builder()
                .author(user)
                .hashtags(hashtags)
                .webtoonName(request.getWebtoonName())
                .uploadDay(request.getUploadDay())
                .viewCount(0L)
                .starScore(0.0)
                .build());
    }

    @Transactional
    public WebtoonChapter uploadWebtoonChapter(String email, UploadWebtoonRequest request,
        List<MultipartFile> imgs){
        User user = userRepository.findUserByEmail(email)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        Webtoon webtoon = webtoonRepository.findByWebtoonName(request.getWebtoonName())
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_WEBTOON));
        if(webtoon.getAuthor() != user){
            new GlobalException(ErrorCode.DENIED_ACCESS_PERMISSION);
        }
        List<UploadFile> uploadFiles = storeFile.storeFiles(imgs,request.getWebtoonName(),
            request.getChapterName());
        List<WebtoonImg> webtoonImgs = uploadFiles.stream()
            .map(UploadFile::toWebtoonImage)
            .collect(Collectors.toList());
        Integer counts = webtoonChapterRepository.countByWebtoon(webtoon);
        return webtoonChapterRepository.save(
            WebtoonChapter.builder()
                .webtoon(webtoon)
                .chapterName(request.getChapterName())
                .chapterCount(counts+1)
                .webtoonImgs(webtoonImgs)
                .build());
    }
}
