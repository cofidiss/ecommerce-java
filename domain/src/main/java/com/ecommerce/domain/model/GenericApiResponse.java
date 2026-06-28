package com.ecommerce.domain.model;


public class GenericApiResponse<TError, TSuccess> {
    private int statusCode;
    private TError errorData;
    private TSuccess successData;

    // Constructor
    public GenericApiResponse() {
        this.statusCode = 200;
    }

    // Getter for Success
    public boolean isSuccess() {
        return errorData == null;
    }

    // Getter and Setter for StatusCode
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    // Getter and Setter for ErrorData
    public TError getErrorData() {
        return errorData;
    }

    public void setErrorData(TError errorData) {
        this.errorData = errorData;
    }

    // Getter and Setter for SuccessData
    public TSuccess getSuccessData() {
        return successData;
    }

    public void setSuccessData(TSuccess successData) {
        if (this.errorData != null) {
            throw new IllegalStateException("Success data cannot be set when Error Data has been set.");
        }
        this.successData = successData;
    }


}
