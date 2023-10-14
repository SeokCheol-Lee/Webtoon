package com.example.webtoon.global.batch.tasklet;

import com.example.webtoon.global.redis.RedisDao;
import com.example.webtoon.webtoon.domain.model.Webtoon;
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
public class ViewCountTasklet implements Tasklet {

    private final WebtoonRepository webtoonRepository;
    private final RedisDao redisDao;

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
        List<Webtoon> webtoonList = webtoonRepository.findAll();
        for(Webtoon webtoon : webtoonList){
            Long viewCount = redisDao.getViewCount(webtoon.getId());
            webtoon.setViewCount(viewCount);
        }
        return RepeatStatus.FINISHED;
    }
}
