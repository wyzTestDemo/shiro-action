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
@RequestMapping("/say")
public class SayController {
    @GetMapping("/index")
    public String say(Model model){
        model.addAttribute("dd","刚进来");
        return "yybUser/say";
    }
    @GetMapping("/sayTest")
    public String sayTest(Model model){
        model.addAttribute("dd","发表了");
        return "yybUser/say";
    }

}
