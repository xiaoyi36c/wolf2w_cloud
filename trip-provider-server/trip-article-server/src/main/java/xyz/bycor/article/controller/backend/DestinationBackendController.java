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
import xyz.bycor.article.domain.Destination;
import xyz.bycor.article.domain.Region;
import xyz.bycor.article.service.DestinationService;
import xyz.bycor.core.qo.QueryObject;
import xyz.bycor.core.response.R;

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
     * 分页查询所有区域
     * @return
     */
    @ApiOperation(value = "分页查询所有区域")
    @GetMapping()
    public R<Page<Destination>> listPage(QueryObject qo) {
        log.info("查询所有目地址");
        return R.ok(destinationService.findPage(qo));
    }
}
