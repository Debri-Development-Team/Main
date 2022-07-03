package com.example.debriserver.core.lecture;

import com.example.debriserver.basicModels.BasicException;
import com.example.debriserver.basicModels.BasicResponse;
import com.example.debriserver.core.lecture.Model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Basic;
import java.util.List;
/**
 * Lecture controller Class
 *
 * @author Rookie/이지호
 * @since 22.07.02
 * @modify -
 * @updated -
 * */
@RestController
@RequestMapping("/api/lecture")
public class LectureController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final LectureService lectureService;
    @Autowired
    private final LectureDao lectureDao;

    public LectureController(LectureService lectureService, LectureDao lectureDao) {
        this.lectureService = lectureService;
        this.lectureDao = lectureDao;
    }

    /**
     * 강좌 추가 API
     * */
    @PostMapping("/add") //(POST)
    public BasicResponse<AddLectureRes> addLecture(@RequestBody AddLectureReq addLectureReq){

        try{
            AddLectureRes addLectureRes = lectureService.addLecture(addLectureReq);

            return new BasicResponse<>(addLectureRes);
        }catch(BasicException exception){
            return new BasicResponse<>((exception.getStatus()));
        }
    }

    /**
     * 강좌 태그 서치 API
     * */
    @PostMapping("/search")//(POST)
    public BasicResponse<List<SearchLectureRes>> searchLecture(@RequestBody SearchLectureReq searchLectureReq) {

        try{
            List<SearchLectureRes> searchLectureRes = lectureService.searchLecture(searchLectureReq);

            return new BasicResponse<>(searchLectureRes);
        }catch (BasicException exception){
            return new BasicResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커리큘럼 구성 API
     * */
    @PostMapping("/collect") //(POST)
    public BasicResponse<CollectLectureRes> collectLecture(@RequestBody CollectLectureReq collectLectureReq) {

        try{
            CollectLectureRes collectLectureRes = lectureService.collectLecture(collectLectureReq);

            return new BasicResponse<>(collectLectureRes);
        }catch (BasicException exception){
            return new BasicResponse<>((exception.getStatus()));
        }
    }

    /**
     * 진행 커리큘럼 변경 API
     * */
    @PatchMapping("/mod") //(PATCH)
    public BasicResponse<ModCurriRes> modCurri(@RequestBody ModCurriReq modCurriReq){

        try{
            ModCurriRes modCurriRes = lectureService.modCurri(modCurriReq);

            return new BasicResponse<>(modCurriRes);
        }catch (BasicException exception){
            return new BasicResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/getlist/{userIdx}")
    public BasicResponse<List<GetCurriListRes>> getCurriList(@PathVariable int userIdx){

        try{
            List<GetCurriListRes> getCurriListRes = lectureService.getCurriList(userIdx);

            return new BasicResponse<>(getCurriListRes);

        }catch (BasicException exception){
            return new BasicResponse<>((exception.getStatus()));
        }
    }

}
