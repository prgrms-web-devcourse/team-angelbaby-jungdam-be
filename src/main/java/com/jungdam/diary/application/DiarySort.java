package com.jungdam.diary.application;

import com.jungdam.diary.domain.Diary;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.TypedSort;

public class DiarySort {

    private final static TypedSort<Diary> diaryTypedSort = Sort.sort(Diary.class);

    public static Sort recordedAtDescIdDesc() {
        return diaryTypedSort.by(Diary::getRecordedAt).descending()
            .and(diaryTypedSort.by(Diary::getId).descending());
    }

    public static Sort rawRecordedAtDescIdDesc() {
        return Sort.by("diary_recorded_at").descending()
            .and(Sort.by("diary_id").descending());
    }
}
