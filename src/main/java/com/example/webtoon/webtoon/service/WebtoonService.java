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
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
        Webtoon webtoon =  this.webtoonRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_WEBTOON));
        return webtoon;
    }

    public List<WebtoonInfo> getAllWebtoons(){
        List<Webtoon> webtoonList = this.webtoonRepository.findAll();
        List<WebtoonInfo> webtoonInfoList = new ArrayList<>();
        for(Webtoon w : webtoonList){
            WebtoonInfo webtoonInfo = WebtoonInfo.builder()
                .id(w.getId())
                .author(w.getAuthor().getNickname())
                .hashtags(w.getHashtagsAsStringList())
                .viewCount(w.getViewCount())
                .starScore(w.getStarScore())
                .build();
            webtoonInfoList.add(webtoonInfo);
        }
        return webtoonInfoList;
    }

    public List<WebtoonChapterInfo> getWebtoonChapters(Long webtoonId){
        Webtoon webtoon =  this.webtoonRepository.findById(webtoonId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_WEBTOON));
        List<WebtoonChapter> chapterList = this.webtoonChapterRepository.findAllByWebtoon(webtoon);
        List<WebtoonChapterInfo> webtoonChapterInfoList = new ArrayList<>();
        for(WebtoonChapter c : chapterList){
            WebtoonChapterInfo chapterInfo = webtoonChapterToWebtoonChapterInfo(c);
            webtoonChapterInfoList.add(chapterInfo);
        }
        return webtoonChapterInfoList;
    }

    public WebtoonChapterInfo getWebtoonChapter(Long chapterId, String ipAddress){
        WebtoonChapter webtoonChapter = this.webtoonChapterRepository.findById(chapterId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NO_EXISTS_WEBTOONCHAPTER));

        String redisKey = String.valueOf(webtoonChapter.getWebtoon().getId());
        String values = redisDao.getValues(redisKey);
        if(!redisDao.getValuesList(ipAddress).contains(redisKey)){
            Long views = Long.parseLong(values);
            redisDao.setValuesList(ipAddress, redisKey);
            views += 1;
            redisDao.setValues(redisKey, String.valueOf(views));
        }
        WebtoonChapterInfo chapterInfo = webtoonChapterToWebtoonChapterInfo(webtoonChapter);

        return chapterInfo;
    }

    private WebtoonChapterInfo webtoonChapterToWebtoonChapterInfo(WebtoonChapter webtoonChapter){
        WebtoonChapterInfo build = WebtoonChapterInfo.builder()
            .id(webtoonChapter.getId())
            .webtoonName(webtoonChapter.getWebtoon().getWebtoonName())
            .chapterName(webtoonChapter.getChapterName())
            .chapterCount(webtoonChapter.getChapterCount())
            .webtoonImgs(webtoonChapter.getWebtoonImgAsStringList())
            .build();
        return build;
    }
}
