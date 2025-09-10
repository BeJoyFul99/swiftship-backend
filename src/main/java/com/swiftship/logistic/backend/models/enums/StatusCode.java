package com.swiftship.logistic.backend.models.enums;

public enum StatusCode {
    //     Authentication

    LOGIN_SUCCESS,          // user has successfully authenticated.  200 OK
    REGISTRATION_SUCCESS,   //	A new user account has been created successfully.	201 Created
    INVALID_CREDENTIALS,    //	The provided username or password was incorrect.	401 Unauthorized
    ACCOUNT_LOCKED,         //The user account is temporarily locked due to too many failed login attempts.	403 Forbidden
    EMAIL_ALREADY_EXISTS,   //	A user with this email address already exists in the system.	409 Conflict
    USERNAME_ALREADY_EXISTS,

    //      Packing & Tracking
    PACKAGE_CREATED,    //A new package has been successfully created.	201 Created
    PACKAGE_MERGED, //Multiple packages have been successfully merged into one.	200 OK
    PACKAGE_NOT_FOUND,  //The tracking number provided does not match any packages.	404 Not Found
    UPDATE_FAILED,  //An attempt to update a package failed due to invalid data.	400 Bad Request
    MERGE_CONDITION_FAILED, //The packages cannot be merged because they don't meet the merge criteria (e.g., different origin/destination).	400 Bad Request

    //      General API & System
    OPERATION_SUCCESS, //A generic success message for an operation without a more specific code.	200 OK
    UNKOWN_ERROR,//  500
    VALIDATION_ERROR, //One or more fields in the request body were invalid.	400 Bad Request
    UNAUTHORIZED_ACCESS, //The user does not have permission to access the requested resource.	403 Forbidden
    INTERNAL_SERVER_ERROR, //A general, unexpected error occurred on the server.	500 Internal Server Error
}
