package com.example.debriserver.core.lecture.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CollectLectureReq {
    private int curriIdx;
    private int lectureIdx;
    private int userIdx;
}
