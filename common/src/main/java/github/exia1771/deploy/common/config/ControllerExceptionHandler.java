package github.exia1771.deploy.common.config;

import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ResponseBody> serviceExceptionHandler(Exception e,
                                                                HttpServletRequest request) throws Exception {
        return CommonResponse.of(null, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBody> exceptionHandler(Exception e,
                                                                HttpServletRequest request) throws Exception {
        return CommonResponse.of(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
