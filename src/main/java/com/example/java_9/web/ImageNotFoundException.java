package com.example.java_9.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Image not found")
public class ImageNotFoundException extends RuntimeException {
}