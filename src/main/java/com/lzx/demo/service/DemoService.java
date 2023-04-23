package com.lzx.demo.service;

import com.lzx.demo.mapper.DemoMapper;
import com.lzx.demo.model.DemoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lzx
 * @Create 2023/4/23
 * @Desc
 */
@Service
public class DemoService {
    private final DemoMapper dao;

    @Autowired
    public DemoService(DemoMapper dao) {
        this.dao = dao;
    }

    public boolean insert(DemoModel model) {
        return dao.insert(model) > 0;
    }

    public DemoModel select(int id) {
        return dao.select(id);
    }

    public List<DemoModel> selectAll() {
        return dao.selectAll();
    }

    public boolean updateValue(DemoModel model) {
        return dao.updateValue(model) > 0;
    }

    public boolean delete(Integer id) {
        return dao.delete(id) > 0;
    }
}
