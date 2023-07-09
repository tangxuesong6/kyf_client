package com.example.openaiclient.listener;

public interface ResponseCallback<T> {
    void success(T resp);

    void fail(String error);
}