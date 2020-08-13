package cn.itcast.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

@RequestMapping("/toLogin")
public String tologin()
{
    return "admin/login";
}
}
