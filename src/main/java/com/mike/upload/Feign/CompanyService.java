package com.mike.upload.Feign;


import com.mike.upload.entity.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description:
 * @Author: Mike Dong
 * @Date: 2019/12/7 15:36.
 */
@FeignClient(value = "company-service")
public interface CompanyService {

    @GetMapping("/company/{companyCode}")
    public ResponseEntity<Company> getCompanyByCompanyCode(@PathVariable("companyCode") long companyCode);
}
