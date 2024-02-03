package com.example.webtoon.webtoon.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.webtoon.global.exception.ErrorCode;
import com.example.webtoon.global.exception.GlobalException;
import com.example.webtoon.global.fileUpload.StoreFileClient;
import com.example.webtoon.global.fileUpload.UploadFile;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.user.domain.repository.UserRepository;
import com.example.webtoon.webtoon.domain.dto.CreateWebtoonRequest;
import com.example.webtoon.webtoon.domain.dto.UploadWebtoonRequest;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.domain.model.WebtoonChapter;
import com.example.webtoon.webtoon.domain.repository.WebtoonChapterRepository;
import com.example.webtoon.webtoon.domain.repository.WebtoonRepository;
import com.example.webtoon.webtoon.domain.tyoe.Day;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class WebtoonManageServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private WebtoonManageService webtoonManageService;
    @Mock
    private WebtoonChapterRepository webtoonChapterRepository;
    @Mock
    private WebtoonRepository webtoonRepository;
    @Mock
    private StoreFileClient storeFileClient;
    private final User user = User.builder()
        .id(1L)
        .nickname("test")
        .email("aa@naver.com")
        .password("encodePassword")
        .build();
    private final Webtoon webtoon = Webtoon.builder()
        .author(user)
        .webtoonName("test")
        .build();

    private List<String> hashtags = new ArrayList<>();

    @Test
    void createWebtoon() {
        //given
        ArgumentCaptor<Webtoon> captor = ArgumentCaptor.forClass(Webtoon.class);
        given(userRepository.findUserByEmail(any()))
            .willReturn(Optional.ofNullable(user));
        hashtags.add("cate");
        CreateWebtoonRequest request = CreateWebtoonRequest.builder()
            .webtoonName("test")
            .uploadDay(Day.MON)
            .hashtags(hashtags)
            .build();
        //when
        webtoonManageService.createWebtoon(request, user.getEmail());
        //then
        verify(webtoonRepository).save(captor.capture());
        Webtoon webtoon = captor.getValue();
        assertEquals(user,webtoon.getAuthor());
        assertEquals(request.getWebtoonName(),webtoon.getWebtoonName());
    }
    @DisplayName("웹툰 생성 실패 - 이미 동일한 이름의 웹툰이 존재")
    @Test
    void createWebtoon_ExistName() {
        //given
        given(userRepository.findUserByEmail(any()))
            .willReturn(Optional.ofNullable(user));
        given(webtoonRepository.existsByWebtoonName(any()))
            .willReturn(true);
        CreateWebtoonRequest request = CreateWebtoonRequest.builder()
            .webtoonName("test")
            .uploadDay(Day.MON)
            .hashtags(hashtags)
            .build();
        //when
        GlobalException exception = assertThrows(GlobalException.class,
            () -> webtoonManageService.createWebtoon(request, user.getEmail()));
        //then
        assertEquals(ErrorCode.EXISTS_WEBTOONNAME, exception.getErrorCode());
    }

    @Test
    void uploadWebtoonChapter() {
        //given
        List<UploadFile> uploadFileList = new ArrayList<>();
        UploadFile uploadFile = UploadFile.of("test","test");
        uploadFileList.add(uploadFile);
        given(userRepository.findUserByEmail(any()))
            .willReturn(Optional.ofNullable(user));
        given(webtoonRepository.findById(any()))
            .willReturn(Optional.ofNullable(webtoon));
        given(storeFileClient.storeFiles(any(),any(),any()))
            .willReturn(uploadFileList);
        given(webtoonChapterRepository.countByWebtoon(any()))
            .willReturn(2);
        UploadWebtoonRequest request = UploadWebtoonRequest.builder()
            .webtoonName("test")
            .chapterName("test")
            .build();
        List<MultipartFile> list = new ArrayList<>();
        ArgumentCaptor<WebtoonChapter> captor = ArgumentCaptor.forClass(WebtoonChapter.class);
        //when
        webtoonManageService.uploadWebtoonChapter(user.getEmail(),request,list);
        //then
        verify(webtoonChapterRepository).save(captor.capture());
        WebtoonChapter webtoonChapter = captor.getValue();
        assertEquals("test",webtoonChapter.getChapterName());
        assertEquals(3,webtoonChapter.getChapterCount());
    }
    @DisplayName("업로드 실패 - 저자가 다름")
    @Test
    void uploadWebtoonChapter_DeniedAccess() {
        //given
        User user2 = User.builder()
            .id(2L)
            .nickname("test2")
            .email("aa2@naver.com")
            .password("encodePassword")
            .build();
        Webtoon webtoon2 = Webtoon.builder()
            .author(user2)
            .webtoonName("test")
            .build();
        List<UploadFile> uploadFileList = new ArrayList<>();
        UploadFile uploadFile = UploadFile.of("test","test");
        uploadFileList.add(uploadFile);
        given(userRepository.findUserByEmail(any()))
            .willReturn(Optional.ofNullable(user));
        given(webtoonRepository.findById(any()))
            .willReturn(Optional.ofNullable(webtoon2));
        UploadWebtoonRequest request = UploadWebtoonRequest.builder()
            .webtoonName("test")
            .chapterName("test")
            .build();
        List<MultipartFile> list = new ArrayList<>();
        //when
        GlobalException exception = assertThrows(GlobalException.class,
            () -> webtoonManageService.uploadWebtoonChapter(user.getEmail(),request,list));
        //then
        assertEquals(ErrorCode.DENIED_ACCESS_PERMISSION, exception.getErrorCode());
    }
}