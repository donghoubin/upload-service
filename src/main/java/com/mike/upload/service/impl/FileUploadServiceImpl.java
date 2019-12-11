package com.mike.upload.service.impl;


import com.mike.upload.Feign.CompanyService;
import com.mike.upload.controller.FileUploadController;
import com.mike.upload.dao.FileUploadRepository;
import com.mike.upload.entity.Company;
import com.mike.upload.entity.StockPrice;
import com.mike.upload.model.UploadResponseInfo;
import com.mike.upload.service.FileUploadService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Mike Dong
 * @Date: 2019/12/3 9:51.
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    private CompanyService companyService;

    @Override
    public UploadResponseInfo uploadFile(HttpServletRequest request, MultipartFile file) throws Exception {
        UploadResponseInfo uploadResponseInfo = new UploadResponseInfo();
        if (!file.isEmpty()) {
            InputStream inputStream = file.getInputStream();
            List<StockPrice> list = getStockPriceListByExcel(inputStream, file.getOriginalFilename());
            Iterable<StockPrice> stockPrices = fileUploadRepository.saveAll(list);

            StockPrice stockPrice = list.get(0);
            StockPrice endTimeStockPrice = list.get(list.size() - 1);
            // get company name by company code.
            String companyCode =  stockPrices.iterator().next().getCompanyCode().trim();
            Company company = companyService.getCompanyByCompanyCode(500112).getBody();
            String companyName = company.getCompanyName();
            // get stock exchange name by stock code.
            String stockCode = stockPrice.getStockExchange();
            String stockExchangeName = stockCode + "(" + "TestStockExchangeName" + ")";
            // total record number.
            int totalImportedNumber = list.size();
            // start time and end time
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = format1.format(new Date(stockPrice.getDate().getTime() + stockPrice.getTime().getTime()));
            String endTime = format1.format(new Date(endTimeStockPrice.getDate().getTime() + endTimeStockPrice.getTime().getTime()));
            uploadResponseInfo.setCompanyName(companyName);
            uploadResponseInfo.setStockExchangeName(stockExchangeName);
            uploadResponseInfo.setTotalImportedNumber(totalImportedNumber);
            uploadResponseInfo.setStartTime(startTime);
            uploadResponseInfo.setEndTime(endTime);

        }
        return uploadResponseInfo;
    }

    public List getStockPriceListByExcel(InputStream in, String fileName) throws Exception {
        List<StockPrice> list = new ArrayList<>();
        //create excel workbook
        Workbook work = this.getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("Create Excel workbook empty！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        List<StockPrice> li = new ArrayList<>();
        DateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            for (int j = sheet.getFirstRowNum() + 1; j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }
                if (row.getLastCellNum() > 0) {
                    if (!"".equalsIgnoreCase(row.getCell(0).getStringCellValue()) &&
                            !"".equalsIgnoreCase(row.getCell(1).getStringCellValue()) &&
                            0 != row.getCell(2).getNumericCellValue() && null != row.getCell(3).getDateCellValue() &&
                            !"".equalsIgnoreCase(row.getCell(4).getStringCellValue())) {
                        StockPrice stockPrice = new StockPrice();
                        stockPrice.setCompanyCode(row.getCell(0).getStringCellValue());
                        stockPrice.setStockExchange(row.getCell(1).getStringCellValue());
                        stockPrice.setCurrentPrice(row.getCell(2).getNumericCellValue());
                        stockPrice.setDate(row.getCell(3).getDateCellValue());
                        // SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm");
                        Date time = format1.parse(row.getCell(4).getStringCellValue());
                        stockPrice.setTime(time);
                        list.add(stockPrice);
                    }
                }
            }
        }
        work.close();
        return list;
    }

    public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inStr);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("Pleas upload excel file!");
        }
        return workbook;
    }
}
