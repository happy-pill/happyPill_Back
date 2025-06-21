package com.happypill.application.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity<T extends Serializable> implements Persistable<T> {

     @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMPTZ", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ")
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
