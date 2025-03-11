package com.pioli.users.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(
            Throwable throwable, Method method, Object... obj) {

        try (FileWriter fw = new FileWriter("async-errors.log", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("Exceção no método " + method.getName() + ": " + throwable.getMessage());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 