package com.example.webtoon.webtoon.service;

import com.example.webtoon.global.exception.ErrorCode;
import com.example.webtoon.global.exception.GlobalException;
import com.example.webtoon.global.redis.RedisDao;
import com.example.webtoon.webtoon.domain.dto.WebtoonChapterInfo;
import com.example.webtoon.webtoon.domain.dto.WebtoonInfo;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.domain.model.WebtoonChapter;
import com.example.webtoon.webtoon.domain.repository.WebtoonChapterRepository;
import com.example.webtoon.webtoon.domain.repository.WebtoonRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonChapterRepository webtoonChapterRepository;
    private final RedisDao redisDao;

    @Transactional
    public Webtoon getWebtoon(Long id){
        return this.webtoonRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_WEBTOON));
    }

    public List<WebtoonInfo> getAllWebtoons(){
        List<Webtoon> webtoonList = this.webtoonRepository.findAll();
        return webtoonList.stream().map(w -> WebtoonInfo.builder()
            .id(w.getId())
            .author(w.getAuthor().getNickname())
            .hashtags(w.getHashtagsAsStringList())
            .viewCount(w.getViewCount())
            .starScore(w.getStarScore())
            .build()).collect(Collectors.toList());
    }

    public Page<WebtoonChapterInfo> getWebtoonChapters(Long webtoonId, Pageable pageable){
        Webtoon webtoon =  this.webtoonRepository.findById(webtoonId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_WEBTOON));
        Page<WebtoonChapter> chapterList = this.webtoonChapterRepository
            .findAllByWebtoon(webtoon, pageable);
        List<WebtoonChapterInfo> chapterInfoList = chapterList.stream().map(
            this::webtoonChapterToWebtoonChapterInfo).collect(Collectors.toList());
        return new PageImpl<>(chapterInfoList,pageable,chapterList.getTotalElements());
    }

    public WebtoonChapterInfo getWebtoonChapter(Long chapterId, String ipAddress){
        WebtoonChapter webtoonChapter = this.webtoonChapterRepository.findById(chapterId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NO_EXISTS_WEBTOONCHAPTER));

        Long redisKey = webtoonChapter.getWebtoon().getId();
        Long views = redisDao.getLongValue(redisKey);
        if(!redisDao.getValuesHash("viewCount").containsKey(ipAddress)){
            redisDao.setValuesHash("viewCount",ipAddress, redisKey);
            views += 1;
            redisDao.setLongValue(redisKey, views);
        }

        return webtoonChapterToWebtoonChapterInfo(webtoonChapter);
    }

    private WebtoonChapterInfo webtoonChapterToWebtoonChapterInfo(WebtoonChapter webtoonChapter){
        return WebtoonChapterInfo.builder()
            .id(webtoonChapter.getId())
            .webtoonName(webtoonChapter.getWebtoon().getWebtoonName())
            .chapterName(webtoonChapter.getChapterName())
            .chapterCount(webtoonChapter.getChapterCount())
            .webtoonImgs(webtoonChapter.getWebtoonImgAsStringList())
            .build();
    }
}
