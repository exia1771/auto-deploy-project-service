package github.exia1771.deploy.common.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class CommonResponse extends ResponseEntity<ResponseBody> {

    private CommonResponse(HttpStatus status) {
        super(status);
    }

    public static <T> ResponseEntity<ResponseBody> success(Object t) {
        ResponseBody responseBody = new ResponseBody(t);
        responseBody.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(responseBody);
    }

    public static <T> ResponseEntity<ResponseBody> error(Object t) {
        ResponseBody responseBody = new ResponseBody(t);
        responseBody.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

    public static <T> ResponseEntity<ResponseBody> of(Object t, HttpStatus httpStatus, String message) {
        ResponseBody responseBody = new ResponseBody(t, message);
        responseBody.setStatus(httpStatus.value());
        return ResponseEntity.status(httpStatus).body(responseBody);
    }
}

