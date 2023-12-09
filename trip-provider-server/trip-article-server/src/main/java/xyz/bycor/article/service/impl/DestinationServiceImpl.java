package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.bycor.article.domain.Destination;
import xyz.bycor.article.domain.Region;
import xyz.bycor.article.mapper.DestinationMapper;
import xyz.bycor.article.qo.DestinationQueryObject;
import xyz.bycor.article.service.DestinationService;
import xyz.bycor.article.service.RegionService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author BAI
 * @description 针对表【destination】的数据库操作Service实现
 * @createDate 2023-12-07 10:53:35
 */
@Service
@SuppressWarnings("all")
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination>
        implements DestinationService {

    private final RegionService regionService;

    public DestinationServiceImpl(RegionService regionService) {
        this.regionService = regionService;
    }

    @Override
    public Page<Destination> findPage(DestinationQueryObject qo) {
        Page<Destination> destinationPage = new Page<>(qo.getCurrent(), qo.getSize());
        QueryWrapper<Destination> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(!qo.getKeyword().isEmpty(), Destination::getName, qo.getKeyword())
                //  如果 parentId != null ,则查询 parentId = parentId
                .isNull(qo.getParentId() == null, Destination::getParentId)
                // 如果 parentId = null ,则查询 parentId = parentId
                .eq(qo.getParentId() != null, Destination::getParentId, qo.getParentId());
        return super.page(destinationPage, wrapper);
    }

    @Override
    public List<Destination> findByRegionId(Long id) {
        // 根据区域id查询区域
        Region region = regionService.getById(id);
        List<Destination> destinations = new ArrayList<>();
        // 根据区域的ids查询目的地
        if (region != null) {
            List<Long> ids = region.parseRefIds();
            for (Long s : ids) {
                Destination destination = super.getById(s);
                destinations.add(destination);
            }
        }
        return destinations;
    }

    @Override
    public List<Destination> findToastList(Long parentId) {
        List<Destination> destinations = new ArrayList<>();
        do {
            Destination destination = super.getById(parentId);
            if (destination == null) {
                break;
            }
            destinations.add(destination);
            parentId = destination.getParentId();
        } while (parentId != null);
        return destinations;
    }

    @Override
    public List<Destination> findHotList(Long rid) {
        List<Destination> parents;
        if (rid == -1) {
            parents = super.list(new QueryWrapper<Destination>().lambda().eq(Destination::getParentId, 1));
        } else {
            parents = this.findByRegionId(rid);
        }
        // 如果没有数据
        if (parents.isEmpty()) {
            return Collections.emptyList();
        }
        // 转成Map<Long key, Destination value>
        Map<Long, Destination> parentMap = parents.stream().collect(
                Collectors.toMap(Destination::getId, destination -> destination));

        //  父级目的地 id
        Set<Long> parentIds = parentMap.keySet();
        // 根据父级目的地id查询子级目的地
        List<Destination> children = super.list(new QueryWrapper<Destination>().lambda()
                .in(Destination::getParentId, parentIds));
        // 将子级目的地放入父级目的地的children属性中
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Destination child : children) {
            // 异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                // 根据子级目的地的parentId查询父级目的地
                Destination parent = parentMap.get(child.getParentId());
                if (parent != null) {
                    Optional<List<Destination>> optional = Optional.ofNullable(parent.getChildren());
                    // 如果父级目的地的children属性为空,则创建一个ArrayList
                    List<Destination> childrenList = Collections.synchronizedList(optional.orElse(new ArrayList<>()));
                    // 将子级目的地放入父级目的地的children属性中
                    childrenList.add(child);
                    // 截断 10 条数据
                    parent.setChildren(childrenList.stream().limit(10).collect(Collectors.toList()));
                }
            });
            futures.add(future);
        }

        // 等待所有子线程执行完毕
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
            voidCompletableFuture.get();
        } catch (Exception ignored) {
        }



        /*for (Destination parent : list) {
            LambdaQueryWrapper<Destination> wrapper = new QueryWrapper<Destination>().lambda()
                    .eq(Destination::getParentId, parent.getId())
                    .last("limit 8");
            List<Destination> children = super.list(wrapper);
            parent.setChildren(children);
        }*/

        // 返回父级目的地
        return parents;
    }
}




