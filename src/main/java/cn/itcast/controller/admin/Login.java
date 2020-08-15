package cn.itcast.controller.admin;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Login {
@RequestMapping("/success/login")
public String  Login()
{
    return "redirect:/admin/article/publish";
}
}
