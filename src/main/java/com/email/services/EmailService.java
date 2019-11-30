package com.email.services;

import com.email.models.CustomMessage;

public interface EmailService {

    public void send(CustomMessage message);
    public void reply();
    public void forward();

}
