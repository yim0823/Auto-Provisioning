package com.bespinglobal.dcos.ap.api.advice;

import com.bespinglobal.dcos.ap.api.exception.ApplicationException;
import com.bespinglobal.dcos.ap.api.response.ApiResponseDto;
import com.bespinglobal.dcos.ap.utils.RtCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * Project : Auto-Provisioning
 * Class : ApiCommonAdvice
 * Version : 2019.07.16 v0.1
 * Created by taehyoung.yim on 2019-07-16.
 * *** 저작권 주의 ***
 */
@Order(value = 1)
@RestControllerAdvice // REST 요청에 대한 에러를 핸들링
public class ApiCommonAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ApiCommonAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ApiResponseDto<String> handleBaseException(Exception e) {
        return ApiResponseDto.createException(RtCode.RT_INTERNAL_ERROR, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingPathVariableException.class)
    public ApiResponseDto<String> handleBaseException(MissingPathVariableException e) {
        ApiResponseDto<String> exception = ApiResponseDto.createException(RtCode.RT_MISSING_PATH_VARIABLE, e.getMessage());
        logger.error("[{}] {}", RtCode.RT_MISSING_PATH_VARIABLE.getRtCode(), exception);

        return exception;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ ConstraintViolationException.class })
    public ApiResponseDto<String> handleBaseException(ConstraintViolationException e) {
        ApiResponseDto<String> exception = ApiResponseDto.createException(RtCode.RT_WRONG_PARAMETER, "Wrong parameters.");
        logger.error("[{}] {}", RtCode.RT_WRONG_PARAMETER.getRtCode(), exception);

        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ApplicationException.class })
    public ApiResponseDto<String> handleValidException(ApplicationException e) {
        ApiResponseDto<String> exception = ApiResponseDto.createException(RtCode.RT_NOT_SUPPORT, e.getMessage());
        logger.error("[{}] {}", RtCode.RT_NOT_SUPPORT.getRtCode(), exception);

        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ConversionFailedException.class })
    public ApiResponseDto<String> handleValidException(ConversionFailedException e) {
        String errorMessage = String.format(
                " %s Conversion failed for Object of a request.",
                e.getTargetType().getType().getSimpleName());

        ApiResponseDto<String> exception = ApiResponseDto.createException(RtCode.RT_WRONG_PARAMETER, errorMessage);
        logger.error("[{}] {}", RtCode.RT_WRONG_PARAMETER.getRtCode(), exception);

        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ApiResponseDto<String> handleValidException(MethodArgumentNotValidException e) {

        BindingResult result = e.getBindingResult();
        String errorMessage = String.format(
                " %s Validation failed for Object of a request.",
                Objects.requireNonNull(result.getFieldError()).getField());

        ApiResponseDto<String> exception = ApiResponseDto.createException(RtCode.RT_VALIDATION_FAILURE, errorMessage);
        logger.error("[{}] {}", RtCode.RT_VALIDATION_FAILURE.getRtCode(), exception);

        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ BindException.class })
    public ApiResponseDto<ErrorMessageCollection> handleValidException(BindException e) {

        ApiResponseDto<ErrorMessageCollection> exception = ApiResponseDto.createException(
                RtCode.RT_WRONG_PARAMETER,
                new ErrorMessageCollection(
                        e.getBindingResult().getFieldErrors(),
                        e.getBindingResult().getGlobalErrors()));
        logger.error("[{}] {}", RtCode.RT_BINDING_FAILURE.getRtCode(), exception);

        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ NumberFormatException.class })
    public ApiResponseDto<String> handleValidException(NumberFormatException e) {
        ApiResponseDto<String> exception = ApiResponseDto.createException(RtCode.RT_WRONG_PARAMETER, "Wrong parameters.");
        logger.error("[{}] {}", RtCode.RT_WRONG_PARAMETER.getRtCode(), exception);

        return exception;
    }

}
