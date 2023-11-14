package com.example.springboot.service;


import com.example.springboot.request.SmsRequest;

public interface SmsSender {

    void sendSms(SmsRequest smaRequest);
}
