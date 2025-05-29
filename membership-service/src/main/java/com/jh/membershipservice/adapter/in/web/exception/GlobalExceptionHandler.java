package com.jh.membershipservice.adapter.in.web.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ApiResponse(
        responseCode = "404", 
        description = "요청한 리소스를 찾을 수 없음",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.warn("No handler found for {} {}", ex.getHttpMethod(), ex.getRequestURL());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .code("NOT_FOUND")
            .message("요청한 리소스를 찾을 수 없습니다")
            .status(HttpStatus.NOT_FOUND.value())
            .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    @ApiResponse(
        responseCode = "500", 
        description = "서버 내부 오류",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred: ", ex);
        
        // 회원을 찾을 수 없는 경우 404 처리
        if (ex.getMessage() != null && ex.getMessage().contains("회원을 찾을 수 없습니다")) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                .code("MEMBER_NOT_FOUND")
                .message("회원을 찾을 수 없습니다")
                .status(HttpStatus.NOT_FOUND.value())
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .code("INTERNAL_SERVER_ERROR")
            .message("서버 내부 오류가 발생했습니다")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ApiResponse(
        responseCode = "400", 
        description = "잘못된 요청",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument exception occurred: ", ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .code("BAD_REQUEST")
            .message(ex.getMessage())
            .status(HttpStatus.BAD_REQUEST.value())
            .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(
        responseCode = "500", 
        description = "예상치 못한 오류",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected exception occurred: ", ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .code("UNEXPECTED_ERROR")
            .message("예상치 못한 오류가 발생했습니다")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
} 