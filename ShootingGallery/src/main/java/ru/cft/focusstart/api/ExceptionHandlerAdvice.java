package ru.cft.focusstart.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.cft.focusstart.api.dto.ErrorCode;
import ru.cft.focusstart.api.dto.ErrorDto;
import ru.cft.focusstart.exception.InternalErrorException;
import ru.cft.focusstart.exception.InvalidParametersException;
import ru.cft.focusstart.exception.ObjectNotFoundException;
import ru.cft.focusstart.exception.ServiceUnavailableException;

import javax.servlet.ServletException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(InternalErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleInternalErrorException(InternalErrorException e) {
        logException(e);
        return new ErrorDto(ErrorCode.INTERNAL_ERROR, e.getMessage());
    }

    @ExceptionHandler(InvalidParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleInvalidParametersException(InvalidParametersException e) {
        logException(e);
        return new ErrorDto(ErrorCode.INVALID_PARAMETERS, e.getMessage());
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDto handleServiceUnavailableException(ServiceUnavailableException e) {
        logException(e);
        return new ErrorDto(ErrorCode.SERVICE_UNAVAILABLE, e.getMessage());
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleObjectNotFoundException(ObjectNotFoundException e) {
        logException(e);
        return new ErrorDto(ErrorCode.OBJECT_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorDto handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logException(e);
        return new ErrorDto(ErrorCode.INVALID_PARAMETERS, e.getMessage());
    }

    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleServletException(ServletException e) {
        logException(e);
        return new ErrorDto(ErrorCode.INVALID_PARAMETERS, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleException(Exception e) {
        logException(e);
        return new ErrorDto(ErrorCode.INTERNAL_ERROR, e.getMessage());
    }

    private void logException(Exception e) {
        log.error("Exception occurred: ", e);
    }
}
