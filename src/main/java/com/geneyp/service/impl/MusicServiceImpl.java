package com.geneyp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geneyp.entity.Music;
import com.geneyp.dao.MusicMapper;
import com.geneyp.service.MusicService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/23 01:52
 * @description com.geneyp.service.impl
 */
@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements MusicService {

    @Override
    public List<Music> getByKeyword(String keyword) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", keyword)
                .or()
                .like("title", keyword)
                .or()
                .like("charts", keyword)
                .or()
                .like("basic_info", keyword)
                .or()
                .like("nickname", keyword)
                .eq("available", 1);
        return list(queryWrapper);
    }

    @Override
    @Async
    public Future<List<Music>> getByTerm(String minLv, String maxLv, String genre, String from, Integer bpm) {
        QueryWrapper<Music> wrapper = new QueryWrapper<>();
        if (!genre.equals("") && !genre.equals("任意流派")) {
            wrapper.eq("genre", genre);
        }
        if (!from.equals("") && !from.equals("任意版本")) {
            wrapper.eq("maifrom", from);
        }
        if (bpm != -1) {
            wrapper.eq("bpm", bpm);
        }
        wrapper.and(wp -> wp.between("basic_ds", minLv, maxLv)
                .or()
                .between("advanced_ds", minLv, maxLv)
                .or()
                .between("expert_ds", minLv, maxLv)
                .or()
                .between("master_ds", minLv, maxLv)
                .or()
                .between("remaster_ds", minLv, maxLv));
        return new AsyncResult<>(list(wrapper));
    }
}
