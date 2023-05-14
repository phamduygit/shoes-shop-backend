package com.shoesshop.backend.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;


@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"createdAt", "createdBy", "updatedBy", "updatedAt"})
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(insertable = false)
    @CreationTimestamp
    private Timestamp updatedAt;

    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;
}
