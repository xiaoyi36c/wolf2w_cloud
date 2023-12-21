package xyz.bycor.search.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.article.dto.DestinationDTO;
import xyz.bycor.article.dto.StrategyDTO;
import xyz.bycor.article.dto.TravelDTO;
import xyz.bycor.article.vo.SearchResult;
import xyz.bycor.core.exception.BusinessException;
import xyz.bycor.core.response.R;
import xyz.bycor.member.dto.UserinfoDTO;
import xyz.bycor.search.feign.ArticleFeignService;
import xyz.bycor.search.feign.UserInfoFeignService;
import xyz.bycor.search.qo.SearchQuery;
import xyz.bycor.search.service.IStrategyEsService;

import java.net.URLDecoder;
import java.util.List;

/**
 * 主页搜索
 *
 * @author BAI
 */

@RestController
@RequestMapping("/q")
@Api(tags = "主页搜索")
@Slf4j
public class SearchController {

    private final UserInfoFeignService userInfoFeignService;

    private final ArticleFeignService articleFeignService;

    private final IStrategyEsService strategyEsService;

    public SearchController(UserInfoFeignService userInfoFeignService, ArticleFeignService articleFeignService, IStrategyEsService strategyEsService) {
        this.userInfoFeignService = userInfoFeignService;
        this.articleFeignService = articleFeignService;
        this.strategyEsService = strategyEsService;
    }


    @GetMapping()
    @ApiOperation("主页搜索")
    public R<?> search(SearchQuery qo) {
        JSONObject ret = new JSONObject();
        try {
            if (!qo.getKeyword().isEmpty()) {
                qo.setKeyword(URLDecoder.decode(qo.getKeyword(), "UTF-8"));
            }
            log.info("[接收到搜索请求] - {}", qo);

            switch (qo.getType()) {
                case SearchQuery.TYPE_DEST:
                    ret = this.searchForDest(qo);
                    break;
                case SearchQuery.TYPE_STRATEGY:
                    ret = this.searchStrategy(qo);
                    break;
                case SearchQuery.TYPE_TRAVEL:
                    ret = this.searchTravel(qo);
                    break;
                case SearchQuery.TYPE_USER:
                    ret = this.searchForUser(qo);
                    break;
                default:
                    ret = this.searchAll(qo);
                    break;
            }
            if (ret != null) {
                ret.put("qo", qo);
            }
        } catch (Exception e) {
            throw new BusinessException("搜索失败");
        }
        return R.ok(ret);
    }

    private JSONObject searchTravel(SearchQuery qo) {
        return null;
    }

    private JSONObject searchAll(SearchQuery qo) {
        return null;
    }

    private JSONObject searchForUser(SearchQuery qo) {
        return null;
    }

    private JSONObject searchStrategy(SearchQuery qo) {
        Page<StrategyDTO> page = strategyEsService.queryWithHighlight(qo.getKeyword(), qo.getCurrent(), qo.getSize());
        JSONObject json = new JSONObject();
        json.put("page", page);
        return json;
    }

    private JSONObject searchForDest(SearchQuery qo) {
        log.info("[搜索目的地] - {}", qo.getKeyword());
        JSONObject rest = new JSONObject();
        SearchResult searchResult = new SearchResult();
        DestinationDTO dest = articleFeignService.getDestByName(qo.getKeyword());
        if (dest != null) {
            // 查询游记
            List<TravelDTO> travels = articleFeignService.findTravelsByDestId(dest.getId());
            searchResult.setTravels(travels);
            // 查询攻略
            List<StrategyDTO> strategies = articleFeignService.findStrategiesByDestId(dest.getId());
            searchResult.setStrategies(strategies);
            // 查询用户
            List<UserinfoDTO> users = userInfoFeignService.findUserByDestName(dest.getName());
            searchResult.setUsers(users);
            searchResult.setTotal(searchResult.getTotal()+1);
        }
        rest.put("dest", dest);
        rest.put("result", searchResult);
        return rest;
    }

}
