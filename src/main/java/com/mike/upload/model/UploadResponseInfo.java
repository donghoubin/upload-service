package com.mike.upload.model;

/**
 * @Description:
 * @Author: Mike Dong
 * @Date: 2019/12/2 23:41.
 */
public class UploadResponseInfo {
    private String responseState;
    private String companyName;
    private String stockExchangeName;
    private int totalImportedNumber;
    private String startTime;
    private String endTime;

    public String getResponseState() {
        return responseState;
    }

    public void setResponseState(String responseState) {
        this.responseState = responseState;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStockExchangeName() {
        return stockExchangeName;
    }

    public void setStockExchangeName(String stockExchangeName) {
        this.stockExchangeName = stockExchangeName;
    }

    public int getTotalImportedNumber() {
        return totalImportedNumber;
    }

    public void setTotalImportedNumber(int totalImportedNumber) {
        this.totalImportedNumber = totalImportedNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
