package service;

import model.requests.ClearRequest;

import java.util.UUID;

public class AuthService {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public void clear(ClearRequest clearRequest){}
}
