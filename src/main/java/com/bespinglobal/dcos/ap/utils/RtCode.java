package com.bespinglobal.dcos.ap.utils;

import org.springframework.http.HttpStatus;

/**
 * Project : Information-Collector
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
    RT_AUTHENTICATION_FAILURE(-8, "Authentication failure", HttpStatus.FORBIDDEN),
    RT_NOT_EXIST(-9, "Not exist", HttpStatus.GONE),
    RT_DUPLICATED(-10, "Duplicated", HttpStatus.BAD_REQUEST),
    RT_NOT_SUPPORT(-11, "Not support", HttpStatus.NOT_FOUND),
    RT_FAILURE(-99, "Failure", HttpStatus.INTERNAL_SERVER_ERROR),

    //  Return Code [-8001XX]
    RT_PORTAL_INTERNAL_ERROR(-800100, "Portal System Error", HttpStatus.BAD_REQUEST),
    RT_PORTAL_CLIENT_ERROR(-800101, "Portal System Client Error", HttpStatus.BAD_REQUEST),
    RT_PORTAL_AUTH_FAILURE(-800102, "Portal Authentication Failure", HttpStatus.BAD_REQUEST),
    RT_PORTAL_URISYNTAX_ERROR(-800103, "Portal URI Syntax Error", HttpStatus.BAD_REQUEST),
    RT_PORTAL_CONNECTION_ERROR(-800110, "Portal System Connect Error", HttpStatus.BAD_REQUEST),
    RT_PORTAL_SOCKET_ERROR(-800111, "Portal System Socket Error", HttpStatus.BAD_REQUEST),
    RT_PORTAL_IO_ERROR(-800112, "Portal System IO Error", HttpStatus.BAD_REQUEST),
    RT_PORTAL_PARSING_ERROR(-800113, "Portal System Parsing Error", HttpStatus.BAD_REQUEST),
    RT_PORTAL_ENCODING_ERROR(-800114, "Portal System Encoding Error", HttpStatus.BAD_REQUEST),

    //  Return Code [-9001XX]
    RT_ASSET_INTERNAL_ERROR(-900100, "Asset System Error", HttpStatus.BAD_REQUEST),
    RT_ASSET_CLIENT_ERROR(-900101, "Asset System Client Error", HttpStatus.BAD_REQUEST),
    RT_ASSET_AUTH_FAILURE(-900102, "Asset Authentication Failure", HttpStatus.BAD_REQUEST),
    RT_ASSET_URISYNTAX_ERROR(-900103, "Asset URI Syntax Error", HttpStatus.BAD_REQUEST),
    RT_ASSET_CONNECTION_ERROR(-900110, "Asset System Connect Error", HttpStatus.BAD_REQUEST),
    RT_ASSET_SOCKET_ERROR(-900111, "Asset System Socket Error", HttpStatus.BAD_REQUEST),
    RT_ASSET_IO_ERROR(-900112, "Asset System IO Error", HttpStatus.BAD_REQUEST),
    RT_ASSET_PARSING_ERROR(-900113, "Asset System Parsing Error", HttpStatus.BAD_REQUEST),
    RT_ASSET_ENCODING_ERROR(-900114, "Asset System Encoding Error", HttpStatus.BAD_REQUEST),

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
