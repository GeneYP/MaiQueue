package com.geneyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geneyp.entity.Que;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/5 17:15
 * @description com.geneyp.service
 */
public interface QueService extends IService<Que> {
    Que getByIds(Integer shopId, String gameId);

    Que addEnd(Integer shopId, Integer gameId, String uuid, Integer version);

    Que quitByPosition(Integer shopId, Integer gameId, Integer position, Integer version);

    Que pushByIds(Integer shopId, Integer gameId, Integer version);

    Que moveByPosition(Integer shopId, Integer gameId, Integer cardFrom, Integer cartTo, Integer version);
}
