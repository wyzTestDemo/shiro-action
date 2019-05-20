package im.zhaojun.service;

import com.github.pagehelper.PageHelper;
import im.zhaojun.mapper.UploadMusicMapper;
import im.zhaojun.model.UploadMusic;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class UploadMusicService {
    @Resource
    private UploadMusicMapper uploadMusicMapper;
    public List<UploadMusic> getAllMusics(int page,int limit){
        PageHelper.startPage(page, limit);
       return uploadMusicMapper.getUploadMusics();
    }

    public UploadMusic selectOne(String id) {
        return uploadMusicMapper.selectByPrimaryKey(id);
    }

    public void update(UploadMusic uploadMusic) {
        uploadMusicMapper.updateByPrimaryKey(uploadMusic);
    }
}
