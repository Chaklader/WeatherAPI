package com.weather.api.weatherapi.dao.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;


@MappedSuperclass
public abstract class AbstractEntity {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_timestamp", nullable = false)
    private Date creationTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modification_timestamp", nullable = false)
    private Date modificationTimestamp;

    @PrePersist
    public void onCreate() {
        creationTimestamp = new Date();
        modificationTimestamp = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        modificationTimestamp = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}
