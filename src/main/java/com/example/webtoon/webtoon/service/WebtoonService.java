package com.example.webtoon.webtoon.service;

import com.example.webtoon.global.exception.ErrorCode;
import com.example.webtoon.global.exception.GlobalException;
import com.example.webtoon.global.redis.RedisDao;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.user.domain.repository.UserRepository;
import com.example.webtoon.webtoon.domain.dto.CommentDto;
import com.example.webtoon.webtoon.domain.dto.CreateCommentRequest;
import com.example.webtoon.webtoon.domain.dto.UpdateComentRequest;
import com.example.webtoon.webtoon.domain.dto.WebtoonChapterInfo;
import com.example.webtoon.webtoon.domain.dto.WebtoonInfo;
import com.example.webtoon.webtoon.domain.model.Comment;
import com.example.webtoon.webtoon.domain.model.Recommend;
import com.example.webtoon.webtoon.domain.model.StarScore;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.domain.model.WebtoonChapter;
import com.example.webtoon.webtoon.domain.repository.CommentRepository;
import com.example.webtoon.webtoon.domain.repository.RecommendRepository;
import com.example.webtoon.webtoon.domain.repository.StarScoreRepository;
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
    private final CommentRepository commentRepository;
    private final RecommendRepository recommendRepository;
    private final UserRepository userRepository;
    private final StarScoreRepository starScoreRepository;

    @Transactional
    public Webtoon getWebtoon(Long id){
        return this.webtoonRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_WEBTOON));
    }

    public List<WebtoonInfo> getAllWebtoons(){
        List<Webtoon> webtoonList = this.webtoonRepository.findAll();
        return webtoonList.stream().map(WebtoonInfo::of).collect(Collectors.toList());
    }

    public Page<WebtoonChapterInfo> getWebtoonChapters(Long webtoonId, Pageable pageable){
        Webtoon webtoon =  this.webtoonRepository.findById(webtoonId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_WEBTOON));
        Page<WebtoonChapter> chapterList = this.webtoonChapterRepository
            .findAllByWebtoon(webtoon, pageable);
        List<WebtoonChapterInfo> chapterInfoList = chapterList.stream().map(
            WebtoonChapterInfo::of).collect(Collectors.toList());
        return new PageImpl<>(chapterInfoList,pageable,chapterList.getTotalElements());
    }

    public WebtoonChapterInfo getWebtoonChapter(Long chapterId, String ipAddress){
        WebtoonChapter webtoonChapter = this.webtoonChapterRepository.findById(chapterId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NO_EXISTS_WEBTOONCHAPTER));

        Long redisKey = webtoonChapter.getWebtoon().getId();
        if(!redisDao.getValuesHash("viewCount").containsKey(ipAddress)){
            redisDao.setValuesHash("viewCount",ipAddress, redisKey);
            redisDao.countView(redisKey);
        }

        return WebtoonChapterInfo.of(webtoonChapter);
    }

    public List<CommentDto> getAllComments(Long webtoonChapterId){
        WebtoonChapter webtoonChapter = this.webtoonChapterRepository.findById(webtoonChapterId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NO_EXISTS_WEBTOONCHAPTER));
        List<Comment> comments = this.commentRepository
            .findByParentAndIsDeletedIsFalseAndWebtoonChapter(null, webtoonChapter);
        return comments.stream().map(c -> CommentDto.of(
            c, this.recommendRepository.countByCommentAndRecommendTypeIsTrue(c)
                    , this.recommendRepository.countByCommentAndRecommendTypeIsFalse(c))).toList();
    }

    public List<CommentDto> getChildrenComments(Long parentId){
        Comment parent = this.commentRepository.findByIdAndIsDeletedFalse(parentId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_COMMENT));
        List<Comment> comments = this.commentRepository.findByParentAndIsDeletedFalse(parent);
        return comments.stream().map(c -> CommentDto.of(
            c, this.recommendRepository.countByCommentAndRecommendTypeIsTrue(c)
            , this.recommendRepository.countByCommentAndRecommendTypeIsFalse(c))).toList();
    }

    @Transactional
    public void createComment(CreateCommentRequest request){
        User user = this.userRepository.findById(request.getUserId())
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        WebtoonChapter webtoonChapter = this.webtoonChapterRepository.findById(
                request.getWebtoonChapterId())
            .orElseThrow(() -> new GlobalException(ErrorCode.NO_EXISTS_WEBTOONCHAPTER));
        Comment comment = Comment.of(user, webtoonChapter);
        if(request.getCommentId() != null){
            Comment parent = this.commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_COMMENT));
            comment.setParent(parent);
        }
        comment.setContent(request.getContent());
        this.commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(UpdateComentRequest request){
        Comment comment = this.commentRepository.findById(request.getCommentId())
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_COMMENT));
        User user = this.userRepository.findById(request.getUserId())
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        if(comment.getWriter() != user){
            throw new GlobalException(ErrorCode.DENIED_ACCESS_PERMISSION);
        }
        comment.setContent(request.getContent());
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId){
        Comment comment = this.commentRepository.findById(commentId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_COMMENT));
        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        if(comment.getWriter() != user){
            throw new GlobalException(ErrorCode.DENIED_ACCESS_PERMISSION);
        }
        commentRepository.delete(comment);
    }

    @Transactional
    public void giveStarScore(Long webtoonChapterId, Long userId, Long starScore){
        WebtoonChapter webtoonChapter = this.webtoonChapterRepository.findById(webtoonChapterId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NO_EXISTS_WEBTOONCHAPTER));
        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        boolean exists = this.starScoreRepository.existsByUserAndWebtoonChapter(user, webtoonChapter);
        if(exists){
            StarScore star = this.starScoreRepository.findByUserAndWebtoonChapter(
                user, webtoonChapter)
                .orElseThrow(() -> new GlobalException(ErrorCode.DENIED_ACCESS_PERMISSION));
            star.setStarscore(starScore);
        }else{
            this.starScoreRepository.save(StarScore.builder()
                .user(user)
                .webtoonChapter(webtoonChapter)
                .starscore(starScore)
                .build());
        }
    }

    @Transactional
    public void giveRecommend(Long commentId, Long userId, boolean recommendType){
        Comment comment = this.commentRepository.findById(commentId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_COMMENT));
        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        boolean exists = this.recommendRepository.existsByCommentAndUser(comment, user);
        if(exists){
            Recommend recommend = this.recommendRepository.findByCommentAndUser(comment, user);
            if(recommendType != recommend.getRecommendType())
                recommend.setRecommendType(recommendType);
        }else{
            this.recommendRepository.save(
                Recommend.builder()
                    .comment(comment)
                    .user(user)
                    .recommendType(recommendType).build()
            );
        }
    }
}
