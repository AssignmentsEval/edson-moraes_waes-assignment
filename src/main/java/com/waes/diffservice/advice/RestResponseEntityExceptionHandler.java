package com.waes.diffservice.advice;


import brave.Tracer;
import com.waes.diffservice.ApplicationErrorEnum;
import com.waes.diffservice.dto.ErrorDTO;
import com.waes.diffservice.exception.DiffServiceException;
import com.waes.diffservice.exception.GenericException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class RestResponseEntityExceptionHandler {

    private final Tracer tracer;

    @ExceptionHandler(GenericException.class)
    protected ResponseEntity<ErrorDTO> handleGenericException(GenericException ex, WebRequest request) {
        String traceIdString = getCurrentTraceId();

        ex.setInstance(traceIdString);
        log.error(ex.getDetail(), ex);

        return ResponseEntity.status(ex.getStatus()).body(ErrorDTO.builder()
                .type(ex.getType())
                .title(ex.getTitle())
                .status(ex.getStatus())
                .detail(ex.getDetail())
                .instance(traceIdString)
                .build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorDTO> handleException(Exception ex, WebRequest request) {
        String traceIdString = getCurrentTraceId();

        DiffServiceException diffServiceException = new DiffServiceException(ex, ApplicationErrorEnum.UNEXPECTED_ERROR, ex.getMessage());
        diffServiceException.setInstance(traceIdString);
        log.error(diffServiceException.getDetail(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(ErrorDTO.builder()
                .type(diffServiceException.getType())
                .title(diffServiceException.getTitle())
                .status(diffServiceException.getStatus())
                .detail(diffServiceException.getDetail())
                .instance(traceIdString)
                .build());
    }

    private String getCurrentTraceId() {
        return tracer.currentSpan().context().traceIdString();
    }

}
