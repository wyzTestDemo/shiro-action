package im.zhaojun.mapper;

import im.zhaojun.model.UploadMusic;
import im.zhaojun.model.dynamic.BatchUpload;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface UploadMusicMapper {
    @Results({
            @Result(property = "user",column = "userId",one = @One(select = "im.zhaojun.mapper.UserMapper.selectOneByUserId"))
    })
    @Select("select * from uploadMusic")
    public List<UploadMusic> getUploadMusics();
    @Results({
            @Result(property = "user",column = "userId",one = @One(select = "im.zhaojun.mapper.UserMapper.selectOneByUserId"))
    })
    @Select("select * from uploadMusic where id=#{id}")
    UploadMusic selectByPrimaryKey(String id);
    @UpdateProvider(type = BatchUpload.class,method ="dynamicUploadMusicUpd" )
    void updateByPrimaryKey(UploadMusic uploadMusic);
}
