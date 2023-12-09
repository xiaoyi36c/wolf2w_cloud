package xyz.bycor.article.controller.backend;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.bycor.article.domain.StrategyTheme;
import xyz.bycor.article.service.StrategyThemeService;
import xyz.bycor.core.qo.QueryObject;
import xyz.bycor.core.response.R;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author baishuai
 * @data: 2023-11-27 18:53:23
 * @Description: 针对表【strategyTheme】的数据库操作Controller
 *
 */

@RestController
@RequestMapping("/backend/strategies/themes")
@Api(tags = "攻略主题接口")
@Slf4j
@Validated
@RefreshScope
public class StrategyThemeBackendController {

    private final StrategyThemeService strategyThemeService;

    public StrategyThemeBackendController(StrategyThemeService strategyThemeService) {
        this.strategyThemeService = strategyThemeService;
    }

    /**
     * 分页查询所有攻略主题
     * @return 分页对象
     */
    @ApiOperation(value = "分页查询所有攻略主题")
    @GetMapping("/list")
    public R<List<StrategyTheme>> list() {
        log.info("查询所有攻略主题");
        return R.ok(strategyThemeService.list());
    }
    

    /**
     * 分页查询所有攻略主题
     * @return
     */
    @ApiOperation(value = "分页查询所有攻略主题")
    @GetMapping()
    public R<Page<StrategyTheme>> listPage(QueryObject qo) {
        log.info("查询所有攻略主题");
        return R.ok(strategyThemeService.findPage(qo));
    }

    @GetMapping("/{id}")
    public R<StrategyTheme> get(@PathVariable @NotNull(message = "id 不能为空") Long id) {
        log.info("查询单个攻略主题");
        return R.ok(strategyThemeService.getById(id));
    }


    /**
     * 新增攻略主题
     * @return
     */
    @ApiOperation(value = "新增攻略主题")
    @PostMapping()
    public R<?> add(StrategyTheme strategyTheme) {
        log.info("新增攻略主题");
        strategyThemeService.save(strategyTheme);
        return R.ok();
    }
    /**
     * 新增攻略主题
     * @return
     */
    @ApiOperation(value = "修改攻略主题")
    @PutMapping()
    public R<?> save(StrategyTheme strategyTheme) {
        log.info("修改攻略主题");
        strategyThemeService.updateById(strategyTheme);
        return R.ok();
    }

    /**
     * 删除攻略主题
     * @return
     */
    @ApiOperation(value = "删除攻略主题")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable @NotNull(message = "id 不能为空") Long id) {
        log.info("删除攻略主题");
        strategyThemeService.removeById(id);
        return R.ok();
    }



}
