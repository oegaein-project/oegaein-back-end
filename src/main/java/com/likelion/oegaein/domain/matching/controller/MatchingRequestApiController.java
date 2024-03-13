package com.likelion.oegaein.domain.matching.controller;

import com.likelion.oegaein.domain.matching.dto.matchingrequest.*;
import com.likelion.oegaein.domain.matching.service.MatchingRequestService;
import com.likelion.oegaein.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MatchingRequestApiController {
    private final MatchingRequestService matchingRequestService;

    @GetMapping("/api/v1/my-matchingrequests") // 내가 신청한 매칭 신청 목록
    public ResponseEntity<ResponseDto> getMyMatchingRequests(){
        log.info("Request to get my matching requests");
        FindMyMatchingReqsResponse response = matchingRequestService.findMyMatchingRequest();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/v1/come-matchingrequests") // 나에게 온 매칭 신청 목록
    public ResponseEntity<ResponseDto> getComeMatchingRequests(){
        log.info("Request to get come matching requests");
        FindComeMatchingReqsResponse response = matchingRequestService.findComeMatchingRequest();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/v1/matchingrequests") // 매칭 신청 등록
    public ResponseEntity<ResponseDto> postMatchingRequest(@RequestBody CreateMatchingReqRequest dto){
        log.info("Request to post matching request");
        CreateMatchingReqData convertedDto = CreateMatchingReqData.toCreateMatchingReqData(dto);
        CreateMatchingReqResponse response = matchingRequestService.createMatchingRequest(convertedDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/api/v1/matchingrequests/{matchingrequestid}") // 매칭 신청 취소
    public ResponseEntity<ResponseDto> deleteMatchingRequest(@PathVariable("matchingrequestid") Long matchingRequestId){
        log.info("Request to delete matching request");
        matchingRequestService.removeMatchingRequest(matchingRequestId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/api/v1/matchingrequest/{matchingrequestsid}/accept")
    public ResponseEntity<ResponseDto> acceptMatchingRequest(@PathVariable("matchingrequestid") Long matchingRequestId){
        log.info("Request to accept matching request");
        AcceptMatchingReqResponse response = matchingRequestService.acceptMatchingRequest(matchingRequestId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/api/v1/matchingrequest/{matchingrequestsid}/reject")
    public ResponseEntity<ResponseDto> rejectMatchingRequest(@PathVariable("matchingrequestid") Long matchingRequestId){
        log.info("Request to accept matching request");
        RejectMatchingReqResponse response = matchingRequestService.rejectMatchingRequest(matchingRequestId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
