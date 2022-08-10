package com.geneyp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geneyp.common.R;
import com.geneyp.entity.Music;
import com.geneyp.service.MusicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/23 02:04
 * @description com.geneyp.controller
 */
@RestController
@RequestMapping("/music")
@Slf4j
public class MusicController {

    @Resource
    private MusicService musicService;

    /**
     * 所有
     *
     * @return
     */
    @GetMapping(value = "/all")
    public R<List<Music>> getAllMusic() {
        try {
            return R.success(musicService.list(), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 分页列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    public R<IPage<Music>> getPage(int current, int size) {
        try {
            Page<Music> page = new Page<>(current, size);
            return R.success(musicService.page(page), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据关键词模糊查询歌曲信息
     *
     * @return music
     */
    @GetMapping(value = "/name/{keyword}")
    public R<List<Music>> getMusicByName(@PathVariable String keyword) {
        try {
            return R.success(musicService.getByKeyword(keyword), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据条件查询歌曲信息
     *
     * @return music
     */
    @GetMapping(value = "/term/{minLv}/{maxLv}/{genre}/{from}/{bpm}")
    public R<List<Music>> getMusicByTerm(@PathVariable String minLv, @PathVariable Integer bpm, @PathVariable String from, @PathVariable String genre, @PathVariable String maxLv) {
        try {
            return R.success(musicService.getByTerm(minLv, maxLv, genre, from, bpm).get(), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    @GetMapping(value = "/random")
    public R<List<Music>> getRandomMusic() {
        try {
            // 总记录数
            int count = (int) musicService.count();
            // 随机数起始位置
            int randomCount = (int) (Math.random() * count);
            // 保证能展示10个数据
            if (randomCount > count - 10) {
                randomCount = count - 10;
            }
            QueryWrapper<Music> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("id");
            wrapper.last("limit " + randomCount + ", 10");
            List<Music> randomList = musicService.list(wrapper);
            return R.success(randomList, "随机成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据id查询歌曲信息
     *
     * @return music
     */
    @GetMapping(value = "/id/{id}")
    public R<Music> getMusicById(@PathVariable Integer id) {
        try {
            return R.success(musicService.getById(id), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }
}
