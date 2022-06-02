package woowacourse.shoppingcart.ui;

import java.util.List;
import javax.validation.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import woowacourse.auth.exception.InvalidTokenException;
import woowacourse.auth.exception.LoginFailException;
import woowacourse.shoppingcart.dto.ErrorResponse;
import woowacourse.shoppingcart.dto.FieldErrorResponse;
import woowacourse.shoppingcart.exception.DuplicateDomainException;
import woowacourse.shoppingcart.exception.DuplicateEmailException;
import woowacourse.shoppingcart.exception.DuplicateUsernameException;
import woowacourse.shoppingcart.exception.ForbiddenAccessException;
import woowacourse.shoppingcart.exception.InvalidCartItemException;
import woowacourse.shoppingcart.exception.InvalidCustomerException;
import woowacourse.shoppingcart.exception.InvalidOrderException;
import woowacourse.shoppingcart.exception.InvalidProductException;
import woowacourse.shoppingcart.exception.NotInCustomerCartItemException;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleUnhandledException() {
        return new ErrorResponse("Unhandled Exception");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ErrorResponse handle() {
        return new ErrorResponse("존재하지 않는 데이터 요청입니다.");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        final FieldError mainError = fieldErrors.get(0);
        FieldErrorResponse response = new FieldErrorResponse(mainError.getField(), mainError.getDefaultMessage());

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DuplicateEmailException.class, DuplicateUsernameException.class})
    public FieldErrorResponse handleDuplicatedRequest(DuplicateDomainException exception) {
        return new FieldErrorResponse(exception.getField(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleInvalidRequest(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            InvalidCustomerException.class,
            InvalidCartItemException.class,
            InvalidProductException.class,
            InvalidOrderException.class,
            NotInCustomerCartItemException.class,
    })
    public ErrorResponse handleInvalidAccess(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({LoginFailException.class, InvalidTokenException.class})
    public ErrorResponse handleLoginFailException(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenAccessException.class)
    public ErrorResponse handleForbiddenAccessException(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllOtherErrors(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return ResponseEntity
                .status(status)
                .body(response);
    }
}