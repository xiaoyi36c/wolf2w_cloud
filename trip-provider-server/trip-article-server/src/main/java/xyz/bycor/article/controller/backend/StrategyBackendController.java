package xyz.bycor.article.controller.backend;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.bycor.article.domain.Strategy;
import xyz.bycor.article.service.StrategyService;
import xyz.bycor.core.qo.QueryObject;
import xyz.bycor.core.response.R;
import xyz.bycor.core.utils.OssUtil;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author baishuai
 * @data: 2023-11-27 18:53:23
 * @Description: 针对表【strategy】的数据库操作Controller
 *
 */

@RestController
@RequestMapping("/backend/strategies")
@Api(tags = "攻略详细接口")
@Slf4j
@Validated
@RefreshScope
public class StrategyBackendController {

    private final StrategyService strategyService;

    public StrategyBackendController(StrategyService strategyService) {
        this.strategyService = strategyService;
    }

    /**
     * 分页查询所有攻略详细
     * @return 分页对象
     */
    @ApiOperation(value = "查询所有攻略详细")
    @GetMapping("/list")
    public R<List<Strategy>> list() {
        log.info("查询所有攻略详细");
        return R.ok(strategyService.list());
    }
    

    /**
     * 分页查询所有攻略详细
     * @return
     */
    @ApiOperation(value = "分页查询所有攻略详细")
    @GetMapping()
    public R<Page<Strategy>> listPage(QueryObject qo) {
        log.info("查询所有攻略详细");
        return R.ok(strategyService.findPage(qo));
    }

    @GetMapping("/{id}")
    public R<Strategy> get(@PathVariable @NotNull(message = "id 不能为空") Long id) {
        log.info("查询单个攻略详细");
        return R.ok(strategyService.getById(id));
    }


    /**
     * 新增攻略详细
     * @return
     */
    @ApiOperation(value = "新增攻略详细")
    @PostMapping()
    public R<?> add(Strategy strategy) {
        log.info("新增攻略详细");
        strategyService.save(strategy);
        return R.ok();
    }
    /**
     * 新增攻略详细
     * @return
     */
    @ApiOperation(value = "修改攻略详细")
    @PutMapping()
    public R<?> save(Strategy strategy) {
        log.info("修改攻略详细");
        strategyService.updateById(strategy);
        return R.ok();
    }

    /**
     * 删除攻略详细
     * @return
     */
    @ApiOperation(value = "删除攻略详细")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable @NotNull(message = "id 不能为空") Long id) {
        log.info("删除攻略详细");
        strategyService.removeById(id);
        return R.ok();
    }

    /**
     *  上传攻略详细图片
     * @return
     */
    @ApiOperation(value = "上传攻略详细图片")
    @PostMapping("/uploadImg")
    public JSONObject uploadImg(MultipartFile upload) {
        log.info("上传攻略详细图片");
        // 生成 UUID  12位
        String url = null;
        JSONObject re = new JSONObject();
        try {
            String fileMainName = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
            url = OssUtil.upload("images", fileMainName, upload);
            re.put("url", url);
            re.put("uploaded",1);
            re.put("fileName",fileMainName + ".jpg");
        } catch (Exception e) {
            re.put("uploaded",0);
            JSONObject error = new JSONObject();
            error.put("message",e.getMessage());
            re.put("error",error);
        }
        return re;
    }


}
