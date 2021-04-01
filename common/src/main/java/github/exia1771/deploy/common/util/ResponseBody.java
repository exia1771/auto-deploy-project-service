package github.exia1771.deploy.common.util;

import lombok.Data;

@Data
public class ResponseBody {
	private final long timestamp = System.currentTimeMillis();
	private Object data;
	private String message;
	private int status;

	public ResponseBody() {
	}

	public ResponseBody(Object data) {
		this.data = data;
	}

	public ResponseBody(Object data, String message) {
		this.data = data;
		this.message = message;
	}

}
