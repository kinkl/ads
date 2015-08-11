package ru.kinkl.ads.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ext<T> {
    private T data;

    private Boolean success;

    private String message;

    public Ext(T data, Boolean success, String message) {
        this.data = data;
        this.success = success;
        this.message = message;
    }

    public Ext(T data) {
        this(data, true, null);
    }
}
