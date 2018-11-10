/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ult.nodo.ciencia.control;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

public class AppResponse<T> {

    private boolean success;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T elemento;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<T> elementos;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public AppResponse(boolean success, String msg, T elemento, Collection<T> elementos, long total) {
        this.success = success;
        this.msg = msg;
        this.elemento = elemento;
        this.elementos = elementos;
        this.total = total == -1 ? null : total;
    }

    @JsonIgnoreType
    public static class ResponseBuilder<T> {

        private boolean success = false;

        private String msg;

        private Collection<T> elementos;

        private T elemento;

        private long total = -1;

        public <T> AppResponse build() {
            return new AppResponse(success, msg, elemento, elementos, total);
        }

        public ResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public ResponseBuilder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public ResponseBuilder total(long total) {
            this.total = total;
            return this;
        }

        public ResponseBuilder elementos(Collection<T> elementos) {
            this.elementos = elementos;
            return this;
        }

        public ResponseBuilder elemento(T elemento) {
            this.elemento = elemento;
            return this;
        }
    }

    public static ResponseBuilder success() {
        ResponseBuilder builder = new ResponseBuilder();
        builder.success(true);
        return builder;
    }

    public static ResponseBuilder success(String msg) {
        ResponseBuilder builder = new ResponseBuilder();
        builder.success(true);
        builder.msg(msg);
        return builder;
    }

    public static <T> ResponseBuilder success(Collection<T> elementos) {
        ResponseBuilder builder = new ResponseBuilder();
        builder.success(true);
        builder.elementos(elementos);
        return builder;
    }

    public static <T> ResponseBuilder success(T elemento) {
        ResponseBuilder builder = new ResponseBuilder();
        builder.success(true);
        builder.elemento(elemento);
        return builder;
    }

    public static <T> ResponseBuilder failure(T elemento) {
        ResponseBuilder builder = new ResponseBuilder();
        builder.success(false);
        builder.elemento(elemento);
        return builder;
    }

    public static ResponseBuilder failure(String msg) {
        ResponseBuilder builder = new ResponseBuilder();
        builder.msg(msg);
        builder.success(false);
        return builder;
    }

    public static ResponseBuilder failure() {
        ResponseBuilder builder = new ResponseBuilder();
        builder.success(false);
        return builder;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Collection getElementos() {
        return elementos;
    }

    public void setElementos(Collection elementos) {
        this.elementos = elementos;
    }

    public void setElemento(T elemento) {
        this.elemento = elemento;
    }

    public T getElemento() {
        return elemento;
    }

    public Long getTotal() {
        return total;
    }

}
