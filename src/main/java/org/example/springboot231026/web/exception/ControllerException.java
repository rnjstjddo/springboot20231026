package org.example.springboot231026.web.exception;


import org.springframework.core.NestedCheckedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.UncheckedIOException;

@ControllerAdvice
public class ControllerException {

    @ExceptionHandler(UncheckedIOException.class)
    public String uncheckedIOException(UncheckedIOException e){
        System.out.println("ControllerException클래스 진입 uncheckedIOException() 메소드 /home/home 이동");
        e.printStackTrace();
        return "redirect:/home/home";
    }

    @ExceptionHandler(NestedCheckedException.class)
    public String nestedCheckedException(NestedCheckedException e){
        System.out.println("ControllerException클래스 진입 nestedCheckedException() 메소드 /home/home 이동");
        e.printStackTrace();
        return "redirect:/home/home";
    }

    @ExceptionHandler(Exception.class)
    public String exception(Exception e){
        System.out.println("ControllerException클래스 진입 exception() 메소드 /home/home 이동");
        e.printStackTrace();
        return "redirect:/home/home";
    }


}
