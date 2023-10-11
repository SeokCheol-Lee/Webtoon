package com.example.webtoon.global.batch.tasklet;

import com.example.webtoon.global.domain.dto.WebtoonStarScore;
import com.example.webtoon.global.exception.ErrorCode;
import com.example.webtoon.global.exception.GlobalException;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.domain.repository.StarScoreRepository;
import com.example.webtoon.webtoon.domain.repository.WebtoonRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@StepScope
@Slf4j
public class DailyTasklet implements Tasklet {

    private final StarScoreRepository starScoreRepository;
    private final WebtoonRepository webtoonRepository;

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
        List<WebtoonStarScore> webtoonScoreList = starScoreRepository.findByWebtoon();
        for(WebtoonStarScore webtoonStarScore : webtoonScoreList){
            Webtoon webtoon = webtoonRepository.findById(webtoonStarScore.getWebtoonId())
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_WEBTOON));
            webtoon.setStarScore(webtoonStarScore.getStarScore());
        }
        return RepeatStatus.FINISHED;
    }
}
