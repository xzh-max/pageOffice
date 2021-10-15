package com.pageOfficeServer.response;

import java.io.Serializable;

public class ReturnUrlResponse<T> implements Serializable {

    private String message;

    private T data;


}
