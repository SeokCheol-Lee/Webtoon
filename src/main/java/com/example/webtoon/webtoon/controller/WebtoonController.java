package com.example.webtoon.webtoon.controller;

import com.example.webtoon.webtoon.domain.dto.CommentDto;
import com.example.webtoon.webtoon.domain.dto.CreateCommentRequest;
import com.example.webtoon.webtoon.domain.dto.UpdateComentRequest;
import com.example.webtoon.webtoon.domain.dto.WebtoonChapterInfo;
import com.example.webtoon.webtoon.domain.dto.WebtoonDto;
import com.example.webtoon.webtoon.domain.dto.WebtoonInfo;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.service.WebtoonService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/webtoon")
@RestController
@RequiredArgsConstructor
public class WebtoonController {

    private final WebtoonService webtoonService;

    @Transactional
    @GetMapping("/search")
    public ResponseEntity<WebtoonDto> searchWebtoon(@RequestParam Long id){
        Webtoon webtoon = webtoonService.getWebtoon(id);
        return ResponseEntity.ok(WebtoonDto.from(webtoon));
    }

    @Transactional
    @GetMapping("/index")
    public ResponseEntity<List<WebtoonInfo>> getAllWebtoons(){
        List<WebtoonInfo> allWebtoons = webtoonService.getAllWebtoons();
        return ResponseEntity.ok(allWebtoons);
    }

    @Transactional
    @GetMapping("/list")
    public ResponseEntity<Page<WebtoonChapterInfo>> getWebtoonChapters(@RequestParam Long webtoonId
    , @PageableDefault(size = 10) Pageable pageable){
        Page<WebtoonChapterInfo> webtoonChapters = webtoonService
            .getWebtoonChapters(webtoonId,pageable);
        return ResponseEntity.ok(webtoonChapters);
    }

    @Transactional
    @GetMapping("/detail")
    public ResponseEntity<WebtoonChapterInfo> getWebtoon(HttpServletRequest request, @RequestParam Long id){
        String ipAddress = request.getRemoteAddr();
        WebtoonChapterInfo webtoonChapter = webtoonService
            .getWebtoonChapter(id, ipAddress);
        return ResponseEntity.ok(webtoonChapter);
    }

    @Transactional
    @GetMapping("/comments-all")
    public ResponseEntity<List<CommentDto>> getAllComments(@RequestParam Long webtoonChapterId){
        List<CommentDto> allComments = webtoonService.getAllComments(webtoonChapterId);
        return ResponseEntity.ok(allComments);
    }

    @Transactional
    @GetMapping("/comment-children")
    public ResponseEntity<List<CommentDto>> getChildrenComments(@RequestParam Long commentId){
        List<CommentDto> childrenComments = webtoonService.getChildrenComments(commentId);
        return ResponseEntity.ok(childrenComments);
    }

    @PostMapping("/addComment")
    public ResponseEntity<String> createComment(@RequestBody CreateCommentRequest request){
        webtoonService.createComment(request);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/modifyComment")
    public ResponseEntity<String> modifyComment(@RequestBody UpdateComentRequest request){
        webtoonService.updateComment(request);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/deleteComment")
    public ResponseEntity<String> deleteComment(@RequestParam Long userId
        , @RequestParam Long commentId){
        webtoonService.deleteComment(userId,commentId);
        return ResponseEntity.ok("Success");
    }
    @PostMapping("/StarScore")
    public ResponseEntity<String> giveStarScore(@RequestParam Long webtoonChapterId,
        @RequestParam Long userId,@RequestParam Long starScore){
        webtoonService.giveStarScore(webtoonChapterId,userId,starScore);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/recommend")
    public ResponseEntity<String> giveRecommend(@RequestParam Long commentId
        , @RequestParam Long userId, boolean recommendType){
        webtoonService.giveRecommend(commentId,userId,recommendType);
        return ResponseEntity.ok("Success");
    }
}
