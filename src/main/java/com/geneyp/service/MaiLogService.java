package com.geneyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geneyp.entity.MaiLog;

import java.util.List;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/5 17:15
 * @description com.geneyp.service
 */
public interface MaiLogService extends IService<MaiLog> {
    List<MaiLog> getByIds(Integer shopId, Integer gameId);
}
