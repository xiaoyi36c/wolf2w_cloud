package xyz.bycor.article.controller.backend;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.bycor.article.domain.Destination;
import xyz.bycor.article.domain.Region;
import xyz.bycor.article.service.DestinationService;
import xyz.bycor.article.service.RegionService;
import xyz.bycor.core.qo.QueryObject;
import xyz.bycor.core.response.R;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author baishuai
 * @data: 2023-11-27 18:53:23
 * @Description: 针对表【region】的数据库操作Controller
 *
 */

@RestController
@RequestMapping("/backend/regions")
@Api(tags = "区域信息接口")
@Slf4j
@Validated
@RefreshScope
public class RegionBackendController {

    private final RegionService regionService;

    private final DestinationService destinationService;

    public RegionBackendController(RegionService regionService, DestinationService destinationService) {
        this.regionService = regionService;
        this.destinationService = destinationService;
    }
    /**
     * 分页查询所有区域
     * @return
     */
    @ApiOperation(value = "分页查询所有区域")
    @GetMapping()
    public R<Page<Region>> listPage(QueryObject qo) {
        log.info("查询所有目地址");
        return R.ok(regionService.findPage(qo));
    }

    @GetMapping("/{id}")
    public R<Region> get(@PathVariable @NotNull(message = "id 不能为空") Long id) {
        log.info("查询单个区域");
        return R.ok(regionService.getById(id));
    }


    /**
     * 新增区域
     * @return
     */
    @ApiOperation(value = "新增区域")
    @PostMapping()
    public R<?> add(Region region) {
        log.info("新增区域");
        regionService.save(region);
        return R.ok();
    }
    /**
     * 新增区域
     * @return
     */
    @ApiOperation(value = "修改区域")
    @PutMapping()
    public R<?> save(Region region) {
        log.info("修改区域");
        regionService.updateById(region);
        return R.ok();
    }

    /**
     * 删除区域
     * @return
     */
    @ApiOperation(value = "删除区域")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable @NotNull(message = "id 不能为空") Long id) {
        log.info("删除区域");
        regionService.removeById(id);
        return R.ok();
    }


    /**
     * 查看区域目的地
     * @return
     */
    @ApiOperation(value = "查看区域目的地")
    @GetMapping("/{id}/destination")
    public R<?> destByRegionId(@PathVariable @NotNull(message = "id 不能为空") Long id) {
        log.info("查看区域目的地");
       List<Destination> destinations = destinationService.findByRegionId(id);

        return R.ok(destinations);
    }

}
