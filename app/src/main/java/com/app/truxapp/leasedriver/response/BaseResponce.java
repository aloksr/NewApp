package com.app.truxapp.leasedriver.response;

/**
 * Created by sharadsingh on 12/07/17.
 */

public class BaseResponce  {
    private String errorCode;
    private String errorMesaage;
    private String versionCheck;
    private String count;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMesaage() {
        return errorMesaage;
    }

    public void setErrorMesaage(String errorMesaage) {
        this.errorMesaage = errorMesaage;
    }

    public String getVersionCheck() {
        return versionCheck;
    }

    public void setVersionCheck(String versionCheck) {
        this.versionCheck = versionCheck;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


}
