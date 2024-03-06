package com.likelion.oegaein.domain.matching.dto.matchingpost;

import com.likelion.oegaein.global.dto.ResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateMatchingPostResponse implements ResponseDto {
    private final Long matchingPostId;
}
