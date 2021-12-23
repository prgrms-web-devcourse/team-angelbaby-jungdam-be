package com.jungdam.diary.infrastructure;

import com.jungdam.album.domain.Album;
import com.jungdam.diary.domain.Diary;
import com.jungdam.diary.domain.vo.Bookmark;
import com.jungdam.diary.domain.vo.RecordedAt;
import com.jungdam.participant.domain.Participant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    boolean existsByRecordedAtAndParticipant(RecordedAt recordedAt, Participant participant);

    @Override
    Optional<Diary> findById(Long id);

    Boolean existsByAlbumAndBookmarkAndIdLessThan(Album album, Bookmark bookmark, Long id);

    List<Diary> findAllByAlbumOrderByRecordedAtDescCreatedAtDesc(Album album, Pageable page);

    List<Diary> findAllByAlbumAndRecordedAtLessThanOrderByRecordedAtDescCreatedAtDesc(Album album,
        RecordedAt recordedAt, Pageable page);

    Boolean existsByAlbumAndRecordedAtLessThan(Album album, RecordedAt recordedAt);

    List<Diary> findAllByAlbumAndParticipantOrderByRecordedAtDesc(Album album,
        Participant participant,
        Pageable pageable);

    List<Diary> findAllByAlbumAndBookmarkOrderByRecordedAtDesc(Album album, Bookmark bookmark,
        Pageable page);

    List<Diary> findAllByAlbumAndBookmarkAndIdLessThanOrderByRecordedAtDesc(Album album,
        Bookmark bookmark, Long cursorId,
        Pageable page);

    List<Diary> findAllByAlbumAndParticipant(Album album, Participant participant,
        Pageable pageable);

    @Query(value = "select * from diary "
        + "where album_id = :album "
        + "and participant_id = :participant "
        + "and diary_recorded_at < :recordedAt "
        + "or (diary_recorded_at = :recordedAt and diary_id < :id) ",
        nativeQuery = true)
    List<Diary> findAllByAlbumAndParticipantCursor(
        @Param("album") Long album,
        @Param("participant") Long participant,
        @Param("id") Long cursorId,
        @Param("recordedAt") String recordedAt, Pageable pageable);

    @Query(value = "select * from diary "
        + "where album_id = :album "
        + "and participant_id = :participant "
        + "and diary_recorded_at < :recordedAt "
        + "or (diary_recorded_at = :recordedAt and diary_id < :id) "
        + "limit 1",
        nativeQuery = true)
    Optional<Diary> existsByAlbumAndParticipantCursor(
        @Param("album") Long album,
        @Param("participant") Long participant,
        @Param("id") Long cursorId,
        @Param("recordedAt") String recordedAt);
}