package xyz.bycor.member.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xyz.bycor.member.domain.Userinfo;

/**
* @author BAI
* @description 针对表【userinfo】的数据库操作Mapper
* @createDate 2023-12-02 15:57:08
* @Entity xyz/bycor/member.domain.Userinfo
*/
public interface UserinfoMapper extends BaseMapper<Userinfo> {

    Long selectFavorite(@Param("sid") Long sid, @Param("userId") Long userId);

    void deleteFavorite(@Param("sid") Long sid, @Param("userId") Long userId);

    void insertFavorite(@Param("sid") Long sid, @Param("userId") Long userId);
}




