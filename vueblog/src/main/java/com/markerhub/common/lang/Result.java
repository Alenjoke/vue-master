package com.markerhub.common.lang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public static Result status(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public static Result success(Object obj) {

        return new Result(200, "操作成功", obj);
    }

    public static Result fail(Object obj) {

        return new Result(400, "操作失败", obj);
    }

}
