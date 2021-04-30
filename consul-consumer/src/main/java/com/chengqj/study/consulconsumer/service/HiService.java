package com.chengqj.study.consulconsumer.service;

import com.chengqj.study.consulconsumer.api.HiFeignInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author chengqj
 * @Date 2020/12/27 15:50
 * @Desc
 */
@Service
public class HiService {
    @Autowired
    HiFeignInterface hiFeign;

    public String sayHi(String name) {
        return hiFeign.sayHiFromClient(name);
    }

}
