package com.bespinglobal.dcos.ap.utils;

import org.springframework.http.HttpStatus;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ic.utils.RtCode
 * Version : 2019.07.24 v0.1
 * Created by taehyoung.yim on 2019-07-24.
 * *** 저작권 주의 ***
 */
public enum RtCode {

    // Common Return Code;
    RT_EXECUTED(1, "Executed", HttpStatus.ACCEPTED),
    RT_SUCCESS(0, "Success", HttpStatus.OK),
    RT_INTERNAL_ERROR(-1, "Internal error", HttpStatus.INTERNAL_SERVER_ERROR),
    RT_WRONG_PARAMETER(-2, "Wrong parameter", HttpStatus.BAD_REQUEST),
    RT_WRONG_STATUS(-3, "Wrong status", HttpStatus.BAD_REQUEST),
    RT_MISSING_PATH_VARIABLE(-4, "Missing path variable", HttpStatus.BAD_REQUEST),
    RT_PROPAGATION_ERROR(-5, "Propagation error", HttpStatus.BAD_REQUEST),
    RT_VALIDATION_FAILURE(-6, "Validation error", HttpStatus.BAD_REQUEST),
    RT_BINDING_FAILURE(-7, "Binding error", HttpStatus.BAD_REQUEST),
    RT_PARSING_ERROR(-8, "Parsing Error", HttpStatus.BAD_REQUEST),
    RT_AUTHENTICATION_FAILURE(-9, "Authentication failure", HttpStatus.FORBIDDEN),
    RT_NOT_EXIST(-10, "Not exist", HttpStatus.GONE),
    RT_DUPLICATED(-11, "Duplicated", HttpStatus.BAD_REQUEST),
    RT_NOT_SUPPORT(-12, "Not support", HttpStatus.NOT_FOUND),
    RT_FAILURE(-99, "Failure", HttpStatus.INTERNAL_SERVER_ERROR),

    //  Return Code [-10001XX]
    RT_AWS_INTERNAL_ERROR(-1000100, "AWS System Error", HttpStatus.BAD_REQUEST),
    RT_AWS_CLIENT_ERROR(-1000101, "AWS System Client Error", HttpStatus.BAD_REQUEST),
    RT_AWS_AUTH_FAILURE(-1000102, "AWS Authentication Failure", HttpStatus.BAD_REQUEST),
    RT_AWS_URISYNTAX_ERROR(-1000103, "AWS URI Syntax Error", HttpStatus.BAD_REQUEST),
    RT_AWS_CONNECTION_ERROR(-1000110, "AWS System Connect Error", HttpStatus.BAD_REQUEST),
    RT_AWS_SOCKET_ERROR(-1000111, "AWS System Socket Error", HttpStatus.BAD_REQUEST),
    RT_AWS_IO_ERROR(-1000112, "AWS System IO Error", HttpStatus.BAD_REQUEST),
    RT_AWS_PARSING_ERROR(-1000113, "AWS System Parsing Error", HttpStatus.BAD_REQUEST),
    RT_AWS_ENCODING_ERROR(-1000114, "AWS System Encoding Error", HttpStatus.BAD_REQUEST),

    //  Return Code [-11001XX]
    RT_GCP_INTERNAL_ERROR(-1100100, "GCP System Error", HttpStatus.BAD_REQUEST),
    RT_GCP_CLIENT_ERROR(-1100101, "GCP System Client Error", HttpStatus.BAD_REQUEST),
    RT_GCP_AUTH_FAILURE(-1100102, "GCP Authentication Failure", HttpStatus.BAD_REQUEST),
    RT_GCP_URISYNTAX_ERROR(-1100103, "GCP URI Syntax Error", HttpStatus.BAD_REQUEST),
    RT_GCP_CONNECTION_ERROR(-1100110, "GCP System Connect Error", HttpStatus.BAD_REQUEST),
    RT_GCP_SOCKET_ERROR(-1100111, "GCP System Socket Error", HttpStatus.BAD_REQUEST),
    RT_GCP_IO_ERROR(-1100112, "GCP System IO Error", HttpStatus.BAD_REQUEST),
    RT_GCP_PARSING_ERROR(-1100113, "GCP System Parsing Error", HttpStatus.BAD_REQUEST),
    RT_GCP_ENCODING_ERROR(-1100114, "GCP System Encoding Error", HttpStatus.BAD_REQUEST),


    // UNKNOWN Error Return Code
    RT_UNKNOWN(-999999, "Unknown error. contact to admin", HttpStatus.INTERNAL_SERVER_ERROR);

    private final long rtCode;
    private final String rtMessage;
    private final HttpStatus httpStatus;

    private RtCode(long rtCode, String rtMessage, HttpStatus httpStatus) {
        this.rtCode = rtCode;
        this.rtMessage = rtMessage;
        this.httpStatus = httpStatus;
    }

    public long getRtCode() {
        return this.rtCode;
    }

    public String getRtMessage() {
        return this.rtMessage;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //sb.append(this.rtCode).append(ApplicationConstants.DELIMITER_COLON);
        sb.append("[").append(this.rtMessage).append("] ");
        //sb.append(this.httpStatus);

        return sb.toString();
    }
}
