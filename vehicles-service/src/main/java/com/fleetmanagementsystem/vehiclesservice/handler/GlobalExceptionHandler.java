package com.fleetmanagementsystem.vehiclesservice.handler;

import com.fleetmanagementsystem.vehiclesservice.exception.VehicleEntityNotFoundException;
import com.fleetmanagementsystem.vehiclesservice.mapper.CustomResponse;
import com.fleetmanagementsystem.vehiclesservice.utils.ResponseTypeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Map<String, String> errors = new HashMap<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    errors.put(((FieldError) error).getField(), error.getDefaultMessage());

                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new CustomResponse<>(
                        "Validation failed for request parameters. Please check the errors.",
                        ResponseTypeEnum.FAILED,
                        errors
                )
        );
    }


    @ExceptionHandler(VehicleEntityNotFoundException.class)
    public ResponseEntity<CustomResponse<?>> handle(VehicleEntityNotFoundException exp) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new CustomResponse<>(exp.getMsg(),ResponseTypeEnum.FAILED,null)
                );
    }

}
