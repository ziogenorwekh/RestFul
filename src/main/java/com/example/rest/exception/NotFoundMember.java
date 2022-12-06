package com.example.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Member not in database")
public class NotFoundMember extends RuntimeException {

    @Override
    public String getMessage() {
        return "Member not in database";
    }
}
