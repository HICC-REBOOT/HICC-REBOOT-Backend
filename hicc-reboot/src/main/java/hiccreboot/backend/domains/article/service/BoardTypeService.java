package hiccreboot.backend.domains.article.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.exception.BoardTypeNotFoundException;
import hiccreboot.backend.common.exception.DuplicateBoardTypeException;
import hiccreboot.backend.domains.article.domain.BoardTypeEntity;
import hiccreboot.backend.domains.article.dto.response.FindBoardTypesResponse;
import hiccreboot.backend.domains.article.repository.BoardTypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardTypeService {

	private final BoardTypeRepository boardTypeRepository;

	// 게시판 목록 조회
	public List<FindBoardTypesResponse> findBoardTypes() {
		return boardTypeRepository.findAll()
			.stream()
			.map(FindBoardTypesResponse::from)
			.toList();
	}

	// 게시판 목록 추가
	@Transactional
	public void createBoardType(String name) {
		if (name.equals("전체게시판")) {
			throw DuplicateBoardTypeException.EXCEPTION;
		}
		if (boardTypeRepository.existsByName(name)) {
			throw DuplicateBoardTypeException.EXCEPTION;
		}

		boardTypeRepository.save(new BoardTypeEntity(name));
	}

	@Transactional
	public void deleteBoardType(Long boardTypeId) {
		boardTypeRepository.deleteById(boardTypeId);
	}

	@Transactional
	public void updateBoardType(Long boardTypeId, String name) {
		BoardTypeEntity boardType = boardTypeRepository.findById(boardTypeId)
			.orElseThrow(() -> BoardTypeNotFoundException.EXCEPTION);

		boardType.updateName(name);
	}
}
