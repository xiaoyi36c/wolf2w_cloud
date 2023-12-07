package xyz.bycor.article.controller.frontend;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.article.domain.Region;
import xyz.bycor.article.service.DestinationService;
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
@RequestMapping("/regions")
@Api(tags = "区域信息接口")
@Slf4j
@Validated
@RefreshScope
public class RegionController {

    private final RegionService regionService;

    private final DestinationService destinationService;


    public RegionController(RegionService regionService, DestinationService destinationService) {
        this.regionService = regionService;
        this.destinationService = destinationService;
    }

    @ApiOperation(value = "查询热门区域")
    @GetMapping("/hotList")
    public R<List<Region>> hotList() {
        log.info("查询热门区域");
        return R.ok(regionService.findHotList());
    }
}
