package com.sca.controller;


import com.sca.common.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author yuit
 * @date  2018/3/30 20:37
 *
 */
@RestControllerAdvice
public final class ControllerExceptionHandler {

    Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity unKnowExceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseUtils.error(e.getMessage());
    }


}
