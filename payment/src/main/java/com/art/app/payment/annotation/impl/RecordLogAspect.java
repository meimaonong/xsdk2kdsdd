package com.art.app.payment.annotation.impl;

import com.art.app.payment.PaymentException;
import com.art.app.payment.annotation.RecordLog;
import com.art.app.payment.utils.JackJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Aspect
@Slf4j
public class RecordLogAspect {

    @Around("execution(* *(..)) && @annotation(anno)")
    public Object log(ProceedingJoinPoint pjp, RecordLog anno) throws Throwable {
        long begin = System.currentTimeMillis();
        String msg = null;
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            if (e instanceof PaymentException) {
                msg = "[" + ((PaymentException) e).getCode() + "]" + e.getMessage();
            } else {
                msg = e.getMessage();
            }
            throw e;
        } finally {
            String desc = StringUtils.isEmpty(anno.desc()) ? "" : anno.desc() + ", ";
            log.info("{}, 入参 -> {}, 出参 -> {}, 耗时 -> {}",
                    desc + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName(),
                    anno.inputParams() ? JackJsonUtil.toJson(pjp.getArgs()) : null,
                    anno.outputParams() ? (StringUtils.isEmpty(msg) ? JackJsonUtil.toJson(result) : msg) : null,
                    System.currentTimeMillis() - begin);
        }
        return result;
    }


}
