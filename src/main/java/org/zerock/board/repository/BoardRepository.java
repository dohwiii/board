package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

    @Query("SELECT b, w FROM Board b LEFT JOIN b.writer w WHERE b.bno = :bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query("SELECT b, r FROM Board b left join Reply r ON r.board = b WHERE b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    //한 게시물에 작성자와 달린 댓글 수 보기
    @Query(value = "SELECT b, w, count(r) " + " FROM Board b " + " LEFT JOIN b.writer w "
            + " LEFT JOIN Reply r ON r.board = b " + " GROUP BY b", countQuery = "SELECT count(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    //게시물 번호로 게시물과 작성자, 댓글 수 보기
    @Query("SELECT b, w, count(r) " + " FROM Board b LEFT JOIN b.writer w " + " LEFT OUTER JOIN Reply r ON r.board=b "+
            " WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

}
