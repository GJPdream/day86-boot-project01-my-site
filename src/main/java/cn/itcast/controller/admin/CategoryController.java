package cn.itcast.controller.admin;

import cn.itcast.constant.ErrorContant;
import cn.itcast.constant.Types;
import cn.itcast.constant.WebConst;
import cn.itcast.dto.cond.MetaDto;
import cn.itcast.service.MetaService;
import cn.itcast.utils.APIResponse;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
@Autowired
private MetaService metaService;


    @GetMapping(value = "")
    public String index(
            HttpServletRequest request
    )
    {
        List<MetaDto> metaDtos= metaService.getMetaList(Types.CATEGORY.getType(),null, WebConst.MAX_POSTS);
        List<MetaDto> tags = metaService.getMetaList(Types.TAG.getType(), null, WebConst.MAX_POSTS);
         request.setAttribute("categories",metaDtos);
         request.setAttribute("tags",tags);
         return "admin/category";
    }
    @PostMapping(value = "save")
    @ResponseBody
    public APIResponse save(
           @RequestParam(name = "cname",required = true)
            String cname,
            @RequestParam(name = "mid",required = true)
            Integer mid)
    {
        try {
            metaService.saveMeta(Types.CATEGORY.getType(),cname,mid);
        } catch (Exception e) {
            e.printStackTrace();
          return APIResponse.fail(ErrorContant.Error.Classification_ERROR);
        }

        return APIResponse.success();
        }

        @PostMapping("delete")
        @ResponseBody
    public APIResponse delete(
            @RequestParam(name = "mid",required = true)
            Integer mid
        )
        {
            try {
                metaService.deleteMetaById(mid);
            } catch (Exception e) {
                e.printStackTrace();
                return APIResponse.fail(ErrorContant.Error.DELETE_ERROR);
            }
            return APIResponse.success();
        }
}
