package  com.works.bootworks.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author T016071
 * @date 2023/08/02
 * 响应结果 统一返回类
 */
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RES<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;

	private String msg;

	private T data;


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static <T> RES<T> no(int code, String msg) {
		return restResult(code, msg, null);
	}

	public static <T> RES<T> ok(int code, String msg,T data) {
		return restResult(code, msg,data);
	}

	private static <T> RES<T> restResult(int code, String msg,T data) {
		RES<T> apiResult = new RES<>();
		apiResult.setCode(code);
		apiResult.setMsg(msg);
		apiResult.setData(data);
		return apiResult;
	}

}
