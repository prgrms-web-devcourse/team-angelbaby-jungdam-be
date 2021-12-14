package com.jungdam.diary.facade;

import com.jungdam.album.application.AlbumService;
import com.jungdam.album.domain.Album;
import com.jungdam.diary.application.DiaryService;
import com.jungdam.diary.domain.Diary;
import com.jungdam.diary.dto.bundle.CreateDiaryBundle;
import com.jungdam.diary.dto.response.CreateDiaryResponse;
import com.jungdam.error.ErrorMessage;
import com.jungdam.error.exception.NotExistException;
import com.jungdam.member.application.MemberService;
import com.jungdam.member.domain.Member;
import com.jungdam.participant.application.ParticipantService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DiaryFacade {

    private final MemberService memberService;
    private final AlbumService albumService;
    private final DiaryService diaryService;
    private final ParticipantService participantService;

    public DiaryFacade(MemberService memberService,
        AlbumService albumService, DiaryService diaryService,
        ParticipantService participantService) {
        this.memberService = memberService;
        this.albumService = albumService;
        this.diaryService = diaryService;
        this.participantService = participantService;
    }

    @Transactional
    public CreateDiaryResponse insert(CreateDiaryBundle bundle) {
        Album album = albumService.findById(bundle.getAlbumId());
        Member member = memberService.findById(bundle.getMemberId());

        if (!participantService.existsByAlbumAndMember(album, member)) {
            throw new NotExistException(ErrorMessage.NOT_EXIST_PARTICIPANT);
        }

        Diary diary = diaryService.save(bundle.getTitle(), bundle.getContent(),
            bundle.getRecordedAt(), bundle.getDiaryPhotos(), member);

        album.addDiary(diary);

        return new CreateDiaryResponse(album.getId(), diary.getId());
    }
}