package com.waes.diffservice.advice;


import brave.Tracer;
import com.waes.diffservice.ApplicationErrorEnum;
import com.waes.diffservice.exception.dto.ErrorDTO;
import com.waes.diffservice.exception.DiffServiceException;
import com.waes.diffservice.exception.GenericException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class RestResponseEntityExceptionHandler {

    private final Tracer tracer;

    /**
     * Handles any {@link GenericException}
     * @param ex The exception that ocurred
     * @return The response entity with the values defined by one of the entries of {@link ApplicationErrorEnum}
     */
    @ExceptionHandler(GenericException.class)
    protected ResponseEntity<ErrorDTO> handleGenericException(GenericException ex) {
        String traceIdString = getCurrentTraceId();

        ex.setInstance(traceIdString);
        log.error(ex.getDetail(), ex);

        return responseForGenericException(ex, traceIdString);
    }

    /**
     * Handles any other exception by wrapping it in a {@link DiffServiceException} with the type
     * {@link ApplicationErrorEnum#UNEXPECTED_ERROR}
     * @param ex The exception that ocurred
     * @return The response entity with the values defined by one of the entries of {@link ApplicationErrorEnum#UNEXPECTED_ERROR}
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorDTO> handleException(Exception ex) {
        String traceIdString = getCurrentTraceId();

        DiffServiceException diffServiceException = new DiffServiceException(ex, ApplicationErrorEnum.UNEXPECTED_ERROR, ex.getMessage());
        diffServiceException.setInstance(traceIdString);
        log.error(diffServiceException.getDetail(), ex);

        return responseForGenericException(diffServiceException, traceIdString);
    }

    private ResponseEntity<ErrorDTO> responseForGenericException(GenericException e, String traceId) {
        return ResponseEntity
                .status(e.getStatus())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ErrorDTO.builder()
                    .type(e.getType())
                    .title(e.getTitle())
                    .status(e.getStatus())
                    .detail(e.getDetail())
                    .instance(traceId)
                    .build());
    }

    private String getCurrentTraceId() {
        return tracer.currentSpan().context().traceIdString();
    }

}
