package com.example.debriserver.core.lecture;

import com.example.debriserver.basicModels.BasicException;
import com.example.debriserver.basicModels.BasicResponse;
import com.example.debriserver.basicModels.BasicServerStatus;
import com.example.debriserver.core.lecture.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import javax.sql.DataSource;
import java.util.List;
/**
 * Lecture Dao Class
 *
 * @author Rookie/이지호
 * @since 22.07.02
 * @modify -
 * @updated -
 * */
@Repository
public class LectureDao {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public void getDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public AddLectureRes addLecture(AddLectureReq addLectureReq){
        AddLectureRes addLectureRes = new AddLectureRes(true);
        //새로운 Lecture를 생성하는 쿼리
        String query = "INSERT INTO Lecture\n" +
                "    (lectureName, lectureDescription, language, difficulty,\n" +
                "     lectureKind, pricing, materialUrl, imgUrl)\n" +
                "    VALUE(?, ?, ?, ?, ?, ?, ?, ?);";
        Object[] addLectureParameters = new Object[]
                {
                        addLectureReq.getLectureName(),
                        addLectureReq.getLectureDescription(),
                        addLectureReq.getLanguage(),
                        addLectureReq.getDifficulty(),
                        addLectureReq.getLectureKind(),
                        addLectureReq.getPricing(),
                        addLectureReq.getMaterialUrl(),
                        addLectureReq.getImgUrl()
                };

        int result = this.jdbcTemplate.update(query, addLectureParameters);

        //Insert가 정상적으로 이루어졌는지 체크
        if(result > 0) addLectureRes.setAddResult(true);
        else addLectureRes.setAddResult(false);

        return addLectureRes;
    }

    public List<SearchLectureRes> searchLecture(SearchLectureReq searchLectureReq){
        //4개의 태그를 단순히 하나라도 가지고 있다면 해당 라인을 뽑아내는 쿼리 *(더 정확한 검색을 위해 수정필요)
        String query ="SELECT lectureName, lectureDescription, language, difficulty, lectureKind, pricing, materialUrl, imgUrl, lectureIdx\n" +
                "FROM Lecture\n" +
                "WHERE difficulty = ?\n" +
                "UNION\n" +
                "SELECT lectureName, lectureDescription, language, difficulty, lectureKind, pricing, materialUrl, imgUrl, lectureIdx\n" +
                "FROM Lecture\n" +
                "WHERE lectureKind = ?\n" +
                "UNION\n" +
                "SELECT lectureName, lectureDescription, language, difficulty, lectureKind, pricing, materialUrl, imgUrl, lectureIdx\n" +
                "FROM Lecture\n" +
                "WHERE pricing = ?\n" +
                "UNION\n" +
                "SELECT lectureName, lectureDescription, language, difficulty, lectureKind, pricing, materialUrl, imgUrl, lectureIdx\n" +
                "FROM Lecture\n" +
                "WHERE language = ?;";
        Object[] searchLectureParameters = new Object[]
                {
                        searchLectureReq.getDifficulty(),
                        searchLectureReq.getLectureKind(),
                        searchLectureReq.getPricing(),
                        searchLectureReq.getLanguage()
                };

        return this.jdbcTemplate.query(query,
                ((rs, rowNum) -> new SearchLectureRes(
                        rs.getString("lectureName"),
                        rs.getString("lectureDescription"),
                        rs.getString("language"),
                        rs.getInt("difficulty"),
                        rs.getString("lectureKind"),
                        rs.getString("pricing"),
                        rs.getString("materialUrl"),
                        rs.getString("imgUrl"),
                        rs.getInt("lectureIdx")
                )), searchLectureParameters);
    }

