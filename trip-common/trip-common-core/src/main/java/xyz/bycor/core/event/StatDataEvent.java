package xyz.bycor.core.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

public class StatDataEvent extends ApplicationEvent {

    @Getter
    @ToString
    public static class Data{
        private final Long id;
        private final String field;
        private final Integer value;


        private Data(Long id, String field, Integer value) {
            this.id = id;
            this.field = field;
            this.value = value;
        }

        public static DataBuilder builder(Long id){
            return new DataBuilder(id);
        }

        public static class DataBuilder{
            private final Long id;
            private String field;
            private Integer value;

            public DataBuilder(Long id){
                this.id = id;
            }
            public DataBuilder field(String field){
                this.field = field;
                return this;
            }
            public DataBuilder value(Integer value){
                this.value = value;
                return this;
            }
            public Data build(){
                return new Data(this.id,this.field,this.value);
            }
        }


    }
    public StatDataEvent(Data source) {
        super(source);
    }


}
