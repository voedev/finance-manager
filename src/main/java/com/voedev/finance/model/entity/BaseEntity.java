package com.voedev.finance.model.entity;

import java.io.Serializable;

// todo need update
public interface BaseEntity<T extends Serializable> {

    T getId();

    void setId(T id);
}
