package xyz.bycor.core.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

public class StatDataEvent extends ApplicationEvent {

    @Getter
    @Setter
    public static class Data{
        private Long id;
        private String field;
        private Integer value;
    }



    public StatDataEvent(Object source) {
        super(source);
    }
}
