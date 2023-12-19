package xyz.bycor.article.controller.frontend;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.bycor.article.domain.Strategy;
import xyz.bycor.article.domain.StrategyCatalog;
import xyz.bycor.article.domain.StrategyContent;
import xyz.bycor.article.domain.StrategyTheme;
import xyz.bycor.article.qo.StatDataQuery;
import xyz.bycor.article.qo.StrategyQuery;
import xyz.bycor.article.service.StrategyService;
import xyz.bycor.auth.anno.RequestUser;
import xyz.bycor.auth.anno.RequiredLogin;
import xyz.bycor.auth.vo.LoginResponseUser;
import xyz.bycor.core.event.StatDataEvent;
import xyz.bycor.core.response.R;
import xyz.bycor.core.utils.SpringContextUtil;

import java.util.List;
import java.util.Map;

/**
 * @author baishuai
 * @data: 2023-11-27 18:53:23
 * @Description: 针对表【strategy】的数据库操作Controller
 *
 */

@RestController
@RequestMapping("/strategies")
@Api(tags = "攻略详细接口")
@Slf4j
@Validated
@RefreshScope
public class StrategyController {

    private final StrategyService strategyService;

    public StrategyController(StrategyService strategyService) {
        this.strategyService = strategyService;
    }

    /**
     * 查询 分类分组
     * @return 分页对象
     */
    @ApiOperation(value = "查询所有攻略详细")
    @GetMapping("/groups")
    public R<List<StrategyCatalog>> catalogGroups(Long destId) {
        log.info("查询所有攻略详细");
        return R.ok(strategyService.findCatalogGroupsByDestId(destId));
    }

    /**
     * 概况下的攻略内容
     * @return
     */

    @ApiOperation(value = "概况下的攻略内容")
    @GetMapping("/content")
    public R<StrategyContent> content(Long id) {
        log.info("概况下的攻略文章");
        return R.ok(strategyService.findContent(id));
    }

    /**
     * 概况下的攻略 top3
     *
     * @return
     */

    @ApiOperation(value = "概况下的攻略 top3")
    @GetMapping("/viewnumTop3")
    public R<List<Strategy>> viewnumTop3(Long destId) {
        log.info("概况下的攻略");
        return R.ok(strategyService.findViewingTop3(destId));
    }

    /**
     * 概况下的攻略
     *
     * @return
     */

    @ApiOperation(value = "概况下的攻略")
    @GetMapping("/detail")
    public R<Strategy> detail(Long id) {
        log.info("概况下的攻略");
        // 发布攻略的浏览事件
        StatDataEvent.Data data = StatDataEvent.Data.builder(id).field("viewnum").value(1).build();
        SpringContextUtil.publishEvent(new StatDataEvent(data));
        Strategy strategy = strategyService.getById(id);
        return R.ok(strategy.getState() == Strategy.STATE_PUBLISH ? strategy : new Strategy());
    }

    /**
     * 概况下的攻略标签
     */
    @ApiOperation(value = "概况下的攻略标签")
    @GetMapping("/themes/{destId}")
    public R<List<StrategyTheme>>  themes(@PathVariable Long destId) {
        log.info("概况下的攻略标签");
        return R.ok(strategyService.getThemesByDestId(destId));
    }


    /**
     * 概况下的攻略分页查询
     */
    @ApiOperation(value = "概况下的攻略分页查询")
    @GetMapping("/query")
    public R<Page<Strategy>>  query(StrategyQuery qo) {
        log.info("概况下的攻略分页查询");
        return R.ok(strategyService.queryStrategy(qo));
    }


    /**
     * 旅游攻略下的排行榜
     */

    @ApiOperation(value = "旅游攻略下的排行榜")
    @GetMapping("/ranks")
    public R<JSONObject> ranks() {
        log.info("旅游攻略下的排行榜");
        return R.ok(strategyService.findRangs());
    }

    /**
     * 旅游攻略下的条件查询
     */
    @ApiOperation(value = "旅游攻略下的条件查询")
    @GetMapping("/conditions")
    public R<JSONObject> findConditions() {
        log.info("旅游攻略下的条件查询");
        return R.ok(strategyService.findConditions());
    }


    /**
     * 概况下的攻略分页查询
     */
    @ApiOperation(value = "概况下的攻略分页查询全部")
    @GetMapping("/list")
    public R<Page<Strategy>>  queryList(StrategyQuery qo) {
        log.info("概况下的攻略分页查询");
        return R.ok(strategyService.queryList(qo));
    }


    @ApiOperation(value = "查询攻略统计数据")
    @GetMapping("/stat/lastSevenDay")
   public List<Map<String, Object>> findStatByLastSevenDay(StatDataQuery statQuery){
        return strategyService.findStatByLastSevenDay(statQuery);
    }


    @ApiOperation(value = "持久化攻略统计数据")
    @PostMapping("/stat/saveBatch")
    public void saveBatch(@RequestBody List<Strategy> list) {
        log.info("持久化攻略统计数据: {}", list);
       strategyService.saveOrUpdateBatch(list);
    }


    @RequiredLogin
    @GetMapping("/thumbnumIncr")
    @ApiOperation(value = "顶攻略")
    public R<?> thumbnumIncr(Long sid, @RequestUser LoginResponseUser user) {
        log.info("顶攻略");
        strategyService.thumbnumIncr(sid, user.getId());
        return R.ok();
    }

    @ApiOperation(value = "查询攻略统计数据")
    @GetMapping("stat/data")
    public R<Map<Object,Object>> statData(Long id) {
        log.info("查询攻略统计数据");
        return R.ok(strategyService.findstatData(id));
    }


    @ApiOperation(value = "根据目的地id查询攻略 5")
    @GetMapping("/findStrategiesByDestId")
    public List<Strategy> findStrategiesByDestId(Long destId) {
        return strategyService.findStrategiesByDestId(destId,5);
    }

}
