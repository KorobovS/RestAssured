package ru.korobovs.base;

public enum EndPoint {
    USERS("users"),
    REGISTER("register"),
    LOGIN("login");

    private String endPoint;

    EndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
