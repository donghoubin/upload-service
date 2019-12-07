package com.mike.upload.dao;

import com.mike.upload.entity.StockPrice;
import org.springframework.data.repository.CrudRepository;

/**
 * @Description:
 * @Author: Mike Dong
 * @Date: 2019/12/3 9:53.
 */
public interface FileUploadRepository extends CrudRepository<StockPrice, Integer> {
}
