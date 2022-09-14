package com.atguigu.gmall.cart.exception;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 */
//@RestControllerAdvice  //所有@controller的统一切面
public class GlobalExceptionHandler {

    /**
     * 业务期间出现的所有异常都用 GmallException 包装
     * @param exception
     * @return
     */

    @ExceptionHandler(GmallException.class)
    public Result handleGmallException(GmallException exception){
        //业务状态的枚举类
        ResultCodeEnum codeEnum = exception.getCodeEnum();
        Result<String> result = Result.build("", codeEnum);
        return result;  //给前端的返回
    }

    @ExceptionHandler(NullPointerException.class)
    public String handlenullException(NullPointerException gmallException){

        return "haha";  //给前端的返回
    }

}
