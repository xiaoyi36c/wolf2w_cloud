package xyz.bycor.search.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.core.exception.BusinessException;
import xyz.bycor.core.response.R;
import xyz.bycor.search.qo.SearchQuery;

import java.net.URLDecoder;

/**
 *  主页搜索
 * @author BAI
 */

@RestController
@RequestMapping("/q")
@Api(tags = "主页搜索")
@Slf4j
public class SearchController {

    @GetMapping("/search")
    @ApiOperation("主页搜索")
    public R<?> search(SearchQuery qo) {
        JSONObject ret =null;
        try {
            if (qo.getKeyword().isEmpty()){
                qo.setKeyword(URLDecoder.decode(qo.getKeyword(),"UTF-8"));
            }
            log.info("[接收到搜索请求] - {}", qo);

            switch (qo.getType()) {
                case 0:
                    ret =this.searchForDest(qo);
                    break;
                case 1:
                    ret = this.searchStrategy(qo);
                    break;
                case 2:
                    ret = this.searchForUser(qo);
                    break;
                case 3:
                    ret = this.searchAll(qo);
                    break;
                default:
                    break;
            }
            ret.put("qo",qo);
        } catch (Exception e) {
            throw new BusinessException("搜索失败");
        }
        return R.ok(ret);
    }

    private JSONObject searchAll(SearchQuery qo) {
        return null;
    }

    private JSONObject searchForUser(SearchQuery qo) {
        return null;
    }

    private JSONObject searchStrategy(SearchQuery qo) {
            return null;
    }

    private JSONObject searchForDest(SearchQuery qo) {
            return null;
    }

}
