package com.application.demo.book.web.rest.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;

public class BaseResponse<T> {

    private final Boolean status;
    private final String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime timeStamp;

    private final T data;

    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public T getData() {
        return data;
    }

    public BaseResponse(Boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
        this.data = data;
    }
}
