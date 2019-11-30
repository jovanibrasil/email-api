package com.email.services;

import javax.mail.Message;
import java.util.List;

public interface EmailService {

    public void send(Message message);
    public void reply();
    public void forward();
    public void delete();
    public List<Message> getAll();
}
