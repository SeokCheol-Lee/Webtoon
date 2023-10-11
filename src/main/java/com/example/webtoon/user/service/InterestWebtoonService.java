package com.example.webtoon.user.service;

import com.example.webtoon.global.exception.ErrorCode;
import com.example.webtoon.global.exception.GlobalException;
import com.example.webtoon.user.domain.dto.InterestWebtoonDto;
import com.example.webtoon.user.domain.model.InterestWebtoon;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.user.domain.repository.InterestWebtoonRepository;
import com.example.webtoon.user.domain.repository.UserRepository;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.domain.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InterestWebtoonService {

    private final InterestWebtoonRepository interestWebtoonRepository;
    private final WebtoonRepository webtoonRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createInterestWebtoon(Long userId, Long webtoonId){
        Webtoon webtoon =  this.webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_WEBTOON));
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        InterestWebtoon interestWebtoon = InterestWebtoon.of(user, webtoon);
        boolean exists = this.interestWebtoonRepository.existsByWebtoonAndUser(webtoon, user);
        if(!exists){
            this.interestWebtoonRepository.save(interestWebtoon);
        }else{
            this.interestWebtoonRepository.delete(interestWebtoon);
        }
    }

    public List<InterestWebtoonDto> getInterestWebtoons(Long userId){
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        List<InterestWebtoon> interestWebtoonList = this.interestWebtoonRepository.findAllByUser(user);
        return interestWebtoonList.stream().map(InterestWebtoonDto::of).collect(Collectors.toList());
    }

}
