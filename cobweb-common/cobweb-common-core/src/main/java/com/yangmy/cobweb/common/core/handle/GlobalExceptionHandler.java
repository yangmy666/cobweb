package com.yangmy.cobweb.common.core.handle;

import com.alibaba.fastjson.JSON;
import com.yangmy.cobweb.common.core.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YangMingYang
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private HttpServletRequest request;

    @ExceptionHandler(value= {BindException.class})
    public R<Void> handleValidException(BindException e){
        String requestURI = request.getRequestURI();
        log.info("请求地址'{}',参数校验出现异常'{}',异常类型'{}'",requestURI,e,e.getClass());
        BindingResult bindingResult=e.getBindingResult();
        Map<String,String> map=new HashMap<>();
        bindingResult.getFieldErrors().forEach((item)->
                map.put(item.getField(),item.getDefaultMessage()));
        return R.valid(JSON.toJSONString(map));
    }

    @ExceptionHandler(value= {ConstraintViolationException.class})
    public R<Void> handleConstraintViolationException(ConstraintViolationException e){
        String requestURI = request.getRequestURI();
        log.info("请求地址'{}',参数校验出现异常'{}',异常类型'{}'",requestURI,e,e.getClass());
        return R.valid(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleRuntimeException(RuntimeException e) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生运行时异常{},异常类型{}", requestURI, e,e.getClass());
        return R.failed(e.getMessage());
    }

    @ExceptionHandler(value= Throwable.class)
    public R<Void> handleException(Throwable e){
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生异常{},异常类型{}", requestURI, e,e.getClass());
        return R.failed(e.getMessage());
    }

}
