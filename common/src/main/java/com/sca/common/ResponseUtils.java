package com.sca.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.*;

/**
 * 自定义响应结构工具类, 适应 RESTFull 风格，改变 http status
 */
public class ResponseUtils {

    /**
     * 200 OK - [GET]：服务器成功返回用户请求的数据，该操作是幂等的（Idempotent）
     *
     * @return 响应体
     */
    public static ResponseEntity<Response> ok() {
        return ok(OK.getReasonPhrase());
    }

    public static ResponseEntity<Response> ok(String msg) {
        return build(true, OK, msg);
    }

    public static <T> ResponseEntity<Response<T>> ok(T data) {
        return ok(OK.getReasonPhrase(), data);
    }

    public static <T> ResponseEntity<Response<T>> ok(String msg, T data) {
        return build(true, OK, msg, data);
    }


    /**
     * 201 Created - [POST/PUT/PATCH]：用户新建或修改数据成功
     *
     * @return 响应体
     */
    public static ResponseEntity created() {
        return created(null);
    }

    public static <T> ResponseEntity<Response<T>> created(T data) {
        return created(CREATED.getReasonPhrase(), data);
    }

    public static <T> ResponseEntity<Response<T>> created(String msg, T data) {
        return build(true, CREATED, msg, data);
    }

    /**
     * 202 Accepted - [*]：表示一个请求已经进入后台排队（异步任务）
     *
     * @return 响应体
     */
    public static ResponseEntity accepted() {
        return build(true, ACCEPTED, ACCEPTED.getReasonPhrase());
    }

    public static <T> ResponseEntity accepted(T data) {
        return build(true, ACCEPTED, ACCEPTED.getReasonPhrase(), data);
    }

    /**
     * 204 No Content - [DELETE]：用户删除数据成功，无数据返回
     *
     * @return 响应体
     */
    public static ResponseEntity noContent() {
        return build(true, NO_CONTENT, NO_CONTENT.getReasonPhrase());
    }

    /**
     * 400 Bad Request - [*]：请求参数有误
     *
     * @return 响应体
     */
    public static ResponseEntity badRequest() {
        return build(false, BAD_REQUEST, BAD_REQUEST.getReasonPhrase());
    }

    public static ResponseEntity badRequest(String msg) {
        return build(false, BAD_REQUEST, msg);
    }

    public static <T> ResponseEntity badRequest(T data) {
        return build(false, BAD_REQUEST, BAD_REQUEST.getReasonPhrase(), data);
    }

    public static <T> ResponseEntity badRequest(String msg, T data) {
        return build(false, BAD_REQUEST, msg, data);
    }

    /**
     * 401 Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）
     *
     * @return 响应体
     */
    public static ResponseEntity unauthorized () {
        return build(false, UNAUTHORIZED, UNAUTHORIZED.getReasonPhrase());
    }

    public static ResponseEntity unauthorized (String msg) {
        return build(false, UNAUTHORIZED, msg);
    }

    /**
     * 403 Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的
     *
     * @return 响应体
     */
    public static ResponseEntity forbidden () {
        return build(false, FORBIDDEN, FORBIDDEN.getReasonPhrase());
    }

    public static ResponseEntity forbidden (String msg) {
        return build(false, FORBIDDEN, msg);
    }

    /**
     * 404 Not Found - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
     *
     * @return 响应体
     */
    public static ResponseEntity notFound() {
        return build(false, NOT_FOUND, NOT_FOUND.getReasonPhrase());
    }

    public static ResponseEntity notFound(String msg) {
        return build(false, NOT_FOUND, msg);
    }

    /**
     * 406 Not Acceptable - [GET]：用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
     *
     * @return 响应体
     */
    public static ResponseEntity notAcceptable() {
        return build(false, NOT_ACCEPTABLE, NOT_ACCEPTABLE.getReasonPhrase());
    }

    public static ResponseEntity notAcceptable(String msg) {
        return build(false, NOT_ACCEPTABLE, msg);
    }

    /**
     * 422  Unprocesable entity - [POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误，语义错误，无法响应。
     *
     * @return 响应体
     */
    public static ResponseEntity unprocesable() {
        return build(false, UNPROCESSABLE_ENTITY, UNPROCESSABLE_ENTITY.getReasonPhrase());
    }

    public static ResponseEntity unprocesable(String msg) {
        return build(false, UNPROCESSABLE_ENTITY, msg);
    }

    public static <T> ResponseEntity unprocesable(String msg, T data) {
        return build(false, UNPROCESSABLE_ENTITY, msg, data);
    }

    /**
     * 500 Internal Server Error - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。
     *
     * @return 响应体
     */
    public static ResponseEntity<Response> error() {
        return error(INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    public static ResponseEntity<Response> error(String msg) {
        return build(false, INTERNAL_SERVER_ERROR, msg);
    }

    public static <T> ResponseEntity<Response<T>> error(T data) {
        return error(INTERNAL_SERVER_ERROR.getReasonPhrase(), data);
    }

    public static <T> ResponseEntity<Response<T>> error(String msg, T data) {
        return build(false, INTERNAL_SERVER_ERROR, msg, data);
    }

    /**
     * 构建响应体
     *
     * @param bool   是否成功
     * @param status 状态
     * @param msg    消息
     * @return       响应体
     */
    public static ResponseEntity<Response> build(boolean bool, HttpStatus status, String msg) {
        return new ResponseEntity<>(new Response<>(bool, status.value(), msg, null), status);
    }

    /**
     * 构建响应体
     *
     * @param bool    是否成功
     * @param status  状态
     * @param msg     消息
     * @param <T>     被包装对象
     * @return        响应体
     */
    public static <T> ResponseEntity<Response<T>> build(boolean bool, HttpStatus status, String msg, T data) {
        return new ResponseEntity<>(new Response<>(bool, status.value(), msg, data), status);
    }


    /**
     * 构建响应体
     *
     * @param response 自定义response对象
     * @param <T>      被包装对象
     * @return         响应体
     */
    public static <T> ResponseEntity<Response<T>> build(Response<T> response) {
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
