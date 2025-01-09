package ru.korobovs.pojo;

public class UserNewPojo {

    private String name;
    private String job;
    private String id;
    private String createdAt;
    private String updatedAt;

    public UserNewPojo() {
    }

    public UserNewPojo(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
