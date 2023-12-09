package xyz.bycor.article.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.bycor.article.domain.StrategyCatalog;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatalogGroup {
    private String destName;
    private List<StrategyCatalog> catalogs =new ArrayList<>();

}
