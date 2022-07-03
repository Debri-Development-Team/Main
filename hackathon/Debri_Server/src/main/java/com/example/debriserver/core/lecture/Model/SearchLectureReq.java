package com.example.debriserver.core.lecture.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchLectureReq {
    private String language;
    private int difficulty;
    private String lectureKind;
    private String pricing;
    private String lectureName;
}
