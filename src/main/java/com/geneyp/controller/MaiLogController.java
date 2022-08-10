package com.geneyp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geneyp.common.R;
import com.geneyp.entity.MaiLog;
import com.geneyp.entity.Que;
import com.geneyp.entity.User;
import com.geneyp.service.MaiLogService;
import com.geneyp.service.QueService;
import com.geneyp.vo.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/5 17:14
 * @description com.geneyp.controller
 */
@RestController
@RequestMapping("/logs")
@Slf4j
public class MaiLogController {

    @Resource
    MaiLogService maiLogService;
    @Resource
    QueService queService;

    /**
     * 根据id查询排队log
     *
     * @return Que
     */
    @GetMapping(value = "/get/{shopId}/{gameId}")
    public R<Que> getMaiLogByIds(@PathVariable Integer shopId, @PathVariable Integer gameId) {
        try {
            List<MaiLog> maiLogs = maiLogService.getByIds(shopId, gameId);
            if (maiLogs.size() != 0) {
                return R.success(maiLogs, "查询成功");
            } else {
                return R.success(null, "查询成功，没log");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/add/{shopId}/{gameId}")
    public R<User> addMaiLogByIds(@RequestBody MaiLog maiLog, @PathVariable Integer shopId, @PathVariable Integer gameId) {
        try {
            maiLog.setShopId(shopId);
            maiLog.setGameId(gameId);
            maiLogService.save(maiLog);
            return R.success("上传日志成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("修改失败：" + e.getMessage());
        }
    }
}
