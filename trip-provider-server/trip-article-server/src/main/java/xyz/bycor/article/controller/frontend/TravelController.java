package xyz.bycor.article.controller.frontend;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.bycor.article.domain.Travel;
import xyz.bycor.article.qo.TravelQuery;
import xyz.bycor.article.service.TravelService;
import xyz.bycor.article.vo.TravelRequest;
import xyz.bycor.auth.anno.RequiredLogin;
import xyz.bycor.core.response.R;
import xyz.bycor.core.utils.OssUtil;

import java.util.List;
import java.util.UUID;

@Api(tags = "游记详细接口")
@RestController
@RequestMapping("/travels")
@Slf4j
public class TravelController {
    private final TravelService travelService;


    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    /**
     * 目的地下的游记内容 分页条件查询
     */
    @GetMapping()
    public R<Page<Travel>> findTravelByDestId(TravelQuery travelQuery) {
        log.info("目的地下的游记内容 分页条件查询");
        return R.ok(travelService.findTravelByDestId(travelQuery));
    }


    @ApiOperation(value = "游记Top3")
    @GetMapping("/viewnnumTop3")
    public R<List<Travel>> viewnumTop3(Long destId) {
        log.info("游记Top3");
        return R.ok(travelService.findTravelTop3(destId));
    }

    @ApiOperation(value = "游记明细")
    @GetMapping("/{id}")
    public R<Travel> findById(@PathVariable Long id) {
        log.info("游记明细");
        return R.ok(travelService.findById(id));
    }


    @ApiOperation(value = "游记明细 图片上传")
    @PostMapping("/coverImageUpload")
    @RequiredLogin // 必须登录
    public JSONObject coverImageUpload(@RequestParam("upfile") MultipartFile file) {
        log.info("游记明细 图片上传");
        // 生成 UUID  12位
        String url = null;
        JSONObject json = new JSONObject();
        try {
            String fileMainName = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
            url = OssUtil.upload("travels/images", fileMainName, file);
            //是否上传成功
            json.put("state", "SUCCESS");
            //现在文件名称
            json.put("title", fileMainName);
            //文件原名称
            json.put("original", file.getOriginalFilename());
            //文件类型 .+后缀名
            json.put("type", ".jpg");
            //文件路径
            json.put("url", "https://"+url);
            //文件大小（字节数）
            json.put("size", file.getSize());
        } catch (Exception e) {
            json.put("state", "ERROR");
            log.error("上传失败", e);
        }
        return json;

    }


    @ApiOperation(value = "游记明细新增或修改")
    @PostMapping("/saveOrUpdate")
    public R saveOrUpdate(TravelRequest travel) {
        log.info("游记明细新增或修改");
        Long id = travelService.saveOrUpdateByTravelRequest(travel);
        return R.ok(id);
    }

}