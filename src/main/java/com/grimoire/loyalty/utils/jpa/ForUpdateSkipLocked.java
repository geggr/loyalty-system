package com.grimoire.loyalty.utils.jpa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.LockOptions;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Lock(LockModeType.PESSIMISTIC_WRITE)
@QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "" + LockOptions.NO_WAIT)})
@Transactional
public @interface ForUpdateSkipLocked {
    
}
