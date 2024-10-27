package com.example.sprintproject.model;

public interface IUser {
    String getEmail();
    String getPassword();
    void addUserToFirestore(String userInfo);
}
