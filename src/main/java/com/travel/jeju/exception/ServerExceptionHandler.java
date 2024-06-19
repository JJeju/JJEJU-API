package com.travel.jeju.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;

import com.travel.jeju.model.common.BaseErrorModel;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ServerExceptionHandler {


    @ExceptionHandler(AppException.class)
    public ResponseEntity<BaseErrorModel> handleAppException(AppException e) {

        ExceptionCode errorType = e.getErrorCode();

        BaseErrorModel baseBody = new BaseErrorModel();
        baseBody.setCode(errorType.code());
        baseBody.setDesc(errorType.message());
        
        e.printStackTrace();

        return ResponseEntity.status(errorType.status()).body(baseBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseErrorModel> handleException(Exception e) {

        ExceptionCode errorType = ExceptionCode.INTERNAL_SERVER_ERROR;

        BaseErrorModel baseBody = new BaseErrorModel();
        baseBody.setCode(errorType.code());
        baseBody.setDesc(errorType.message());
        
        e.printStackTrace();

        return ResponseEntity.status(errorType.status()).body(baseBody);
    }

    

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseErrorModel> handleConstraintViolationException(ConstraintViolationException exception){

        List<ConstraintViolation> array = new ArrayList<>(exception.getConstraintViolations());

        StringBuilder desc = new StringBuilder();

        for(int i = 0; i < array.size(); i++){
            String parameterNm = array.get(i).getPropertyPath().toString();

            desc.append("[");
            desc.append(parameterNm.substring(parameterNm.lastIndexOf(".")).replace(".", ""));
            desc.append("]");

            desc.append(" ");
            desc.append(array.get(i).getMessageTemplate());
            desc.append(".");

            if((i+1) == array.size()) continue;

            desc.append(" / ");
        }
        
        BaseErrorModel baseBody = new BaseErrorModel();
        baseBody.setCode(ExceptionCode.NON_VALID_PARAMETER.code());
        baseBody.setMessage(ExceptionCode.NON_VALID_PARAMETER.message());
        baseBody.setDesc(desc.toString());

        return ResponseEntity.status(400).body(baseBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){

        BindingResult bindingResult = exception.getBindingResult();
        
        List<ObjectError> objectErrors = bindingResult.getAllErrors();
        List<FieldError> fields = bindingResult.getFieldErrors();

        StringBuilder desc = new StringBuilder();
        
        for(int i = 0; i < fields.size(); i++){
            desc.append("[");
            desc.append(fields.get(i).getField());
            desc.append("]");

            desc.append(" ");
            desc.append(objectErrors.get(i).getDefaultMessage());
            desc.append(".");

            if((i+1) == fields.size()) continue;

            desc.append(" / ");
        }
        
        BaseErrorModel baseBody = new BaseErrorModel();
        baseBody.setCode(ExceptionCode.NON_VALID_PARAMETER.code());
        baseBody.setMessage(ExceptionCode.NON_VALID_PARAMETER.message());
        baseBody.setDesc(desc.toString());

        return ResponseEntity.status(400).body(baseBody);
    }

}   

