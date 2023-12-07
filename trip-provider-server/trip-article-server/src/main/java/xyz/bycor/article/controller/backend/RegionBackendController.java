package xyz.bycor.article.controller.backend;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.article.domain.Region;
import xyz.bycor.article.service.RegionService;
import xyz.bycor.core.qo.QueryObject;
import xyz.bycor.core.response.R;

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

    public RegionBackendController(RegionService regionService) {
        this.regionService = regionService;
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
}
