package com.reliaquest.api.controller;

public class TooMayRequestsException extends Exception {
    public TooMayRequestsException(){
        super("Too many requests! Please, Try again after sometime!");
    }
}
