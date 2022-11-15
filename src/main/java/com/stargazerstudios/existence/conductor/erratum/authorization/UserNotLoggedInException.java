package com.stargazerstudios.existence.conductor.erratum.authorization;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import org.springframework.http.HttpStatus;

public class UserNotLoggedInException extends AuthorizationException {

    public UserNotLoggedInException() {
        super("User needs to login to access the information. Please log in first.");
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