    public CollectLectureRes collectLecture(CollectLectureReq collectLectureReq){
        CollectLectureRes collectLectureRes = new CollectLectureRes(true);

        //커리큘럼을 새로 만드는 경우
        if(collectLectureReq.getCurriIdx() == -1)
        {
            //새로운 커리큘럼 리스트를 만들기 위한 쿼리
            String newListQuery = "INSERT INTO Lecture_list(userIdx) VALUE (?);";
            //방금 만든 커리큘럼의 curriIdx값을 뽑는 쿼리
            String lastIdxQuery = "SELECT LAST_INSERT_ID();";
            //방금 만든 커리큘럼을 제외한 나머지 유저 자신이 만든 커리큘럼을 비활성화 하는 쿼리
            String statusUpdateQuery = "UPDATE Lecture_list SET status = 'INACTIVE' WHERE userIdx = ?;";

            //모두 비활성화 하고 -> 새로 만들고
            int newListParameters = collectLectureReq.getUserIdx();
            this.jdbcTemplate.update(statusUpdateQuery, collectLectureReq.getUserIdx());
            this.jdbcTemplate.update(newListQuery, newListParameters);

            //방금 만든 커리큘럼 Idx뽑기
            collectLectureReq.setCurriIdx(this.jdbcTemplate.queryForObject(lastIdxQuery,
                    ((rs, rowNum) -> rs.getInt("LAST_INSERT_ID()"))));

        }
            //커리큘럼에 새로운 강좌를 삽입하는 쿼리
        String query = "INSERT INTO List_Lecture(curriIdx, lectureIdx) VALUE (?, ?);";
        Object[] collectLectureParameters = new Object[]
                {
                        collectLectureReq.getCurriIdx(),
                        collectLectureReq.getLectureIdx()
                };


        int result = this.jdbcTemplate.update(query, collectLectureParameters);
        // 삽입이 재대로 되었는지 체크
        if(result > 0) collectLectureRes.setCollectSuccess(true);
        else collectLectureRes.setCollectSuccess(false);

        return collectLectureRes;
    }

    public ModCurriRes modCurri(ModCurriReq modCurriReq){
        ModCurriRes modCurriRes = new ModCurriRes(true);
        //해당 유저의 모든 커리큘럼을 비활성화 하는 쿼리
        String setInactiveQuery = "UPDATE Lecture_list SET status = 'INACTIVE' WHERE  userIdx = ?;";
        //선택한 커리큘럼을 활성화 하는 쿼리
        String setActiveQuery = "UPDATE Lecture_list SET status = 'ACTIVE' WHERE  curriIdx = ?;";

        this.jdbcTemplate.update(setInactiveQuery, modCurriReq.getUserIdx());
        int result = this.jdbcTemplate.update(setActiveQuery, modCurriReq.getCurriIdx());
        
        //업데이트 되었는지 체크
        if(result > 0) modCurriRes.setModSuccess(true);
        else modCurriRes.setModSuccess(false);

        return modCurriRes;
    }

    public List<GetCurriListRes> getCurriList(@PathVariable int userIdx) throws BasicException{
        String getLectureListQuery = "SELECT lectureName, lectureDescription, language, difficulty, lectureKind, pricing, materialUrl, imgUrl, l.lectureIdx\n" +
                "FROM Lecture as l JOIN (SELECT lectureIdx FROM List_Lecture WHERE curriIdx =\n" +
                "(SELECT curriIdx FROM Lecture_list WHERE userIdx = ? and status = 'ACTIVE')) as k on l.lectureIdx = k.lectureIdx;";

        int getLectuerIdxParameter = userIdx;


        return this.jdbcTemplate.query(getLectureListQuery,
                ((rs, rowNum) -> new GetCurriListRes(
                        rs.getString("lectureName"),
                        rs.getString("lectureDescription"),
                        rs.getString("language"),
                        rs.getInt("difficulty"),
                        rs.getString("lectureKind"),
                        rs.getString("pricing"),
                        rs.getString("materialUrl"),
                        rs.getString("imgUrl"),
                        rs.getInt("lectureIdx")
                )), userIdx);

    }
}