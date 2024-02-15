package com.likelion.oegaein.dto.matching.matchingpost;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.likelion.oegaein.domain.matching.DongType;
import com.likelion.oegaein.domain.matching.RoomSizeType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreateMatchingPostRequest {
    private String title;
    private String content;
    private LocalDate deadline;
    private DongType dongType;
    private RoomSizeType roomSizeType;
}