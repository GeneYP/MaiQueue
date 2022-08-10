package com.geneyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geneyp.entity.Music;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/23 02:06
 * @description com.geneyp.service
 */
public interface MusicService extends IService<Music> {

    List<Music> getByKeyword(String keyword);

    Future<List<Music>> getByTerm(String minLv, String maxLv, String genre, String from, Integer bpm);
}
