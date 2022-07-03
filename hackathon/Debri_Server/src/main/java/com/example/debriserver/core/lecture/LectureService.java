package com.example.debriserver.core.lecture;

import com.example.debriserver.basicModels.BasicException;
import com.example.debriserver.basicModels.BasicResponse;
import com.example.debriserver.basicModels.BasicServerStatus;
import com.example.debriserver.core.lecture.Model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;

/**
 * Lecture service Class
 *
 * @author Rookie/이지호
 * @since 22.07.02
 * @modify -
 * @updated -
 * */
@Service
public class LectureService {
    private final LectureDao lectureDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LectureService(LectureDao lectureDao){
        this.lectureDao = lectureDao;
    }

    public AddLectureRes addLecture(AddLectureReq addLectureReq) throws BasicException {
        try{
            return lectureDao.addLecture(addLectureReq);
        }catch(Exception exception){
            throw new BasicException(BasicServerStatus.DB_ERROR);
        }
    }

   public List<SearchLectureRes> searchLecture(SearchLectureReq searchLectureReq) throws BasicException {

        try{
            List<SearchLectureRes> lectureList = lectureDao.searchLecture(searchLectureReq);


            return lectureList;
        }catch (Exception exception){
            throw new BasicException(BasicServerStatus.DB_ERROR);
        }
    }

    public CollectLectureRes collectLecture(CollectLectureReq collectLectureReq) throws BasicException{

        try{
            CollectLectureRes collectLectureRes = lectureDao.collectLecture(collectLectureReq);

            return collectLectureRes;
        }catch(Exception exception){
            logger.debug("CustomError", exception.getMessage());
            System.out.println(exception.getMessage());
            throw new BasicException(BasicServerStatus.DB_ERROR);
        }
    }

    public ModCurriRes modCurri(ModCurriReq modCurriReq) throws BasicException{

        try{
            ModCurriRes modCurriRes = lectureDao.modCurri(modCurriReq);

            return modCurriRes;
        }catch(Exception exception){

            throw new BasicException(BasicServerStatus.DB_ERROR);
        }
    }

    public List<GetCurriListRes> getCurriList(@PathVariable int userIdx) throws BasicException{

        try{
            List<GetCurriListRes> getCurriListRes = lectureDao.getCurriList(userIdx);
            if(getCurriListRes.isEmpty()) throw new BasicException(BasicServerStatus.LECTURE_LIST_NOT_EXIST);

            return getCurriListRes;

        } catch (Exception exception){
            throw new BasicException(BasicServerStatus.LECTURE_LIST_NOT_EXIST);
        }
    }
}
