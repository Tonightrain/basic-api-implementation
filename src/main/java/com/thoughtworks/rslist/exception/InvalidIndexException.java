package com.thoughtworks.rslist.exception;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.http.ResponseEntity;

public class InvalidIndexException extends Throwable {
    public InvalidIndexException(String message) {
        super(message);
    }
}
