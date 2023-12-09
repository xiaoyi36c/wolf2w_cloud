package xyz.bycor.article.controller.backend;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.bycor.article.domain.StrategyCatalog;
import xyz.bycor.article.service.StrategyCatalogService;
import xyz.bycor.core.qo.QueryObject;
import xyz.bycor.core.response.R;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author baishuai
 * @data: 2023-11-27 18:53:23
 * @Description: 针对表【strategyCatalog】的数据库操作Controller
 *
 */

@RestController
@RequestMapping("/backend/strategys/catalogs")
@Api(tags = "目地址接口")
@Slf4j
@Validated
@RefreshScope
public class StrategyCatalogBackendController {

    private final StrategyCatalogService strategyCatalogService;

    public StrategyCatalogBackendController(StrategyCatalogService strategyCatalogService) {
        this.strategyCatalogService = strategyCatalogService;
    }

    /**
     * 分页查询所有目地址
     * @return 分页对象
     */
    @ApiOperation(value = "分页查询所有目地址")
    @GetMapping("/list")
    public R<List<StrategyCatalog>> list() {
        log.info("查询所有目地址");
        return R.ok(strategyCatalogService.list());
    }


    /**
     * 分页查询所有目的地
     * @return
     */
    @ApiOperation(value = "分页查询所有目的地")
    @GetMapping()
    public R<Page<StrategyCatalog>> listPage(QueryObject qo) {
        log.info("查询所有目地址");
        return R.ok(strategyCatalogService.findPage(qo));
    }

    @GetMapping("/{id}")
    public R<StrategyCatalog> get(@PathVariable @NotNull(message = "id 不能为空") Long id) {
        log.info("查询单个目的地");
        return R.ok(strategyCatalogService.getById(id));
    }


    /**
     * 新增目的地
     * @return
     */
    @ApiOperation(value = "新增目的地")
    @PostMapping()
    public R<?> add(StrategyCatalog strategyCatalog) {
        log.info("新增目的地");
        strategyCatalogService.save(strategyCatalog);
        return R.ok();
    }
    /**
     * 新增目的地
     * @return
     */
    @ApiOperation(value = "修改目的地")
    @PutMapping()
    public R<?> save(StrategyCatalog strategyCatalog) {
        log.info("修改目的地");
        strategyCatalogService.updateById(strategyCatalog);
        return R.ok();
    }

    /**
     * 删除目的地
     * @return
     */
    @ApiOperation(value = "删除目的地")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable @NotNull(message = "id 不能为空") Long id) {
        log.info("删除目的地");
        strategyCatalogService.removeById(id);
        return R.ok();
    }



}
