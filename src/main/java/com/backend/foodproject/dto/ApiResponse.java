package com.backend.foodproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    private int responseCode;
    private String status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(HttpStatus.OK.value(),"Success", null,data);
    }
    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>(HttpStatus.OK.value(),"Success", message,data);
    }
}
