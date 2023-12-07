package xyz.bycor.article.controller.backend;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.bycor.article.domain.Region;
import xyz.bycor.article.service.RegionService;
import xyz.bycor.core.response.R;

import java.util.List;

/**
 * @author baishuai
 * @data: 2023-11-27 18:53:23
 * @Description: 针对表【region】的数据库操作Controller
 *
 */

@RestController
@RequestMapping("/region")
@Api(tags = "区域信息接口")
@Slf4j
@Validated
@RefreshScope
public class RegionBackendController {

    private final RegionService regionService;

    public RegionBackendController(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * 查询所有区域信息
     * @return
     */

    @ApiOperation(value = "查询所有区域信息")
    @GetMapping("/list")
    public R<List<Region>> list() {
        log.info("查询所有区域信息");
        return R.ok(regionService.list());
    }

    /**
     * 分页查询所有区域信息
     * @return 分页对象
     */
    @ApiOperation(value = "分页查询所有区域信息")
    @GetMapping("/listPage/{page}/{size}")
    public R<Page<Region>> listPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        log.info("查询所有区域信息");
        Page<Region> regionPage = new Page<>(page, size);
        return R.ok(regionService.page(regionPage));
    }

    /**
     * 根据id查询区域信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询区域信息")
    @GetMapping("/getById/{id}")
    public R<Region> get(@PathVariable("id") Long id) {
        log.info("根据id查询区域信息");
        return R.ok(regionService.getById(id));
    }


    /**
     * 根据id删除区域信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id删除区域信息")
    @DeleteMapping("/deleteById/{id}")
    public R delete(@PathVariable("id") Long id) {
        log.info("根据id删除区域信息");
        regionService.removeById(id);
        return R.ok();
    }


    /**
     * 根据id更新区域信息
     * @param id
     * @param region
     * @return
     */
    @ApiOperation(value = "根据id更新区域信息")
    @PutMapping("/{id}")
    public R update(@PathVariable("id") Long id, @RequestBody Region region) {
        Region byId = regionService.getById(id);
        log.info("根据id更新区域信息");
        region.setId(id);
        regionService.updateById(region);
        return  R.ok();
    }

    /**
     * 新增区域信息
     * @param region
     * @return
     */
    @ApiOperation(value = "新增区域信息")
    @PostMapping
    public R save(@RequestBody Region region) {
        log.info("新增区域信息");
        regionService.save(region);
        return R.ok();
    }

}
