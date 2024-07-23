package com.ssafy.bookkoo.libraryservice.service;

import com.ssafy.bookkoo.libraryservice.client.BookServiceClient;
import com.ssafy.bookkoo.libraryservice.dto.RequestCreateLibraryDto;
import com.ssafy.bookkoo.libraryservice.dto.RequestLibraryBookMapperCreateDto;
import com.ssafy.bookkoo.libraryservice.dto.RequestUpdateLibraryDto;
import com.ssafy.bookkoo.libraryservice.dto.ResponseBookDto;
import com.ssafy.bookkoo.libraryservice.dto.ResponseLibraryDto;
import com.ssafy.bookkoo.libraryservice.entity.Library;
import com.ssafy.bookkoo.libraryservice.entity.LibraryBookMapper;
import com.ssafy.bookkoo.libraryservice.entity.LibraryStyle;
import com.ssafy.bookkoo.libraryservice.entity.MapperKey;
import com.ssafy.bookkoo.libraryservice.exception.BookAlreadyMappedException;
import com.ssafy.bookkoo.libraryservice.exception.LibraryNotFoundException;
import com.ssafy.bookkoo.libraryservice.mapper.LibraryBookMapperMapper;
import com.ssafy.bookkoo.libraryservice.mapper.LibraryMapper;
import com.ssafy.bookkoo.libraryservice.repository.LibraryBookMapperRepository;
import com.ssafy.bookkoo.libraryservice.repository.LibraryRepository;
import com.ssafy.bookkoo.libraryservice.repository.LibraryStyleRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;
    private final LibraryStyleRepository libraryStyleRepository;
    private final LibraryBookMapperRepository libraryBookMapperRepository;
    private final LibraryMapper libraryMapper;
    private final LibraryBookMapperMapper libraryBookMapperMapper;
    private final BookServiceClient bookServiceClient;

    @Override
    @Transactional
    public ResponseLibraryDto addLibrary(RequestCreateLibraryDto libraryDto, Long memberId) {
        // library 엔티티 만들기
        Library library = libraryMapper.toEntity(libraryDto, memberId);

        // 서재 스타일 만들기
        LibraryStyle libraryStyle = LibraryStyle.builder()
                                                .libraryColor(libraryDto.libraryStyleDto()
                                                                        .libraryColor())
                                                .library(library)
                                                .build();
        // 서재 스타일 저장하기
        libraryStyleRepository.save(libraryStyle);

        // 서재에 서재 스타일 매핑
        library.setLibraryStyle(libraryStyle);

        // 서재 엔티티 저장하기
        libraryRepository.save(library);

        return libraryMapper.toResponseDto(library);
    }

    @Override
    @Transactional
    public List<ResponseLibraryDto> getLibrariesOfMember(Long memberId) {
        List<Library> libraries = libraryRepository.findByMemberId(memberId);
        return libraryMapper.toResponseDtoList(libraries);
    }

    @Override
    @Transactional
    public ResponseLibraryDto getLibrary(Long libraryId) {
        Library library = findLibraryByIdWithException(libraryId);

        return libraryMapper.toResponseDto(library);
    }

    @Override
    @Transactional
    public ResponseLibraryDto updateLibrary(
        Long libraryId,
        RequestUpdateLibraryDto libraryDto
    ) {
        // 서재 찾기
        Library libraryToUpdate = findLibraryByIdWithException(libraryId);

        // 서재 업데이트
        libraryMapper.updateLibraryFromDto(libraryDto, libraryToUpdate);

        // 서재 스타일 업데이트 로직
        LibraryStyle libraryStyleToUpdate = libraryStyleRepository.findById(libraryToUpdate.getId())
                                                                  .orElseThrow(
                                                                      () -> new LibraryNotFoundException(
                                                                          "LibraryStyle not found"));
        // 있으면 업데이트
        libraryStyleToUpdate.setLibraryColor(libraryDto.libraryStyleDto()
                                                       .libraryColor());

        // 서재 스타일 저장
        libraryStyleRepository.save(libraryStyleToUpdate);
        // 서재 저장
        Library updatedLibrary = libraryRepository.save(libraryToUpdate);

        return libraryMapper.toResponseDto(updatedLibrary);
    }

    @Override
    @Transactional
    public void addBookToLibrary(
        Long libraryId,
        RequestLibraryBookMapperCreateDto mapperDto,
        Long memberId
    ) {
        // 0. 서재 존재 확인
        Library library = findLibraryByIdWithException(libraryId);

        // 1. 해당 책이 DB에 있는지 확인 후 없으면 생성하는 api 요청하기
        ResponseBookDto bookResponse = bookServiceClient.getOrCreateBookByBookData(
            mapperDto.bookDto());

        // 2. 해당 책에 대한 id 값을 받아서 책-서재 매퍼에 추가해 책 등록하기
        // 2.1 mapper key 생성
        MapperKey mapperKey = new MapperKey();
        mapperKey.setLibraryId(libraryId);
        mapperKey.setBookId(bookResponse.id());

        // 2.1.1 이미 존재하는 매퍼가 있는지 확인
        if (libraryBookMapperRepository.existsById(mapperKey)) {
            throw new BookAlreadyMappedException("This book is already mapped to the library.");
        }

        // 2.2 엔티티 생성
        LibraryBookMapper lbMapper = libraryBookMapperMapper.toEntity(mapperDto, mapperKey);
        // lbMapper와 서재 매핑해주기
        lbMapper.setLibrary(library);
        // 2.3 매퍼 엔티티 저장
        libraryBookMapperRepository.save(lbMapper);

    }

    /**
     * 서재에 책 몇권있는지 세기
     *
     * @param memberId : 사용자 ID
     * @return 권수
     */
    @Override
    @Transactional
    public Integer countBooksInLibrary(Long memberId) {
        return libraryBookMapperRepository.countLibrariesByMemberId(memberId);
    }


    private Library findLibraryByIdWithException(Long libraryId) {
        return libraryRepository.findById(libraryId)
                                .orElseThrow(() -> new LibraryNotFoundException(
                                    "Library not found with id : " + libraryId));
    }
}
