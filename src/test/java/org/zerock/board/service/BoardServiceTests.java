package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;

import javax.transaction.Transactional;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister()
    {
        BoardDTO dto = BoardDTO.builder()
                .title("Test.")
                .content("Test...")
                .writerEmail("user55@aaa.com")
                .build();

        Long bno = boardService.register(dto);
    }
    @Test
    public void testList()
    {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        for (BoardDTO boardDTO : result.getDtoList()) {
            System.out.println(boardDTO);

        }
    }
    @Test
    public void testGet()
    {
        Long bno = 99L;

        BoardDTO boardDTO = boardService.get(bno);

        System.out.println(boardDTO);
    }
    @Test
    public void testRemove()
    {
        Long bno = 2L;
        boardService.removeWithReplies(bno);
    }
    @Test
    public void testModify()
    {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(99L)
                .title("변경변경1월10일")
                .content("오늘1월10일임")
                .build();

        boardService.modify(boardDTO);
    }

}
