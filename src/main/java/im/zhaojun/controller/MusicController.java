package im.zhaojun.controller;

import com.github.pagehelper.PageInfo;
import im.zhaojun.annotation.OperationLog;
import im.zhaojun.model.UploadMusic;
import im.zhaojun.service.UploadMusicService;
import im.zhaojun.util.PageResultBean;
import im.zhaojun.util.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/music")
public class MusicController {
    @Resource
    private UploadMusicService uploadMusicService;
    @GetMapping("/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        UploadMusic uploadMusic = uploadMusicService.selectOne(id);
        model.addAttribute("uploadMusic", uploadMusic);
        return "audit/music-play";
    }
}
