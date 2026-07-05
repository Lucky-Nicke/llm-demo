package club.lanxige.llmbackend.vo;

public class Result<T> {

    private Boolean success;
    private Integer code;
    private String message;
    private T data;

    private Result(Boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> ok() {
        return new Result<>(true, 200, "success", null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(true, 200, "success", data);
    }

    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(true, 200, message, data);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(false, code, message, null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(false, 500, message, null);
    }

    public Boolean getSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
