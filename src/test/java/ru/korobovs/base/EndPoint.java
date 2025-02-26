package ru.korobovs.base;

public enum EndPoint {
    USERS("users");

    private String endPoint;

    EndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
