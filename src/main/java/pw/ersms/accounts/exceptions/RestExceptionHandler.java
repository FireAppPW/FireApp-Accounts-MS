package pw.ersms.accounts.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Map<String, Object> handleNoSuchElementException(NoSuchElementException exception) {
        return responseException(exception);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Map<String, Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        return responseException(exception);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalStateException.class)
    public Map<String, Object> doesBotExistException(IllegalStateException exception) {
        return responseException(exception);
    }

    public Map<String, Object> responseException(Exception exception) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("success", false);
        errorMap.put("message", exception.getMessage());
        return errorMap;
    }
}
