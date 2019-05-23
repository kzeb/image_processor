package com.example.java_9.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Wrong argument")
public class ScaleException extends RuntimeException {
}