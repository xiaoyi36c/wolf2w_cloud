package xyz.bycor.article.qo;

import lombok.Getter;
import lombok.Setter;
import xyz.bycor.article.utils.RangParam;
import xyz.bycor.core.qo.QueryObject;

import java.util.HashMap;

@Getter
@Setter
public class TravelQuery extends QueryObject {
    // 月份 1-12
    private static final HashMap<Integer,RangParam> TRAVEL_TIME = new HashMap<>();
    // 人均消费
    private static final HashMap<Integer,RangParam> TRAVEL_CONSUME = new HashMap<>();
    // 出行天数
    private static final HashMap<Integer,RangParam> TRAVEL_DAYS = new HashMap<>();


    static {
        // 月份范围
        TRAVEL_TIME.put(1,new RangParam(1,2));
        TRAVEL_TIME.put(2,new RangParam(3,4));
        TRAVEL_TIME.put(3,new RangParam(5,6));
        TRAVEL_TIME.put(4,new RangParam(7,8));
        TRAVEL_TIME.put(5,new RangParam(9,10));
        TRAVEL_TIME.put(6,new RangParam(11,12));
        // 人均消费范围
        TRAVEL_CONSUME.put(1,new RangParam(0,999));
        TRAVEL_CONSUME.put(2,new RangParam(1000,6000));
        TRAVEL_CONSUME.put(3,new RangParam(6001,20000));
        TRAVEL_CONSUME.put(4,new RangParam(20001,9999999));
        // 出行天数范围
        TRAVEL_DAYS.put(1,new RangParam(1,3));
        TRAVEL_DAYS.put(2,new RangParam(4,7));
        TRAVEL_DAYS.put(3,new RangParam(8,14));
        TRAVEL_DAYS.put(4,new RangParam(15,999));
    }



    private Long destId;
    private String orderBy = "created_time";

    private RangParam travelTime;
    private RangParam travelConsume;
    private RangParam travelDays;


    public void setTravelTimeType(Integer travelTimeType){
        this.travelTime= TRAVEL_TIME.get(travelTimeType);
    }

    public void setConsumeType(Integer consumeType){
        this.travelConsume = TRAVEL_CONSUME.get(consumeType);
    }

    public void setDayType(Integer dayType){
        this.travelDays = TRAVEL_DAYS.get(dayType);
    }
}
