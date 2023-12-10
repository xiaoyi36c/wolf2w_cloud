package xyz.bycor.article.controller.frontend;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.article.domain.StrategyCatalog;
import xyz.bycor.article.service.StrategyService;
import xyz.bycor.core.response.R;

import java.util.List;

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
}
