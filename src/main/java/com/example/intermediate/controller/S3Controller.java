package com.example.intermediate.controller;

import com.example.intermediate.controller.response.ImageResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class S3Controller {
    private final S3UploaderService s3UploaderService;

//    List<MultipartFile> files;

    @PostMapping("/api/auth/image")
    public ResponseDto<?> imageUpload(@RequestParam("image") MultipartFile multipartFile){

        if(multipartFile.isEmpty()){
            return ResponseDto.fail("INVALID_FILE","파일이 유효하지 않습니다.");
        }
        try{
            return ResponseDto.success(new ImageResponseDto(s3UploaderService.uploadFiles(multipartFile,"static")) );
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.fail("INVALID_FILE","파일이 유효하지 않습니다.");
        }

    }

}
