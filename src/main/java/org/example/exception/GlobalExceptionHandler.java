package org.example.exception;
import org.example.admin.exception.AdminNotFoundException;
import org.example.client.exception.ClientNotFoundException;
import org.example.common.exception.EmailRequiredException;
import org.example.common.exception.NameRequiredException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            EmailRequiredException.class,
            NameRequiredException.class,
            ClientNotFoundException.class,
    })
    public ResponseEntity<ErrorMessage> handleClientExceptions(RuntimeException ex, WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=","")
        );

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            AdminNotFoundException.class
    })
    public ResponseEntity<ErrorMessage> handleAdminExceptions(RuntimeException ex, WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=","")
        );

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGeneric(Exception ex, WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleAtleastOneExceptions(Exception ex, WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                "BAD REQUEST ERROR: " + "At least one field necessary to continue",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
