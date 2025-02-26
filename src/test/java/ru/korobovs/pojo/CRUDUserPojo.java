package ru.korobovs.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CRUDUserPojo {

    private String name;
    private String job;
    private String id;
    private String createdAt;

    public CRUDUserPojo() {
    }

    public CRUDUserPojo(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
