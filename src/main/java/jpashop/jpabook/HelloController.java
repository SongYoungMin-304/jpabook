package jpashop.jpabook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data","hello!!!");
        return "hello";
    }

    @RequestMapping("/hello2")
    public String hello2(Model model){
        model.addAttribute("data","hello!!!");
        return "hello";
    }
}
