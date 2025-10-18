package com.deveria.identity.service.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtils {

    public static void logMethodInfo(String message){
        // Lấy stack trace hiện tại
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // Phần tử [2] là chính method gọi tới LogUtils.logMethodInfo()
        // Phần tử [3] là method chứa lệnh đó
        if (stackTrace.length > 2) {
            StackTraceElement caller = stackTrace[2];
            String className = caller.getClassName();
            String methodName = caller.getMethodName();

            log.info("[{}.{}] {}", className, methodName, message);
        } else {
            log.info(message);
        }
    }
}
