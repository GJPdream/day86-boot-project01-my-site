package cn.itcast.controller.admin;


import cn.itcast.dto.cond.CommentCond;

import cn.itcast.model.CommentDomain;
import cn.itcast.service.comment.CommentService;
import cn.itcast.utils.APIResponse;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/comments")
public class CommentController {

    @Autowired
    CommentService commentService;
@GetMapping(value = "")
    public String index(
@RequestParam(name = "page",required = false,defaultValue = "1")
Integer page,
@RequestParam(name = "limit",required = false,defaultValue = "8")
Integer limit,
HttpServletRequest request
)
{
    PageInfo<CommentDomain> comments= commentService.getCommentsByCond(new CommentCond(),page,limit);
    request.setAttribute("comments",comments);
    return "admin/comment_list";
}

/**
 * 更改状态的地方
 */
@PostMapping(value = "/status")
@ResponseBody
public APIResponse changeStatus(
        @RequestParam(name = "coid",required = true)
        Integer coid,
        @RequestParam(name = "status",required = true)
        String status
)
{
    List<CommentDomain> comments = commentService.getCommentsByCId(coid);
    if (comments!=null){
        commentService.updateCommentStatus(coid, status);
    }
    else {
        return APIResponse.fail("没有此信息,不能评论");
    }
    return APIResponse.success();
}

@PostMapping(value ={"/delete"})
@ResponseBody
public APIResponse delete(
        @RequestParam(name = "coid",required = true)
        Integer coid
)
{
    List<CommentDomain> commentsByCId = commentService.getCommentsByCId(coid);
    if (commentsByCId!=null){
    commentService.deleteCommment(coid);
    return APIResponse.success();
    }
    return APIResponse.fail("没有此评论，不能被删除；刷新后重试");
}
}
