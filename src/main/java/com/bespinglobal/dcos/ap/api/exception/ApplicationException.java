package com.bespinglobal.dcos.ap.api.exception;

import com.bespinglobal.dcos.ap.utils.RtCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Project : Information-Collector
 * Class : com.bespinglobal.dcos.ic.api.exception.ApplicationException
 * Version : 2019.07.24 v0.1
 * Created by taehyoung.yim on 2019-07-24.
 * *** 저작권 주의 ***
 */
@Getter
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private long errorCode;
    private String errorMessage;
    private HttpStatus httpStatus;

    public ApplicationException(RtCode rtCode) {
        super(rtCode.toString());
        this.errorCode = rtCode.getRtCode();
        this.errorMessage = rtCode.getRtMessage();
        this.httpStatus = rtCode.getHttpStatus();
    }

    public ApplicationException(RtCode rtCode, String additionalMessage) {
        super(rtCode.toString() + additionalMessage);
        this.errorCode = rtCode.getRtCode();
        this.errorMessage = rtCode.getRtMessage() + additionalMessage;
        this.httpStatus = rtCode.getHttpStatus();
    }

    public ApplicationException(RtCode rtCode, Throwable th) {
        super(rtCode.toString(), th);
        this.errorCode = rtCode.getRtCode();
        this.errorMessage = rtCode.getRtMessage();
        this.httpStatus = rtCode.getHttpStatus();
    }

    public ApplicationException(RtCode rtCode, String additionalMessage, Throwable th) {
        super(rtCode.toString() + additionalMessage, th);
        this.errorCode = rtCode.getRtCode();
        this.errorMessage = rtCode.getRtMessage();
        this.httpStatus = rtCode.getHttpStatus();
    }

}
