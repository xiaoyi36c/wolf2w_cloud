package xyz.bycor.article.vo;

import lombok.Getter;
import lombok.Setter;
import xyz.bycor.article.dto.StrategyDTO;
import xyz.bycor.article.dto.TravelDTO;
import xyz.bycor.member.dto.UserinfoDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索统一返回结果
 * @author BAI
 */

@Getter
@Setter
public class SearchResult {
    private Long total =0L;

    private List<StrategyDTO> strategies = new ArrayList<>();

    private List<TravelDTO> travels = new ArrayList<>();

    private List<UserinfoDTO> users = new ArrayList<>();


    public Long getTotal() {
        if (this.total >0L) {
         return   this.total;
        }
        return (long) (strategies.size() + travels.size() + users.size());
    }


}
