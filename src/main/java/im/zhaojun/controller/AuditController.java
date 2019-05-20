package im.zhaojun.controller;

import com.github.pagehelper.PageInfo;
import im.zhaojun.annotation.OperationLog;
import im.zhaojun.model.Role;
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
@RequestMapping("/audit")
public class AuditController {
    @Resource
    private UploadMusicService uploadMusicService;
    @GetMapping("/getAllMusic")
    @ResponseBody
    public PageResultBean<UploadMusic> getAllMusic(@RequestParam(value = "page", defaultValue = "1") int page,
                                            @RequestParam(value = "limit", defaultValue = "10")int limit){
        List<UploadMusic> allMusics = uploadMusicService.getAllMusics(page, limit);
        PageInfo<UploadMusic> rolePageInfo = new PageInfo<>(allMusics);
        return new PageResultBean<UploadMusic>(rolePageInfo.getTotal(), rolePageInfo.getList());
    }
    @GetMapping("/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        UploadMusic uploadMusic = uploadMusicService.selectOne(id);
        model.addAttribute("uploadMusic", uploadMusic);
        return "audit/music-add";
    }
    @OperationLog("修改音乐信息")
    @PutMapping
    @ResponseBody
    public ResultBean update(UploadMusic uploadMusic) {
        uploadMusicService.update(uploadMusic);
        return ResultBean.success();
    }

    @GetMapping("/index")
    public String index() {
        return "audit/music-list";
    }



    @GetMapping
    public String add() {
        return "dept/dept-add";
    }


}
