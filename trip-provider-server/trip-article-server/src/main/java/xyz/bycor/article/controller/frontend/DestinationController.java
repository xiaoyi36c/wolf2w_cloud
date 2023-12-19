package xyz.bycor.article.controller.frontend;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.article.domain.Destination;
import xyz.bycor.article.service.DestinationService;
import xyz.bycor.core.response.R;

import java.util.List;

/**
 * @author baishuai
 * @data: 2023-11-27 18:53:23
 * @Description: 针对表【destination】的数据库操作Controller
 *
 */

@RestController
@RequestMapping("/destinations")
@Api(tags = "目地址接口")
@Slf4j
@Validated
@RefreshScope
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }
    @ApiOperation(value = "查询热点目的地")
    @GetMapping("/hotList/{rid}")
    public R<List<Destination>> hotList(@PathVariable Long rid) {
        log.info("查询热点目的地");
        return R.ok(destinationService.findHotList(rid));
    }

    /**
     * 吐司,面包屑
     * @return
     */
    @ApiOperation(value = "吐司,面包屑")
    @GetMapping("/toasts")
    public R<List<Destination>> toasts(Long destId) {
        log.info("吐司,面包屑");
        return R.ok(destinationService.findToastList(destId));
    }

    @ApiOperation(value = "根据关键字查询目的地")
    @GetMapping("/getDestByName")
    public Destination getDestByName(String destName) {
        log.info("根据关键字查询目的地");
        return destinationService.getDestByName(destName);
    }



}
