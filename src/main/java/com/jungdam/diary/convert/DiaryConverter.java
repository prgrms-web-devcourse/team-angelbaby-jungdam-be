package com.jungdam.diary.convert;

import com.jungdam.album.domain.Album;
import com.jungdam.diary.domain.Diary;
import com.jungdam.diary.dto.bundle.CreateDiaryBundle;
import com.jungdam.diary.dto.response.CheckBookmarkResponse;
import com.jungdam.diary.dto.response.CreateDiaryResponse;
import com.jungdam.diary.dto.response.DeleteDiaryResponse;
import com.jungdam.diary.dto.response.ReadDiaryResponse;
import com.jungdam.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class DiaryConverter {

    public CheckBookmarkResponse toCheckBookmarkResponse(Diary diary) {
        return new CheckBookmarkResponse(diary.getId(), diary.getBookmarkValue());
    }

    public CreateDiaryResponse toCreateDiaryResponse(Diary diary) {
        return new CreateDiaryResponse(diary.getAlbumValue(), diary.getId());
    }

    public ReadDiaryResponse toReadDiaryResponse(Diary diary) {
        return ReadDiaryResponse.builder()
            .albumId(diary.getAlbumValue())
            .diaryId(diary.getId())
            .title(diary.getTitleValue())
            .content(diary.getContentValue())
            .bookmark(diary.getBookmarkValue())
            .diaryPhotos(diary.getDiaryPhotosValue())
            .recordedAt(diary.getRecordedAtValue())
            .build();
    }

    public Diary toDiary(CreateDiaryBundle bundle, Member member) {
        return Diary.builder()
            .title(bundle.getTitle())
            .content(bundle.getContent())
            .recordedAt(bundle.getRecordedAt())
            .member(member)
            .build();
    }

    public DeleteDiaryResponse toDeleteDiaryResponse(Album album) {
        return new DeleteDiaryResponse(album.getId());
    }
}