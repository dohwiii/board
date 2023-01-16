package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository repository;
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {

        log.info(dto);

        Board board = dtoToEntity(dto); //dto -> entity
        repository.save(board);

        return board.getBno();
    }

    //목록(페이징) 보기
    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO)
    {
        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0], (Member)en[1], (Long)en[2])); //entity -> DTO

        //result은 entity
//        Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));
        Page<Object[]> result = repository.searchPage(pageRequestDTO.getType(), pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending()));

        //PageResultDTO<DTO, EN>
        return new PageResultDTO<>(result, fn);

    }
    //게시물 조회
    @Override
    public BoardDTO get(Long bno) {

        Object result = repository.getBoardByBno(bno); //entity형태로 가져옴
        Object[] arr = (Object[])result;

        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }
    @Transactional
    @Override
    public void removeWithReplies(Long bno)
    {
        replyRepository.deleteByBno(bno); //댓글 삭제

        repository.deleteById(bno); //게시물 삭제

    }
    @Transactional
    @Override
    public void modify(BoardDTO boardDTO)
    {
        Optional<Board> result = repository.findById(boardDTO.getBno());
        Board board = result.get(); //원래 게시물 내용
//        Board board = repository.getById(boardDTO.getBno());

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());

        repository.save(board);
    }

}
