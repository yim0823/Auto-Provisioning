package com.bespinglobal.dcos.ap.api.response;

import com.bespinglobal.dcos.ap.api.exception.ApplicationException;
import com.bespinglobal.dcos.ap.utils.RtCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Project : Information-Collector
 * Class : ApiResponseDto
 * Version : 2019.07.16 v0.1
 * Created by taehyoung.yim on 2019-07-16.
 * *** 저작권 주의 ***
 */
@ToString
@NoArgsConstructor
@Getter
public class ApiResponseDto<T> {

    public static final ApiResponseDto<String> DEFAULT_UNAUTHORIZED = new ApiResponseDto<>(RtCode.RT_SUCCESS);
    public static final ApiResponseDto<String> DEFAULT_OK = new ApiResponseDto<>(RtCode.RT_SUCCESS);

    private RtCode code;
    private String message;
    private T data;

    private ApiResponseDto(RtCode status) {
        this.bindStatus(status);
    }

    private ApiResponseDto(RtCode status, T data) {
        this.bindStatus(status);
        this.data = data;
    }

    private ApiResponseDto(RtCode code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private ApiResponseDto(RtCode code, ApplicationException e) {
        this.code = code;
        this.message = e.getMessage();
    }

    private void bindStatus(RtCode status) {
        this.code = status;
        this.message = status.getRtMessage();
    }

    public static <T> ApiResponseDto<T> createOK(T data) {
        return new ApiResponseDto<>(RtCode.RT_SUCCESS, data);
    }

    public static ApiResponseDto<String> createException(RtCode code, String message) {
        return new ApiResponseDto<>(code, message, "");
    }

    public static <T> ApiResponseDto<T> createException(RtCode code, T data) {
        return new ApiResponseDto<>(code, data);
    }
}
