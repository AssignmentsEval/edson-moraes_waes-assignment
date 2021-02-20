package com.waes.diffservice.advice;


import brave.Tracer;
import com.waes.diffservice.dto.ErrorDTO;
import com.waes.diffservice.exception.GenericException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    protected ResponseEntity<ErrorDTO> handleConflict(GenericException ex, WebRequest request) {
        String traceIdString = tracer.currentSpan().context().traceIdString();

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

}
