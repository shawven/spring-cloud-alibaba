package com.sca.common;

/**
 * 自定义响应消息体
 *
 * @author FS
 * @since  2018-11-09
 */
public class Response<T>{

    /**
     * 成功标志
     */
    private Boolean success = true;

    /**
     * 响应业务状态
     */
    private Integer status = 200;

    /**
     * 响应消息
     */
    private String message = "ok";

    /**
     * 时间撮
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 响应中的数据
     */
    private T data = null;

    public Response() {
    }

    public Response(T data) {
        this.data = data;
    }

    public Response(String msg, T data) {
        this(data);
        this.message = msg;
    }

    public Response(Integer status, String msg, T data) {
        this(msg, data);
        this.status = status;
    }

    public Response(Boolean success, Integer status, String msg, T data) {
        this(status, msg, data);
        this.success = success;
    }


    public Boolean getSuccess() {
        return success;
    }

    public Response setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public Response setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Response setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getData() {
        return data;
    }

    public Response setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("com.sca.common.Response{");
        sb.append("success=").append(success);
        sb.append(", status=").append(status);
        sb.append(", message='").append(message).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
