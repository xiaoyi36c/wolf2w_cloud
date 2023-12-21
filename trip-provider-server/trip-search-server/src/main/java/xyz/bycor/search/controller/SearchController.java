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
import xyz.bycor.search.domain.DestinationEs;
import xyz.bycor.search.domain.StrategyEs;
import xyz.bycor.search.domain.TravelEs;
import xyz.bycor.search.domain.UserInfoEs;
import xyz.bycor.search.feign.ArticleFeignService;
import xyz.bycor.search.feign.UserInfoFeignService;
import xyz.bycor.search.qo.SearchQuery;
import xyz.bycor.search.service.DestinationESService;
import xyz.bycor.search.service.StrategyESService;
import xyz.bycor.search.service.TravelESService;
import xyz.bycor.search.service.UserInfoESService;
import xyz.bycor.search.service.impl.StrategyEsServiceImpl;
import xyz.bycor.search.service.impl.TravelEsServiceImpl;
import xyz.bycor.search.service.impl.UserInfoESServiceImpl;

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


    private final StrategyESService strategyEsService;

    private final TravelESService travelESService;

    private final UserInfoESService userInfoESService;

    private final DestinationESService destinationESService;



    public SearchController(UserInfoFeignService userInfoFeignService, ArticleFeignService articleFeignService, StrategyEsServiceImpl strategyEsService, TravelEsServiceImpl travelESEsService, UserInfoESServiceImpl userInfoESService, DestinationESService destinationESService) {
        this.userInfoFeignService = userInfoFeignService;
        this.articleFeignService = articleFeignService;
        this.strategyEsService = strategyEsService;
        this.travelESService = travelESEsService;
        this.userInfoESService = userInfoESService;
        this.destinationESService = destinationESService;
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
            ret.put("qo", qo);
        } catch (Exception e) {
            throw new BusinessException("搜索失败"+ e);
        }
        return R.ok(ret);
    }

    /**
     *  根据关键字搜索游记
     * @param qo 分页查询对象
     * @return 封装高亮的游记对象
     */
    private JSONObject searchTravel(SearchQuery qo) {
        Page<TravelDTO> page = travelESService.queryWithHighlight(TravelEs.class,qo,
                articleFeignService::getTravelById,
                "title", "summary"
        );
        JSONObject json =new JSONObject();
        json.put("page" ,page);
        return json;
    }

    private JSONObject searchAll(SearchQuery qo) {
        SearchResult searchResult = new SearchResult();

        // 查询目的地
        Page<DestinationDTO> destPage = destinationESService.queryWithHighlight(DestinationEs.class,qo,
                articleFeignService::getDestById,
                "info","name"
        );

        // 查询攻略
        Page<StrategyDTO> strategyPage = strategyEsService.queryWithHighlight(StrategyEs.class,qo,
                articleFeignService::getStrategyById,
                "title", "subTitle", "summary"
        );

        // 查询游记
        Page<TravelDTO> travelPage = travelESService.queryWithHighlight(TravelEs.class,qo,
                articleFeignService::getTravelById,
                "title", "summary"
        );

        // 查询用户
        Page<UserinfoDTO> userPage = userInfoESService.queryWithHighlight(UserInfoEs.class,qo,
                userInfoFeignService::findById,
                "info","city"
        );

        // 设置总数,并封装到searchResult中
        searchResult.setTotal(destPage.getTotalElements() + strategyPage.getTotalElements() + travelPage.getTotalElements() + userPage.getTotalElements());
        searchResult.setDests(destPage.getContent());
        searchResult.setStrategies(strategyPage.getContent());
        searchResult.setTravels(travelPage.getContent());
        searchResult.setUsers(userPage.getContent());


        // 封装json 数据
        JSONObject json =new JSONObject();
        json.put("result" ,searchResult);
        return json;
    }

    /**
     *  根据关键字搜索用户
     * @param qo 分页查询对象
     * @return 封装高亮的用户对象
     */
    private JSONObject searchForUser(SearchQuery qo) {
        Page<UserinfoDTO> page = userInfoESService.queryWithHighlight(UserInfoEs.class,qo,
                userInfoFeignService::findById,
                "info","city"
        );
        JSONObject json =new JSONObject();
        json.put("page" ,page);
        return json;
    }


    /**
     *  根据关键字搜索攻略
     * @param qo 分页查询对象
     * @return 封装高亮的攻略对象
     */
    private JSONObject searchStrategy(SearchQuery qo) {
        Page<StrategyDTO> page = strategyEsService.queryWithHighlight(StrategyEs.class,qo,
                articleFeignService::getStrategyById,
                "title", "subTitle", "summary"
                );
        JSONObject json = new JSONObject();
        json.put("page", page);
        return json;
    }


    /**
     *  根据关键字搜索目的地及其相关信息,精确匹配
     * @param qo 分页查询对象
     * @return 目的地及其相关信息
     */
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
