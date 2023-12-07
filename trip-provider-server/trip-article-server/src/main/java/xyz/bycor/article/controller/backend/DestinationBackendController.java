package xyz.bycor.article.controller.backend;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.bycor.article.domain.Destination;
import xyz.bycor.article.qo.DestinationQueryObject;
import xyz.bycor.article.service.DestinationService;
import xyz.bycor.core.response.R;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author baishuai
 * @data: 2023-11-27 18:53:23
 * @Description: 针对表【destination】的数据库操作Controller
 *
 */

@RestController
@RequestMapping("/backend/destinations")
@Api(tags = "目地址接口")
@Slf4j
@Validated
@RefreshScope
public class DestinationBackendController {

    private final DestinationService destinationService;

    public DestinationBackendController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    /**
     * 分页查询所有目地址
     * @return 分页对象
     */
    @ApiOperation(value = "分页查询所有目地址")
    @GetMapping("/list")
    public R<List<Destination>> list() {
        log.info("查询所有目地址");
        return R.ok(destinationService.list());
    }
    

    /**
     * 分页查询所有目的地
     * @return
     */
    @ApiOperation(value = "分页查询所有目的地")
    @GetMapping()
    public R<Page<Destination>> listPage(DestinationQueryObject qo) {
        log.info("查询所有目地址");
        return R.ok(destinationService.findPage(qo));
    }

    @GetMapping("/{id}")
    public R<Destination> get(@PathVariable @NotNull(message = "id 不能为空") Long id) {
        log.info("查询单个目的地");
        return R.ok(destinationService.getById(id));
    }


    /**
     * 新增目的地
     * @return
     */
    @ApiOperation(value = "新增目的地")
    @PostMapping()
    public R<?> add(Destination destination) {
        log.info("新增目的地");
        destinationService.save(destination);
        return R.ok();
    }
    /**
     * 新增目的地
     * @return
     */
    @ApiOperation(value = "修改目的地")
    @PutMapping()
    public R<?> save(Destination destination) {
        log.info("修改目的地");
        destinationService.updateById(destination);
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
        destinationService.removeById(id);
        return R.ok();
    }

    /**
     * 吐司,面包屑
     * @return
     */
    @ApiOperation(value = "吐司,面包屑")
    @GetMapping("/toasts/{parentId}")
    public R<List<Destination>> toasts(@PathVariable Long parentId) {
        log.info("吐司,面包屑");
        return R.ok(destinationService.findToastList(parentId));
    }


}
