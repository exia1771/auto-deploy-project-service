package github.exia1771.deploy.common.util;

import com.alibaba.fastjson.JSONObject;
import github.exia1771.deploy.common.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


public abstract class CommonResponse extends ResponseEntity<ResponseBody> {

    private CommonResponse(HttpStatus status) {
        super(status);
    }

    public static ResponseEntity<ResponseBody> success(Object t) {
        ResponseBody responseBody = new ResponseBody(t);
        responseBody.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(responseBody);
    }

    public static ResponseEntity<ResponseBody> success() {
        return success(null);
    }

    public static ResponseEntity<ResponseBody> error(Object t) {
        ResponseBody responseBody = new ResponseBody(t);
        responseBody.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

    public static ResponseEntity<ResponseBody> of(Object t, HttpStatus httpStatus, String message) {
        ResponseBody responseBody = new ResponseBody(t, message);
        responseBody.setStatus(httpStatus.value());
        return ResponseEntity.status(httpStatus).body(responseBody);
    }

    public static <T> ResponseEntity<ResponseBody> page(List<T> content, Integer from, Integer size) {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus(HttpStatus.OK.value());

        if (from == null || from <= 0) {
            throw new ServiceException("起始页码不能为空或者小于0!");
        }

        if (size == null) {
            size = 20;
        }

        JSONObject jsonObject = new JSONObject();

        int start = (from - 1) * size;
        if (start > content.size()) {
            throw new ServiceException("起始页码数不能大于列表总数!");
        }
        int end = start + size;
        if (end >= content.size()) {
            end = content.size();
        }

        jsonObject.put("total", content.size());
        jsonObject.put("from", from);
        jsonObject.put("size", size);
        jsonObject.put("content", content.subList(start, end));

        responseBody.setData(jsonObject);
        return ResponseEntity.ok(responseBody);
    }


}

