package cn.itcast.controller;

import cn.itcast.constant.Types;
import cn.itcast.dto.cond.ContentCond;
import cn.itcast.dto.cond.MetaCond;
import cn.itcast.model.ContentDomain;
import cn.itcast.model.MetaDomain;
import cn.itcast.service.MetaService;
import cn.itcast.service.content.ContentService;
import cn.itcast.utils.APIResponse;
import cn.itcast.utils.TaleUtils;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/article")
public class ArticalController {
    @Autowired
    MetaService metaService;
    @Autowired
    ContentService contentService;

/**
 * 编辑文章
 */

@GetMapping("/{cid}")
public String edit(
        @PathVariable(value = "cid") Integer cid,
        HttpServletRequest request
         )
{
    System.out.println(cid);
    ContentDomain content=  contentService.getArticlesById(cid);
    /*System.out.println(content.getContent());*/
    request.setAttribute("contents",content);
    MetaCond metaCond = new MetaCond();
    metaCond.setType(Types.CATEGORY.getType());
    List<MetaDomain> metas = metaService.getMetas(metaCond);
  /*  System.out.println("==============");
    for (MetaDomain m:metas)
    {
        System.out.println(m.getName());
    }*/

    request.setAttribute("categories",metas);
    request.setAttribute("active","article");
    return "admin/article_edit";
}

    /**
     * 就是进行分页查询，显示list
     * @param request
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "")
    public String index(
            HttpServletRequest request,
            @RequestParam(name = "page", required = false, defaultValue = "1")
            int page,
            @RequestParam(name = "limit", required = false, defaultValue = "8")
            int limit
    )
    {
        PageInfo<ContentDomain> articles = contentService.getArticlesByCond(new ContentCond(), page, limit);
        request.setAttribute("at", articles);
        return "admin/article_list";
    }

    @GetMapping("/publish")
    public String newArticle(HttpServletRequest request)
    {
        MetaCond metaCond = new MetaCond();
        metaCond.setType("category");
        List<MetaDomain> metas = metaService.getMetas(metaCond);
    request.setAttribute("categories",metas);
    return "admin/article_edit";
    }

    @PostMapping("/modify")
    @ResponseBody
    public APIResponse modifyArticle(
            HttpServletRequest request,
            @RequestParam(name ="cid",required =true)
            Integer cid,
            @RequestParam(name = "title", required = true)
            String title,
            @RequestParam(name = "titlePic", required = false)
            String titlePic,
            @RequestParam(name = "slug", required = false)
            String slug,
            @RequestParam(name = "content", required = true)
            String content,
            @RequestParam(name = "type", required = true)
            String type,
            @RequestParam(name = "status", required = true)
            String status,
            @RequestParam(name = "tags", required = false)
            String tags,
            @RequestParam(name = "categories", required = false, defaultValue = "默认分类")
            String categories,
            @RequestParam(name = "allowComment", required = true)
            Boolean allowComment
    ){
       /* content = TaleUtils.cleanXSS(content);
        content = EmojiParser.parseToAliases(content);*/
        /* content = TaleUtils.htmlToText(content);
        System.out.println(content);
        content = TaleUtils.cleanXSS(content);
        content = EmojiParser.parseToAliases(content);*/
        ContentDomain contentDomain = new ContentDomain();
        contentDomain.setCid(cid);
        contentDomain.setTitle(title);
        contentDomain.setTitlePic(titlePic);
        contentDomain.setSlug(slug);
        contentDomain.setContent(content);
        contentDomain.setType(type);
        contentDomain.setStatus(status);
        contentDomain.setTags(tags);
        contentDomain.setCategories(categories);
        contentDomain.setAllowComment(allowComment ? 1 : 0);
       contentService.updateArticleById(contentDomain);
        return APIResponse.success();
    }
    @PostMapping(value = "/publish")
    @ResponseBody
    public APIResponse publishArticle(
            HttpServletRequest request,
            @ApiParam(name = "title", value = "标题", required = true)
            @RequestParam(name = "title", required = true)
                    String title,
            @ApiParam(name = "titlePic", value = "标题图片", required = false)
            @RequestParam(name = "titlePic", required = false)
                    String titlePic,
            @ApiParam(name = "slug", value = "内容缩略名", required = false)
            @RequestParam(name = "slug", required = false)
                    String slug,
            @ApiParam(name = "content", value = "内容", required = true)
            @RequestParam(name = "content", required = true)
                    String content,
            @ApiParam(name = "type", value = "文章类型", required = true)
            @RequestParam(name = "type", required = true)
                    String type,
            @ApiParam(name = "status", value = "文章状态", required = true)
            @RequestParam(name = "status", required = true)
                    String status,
            @ApiParam(name = "tags", value = "标签", required = false)
            @RequestParam(name = "tags", required = false)
                    String tags,
            @ApiParam(name = "categories", value = "分类", required = false)
            @RequestParam(name = "categories", required = false, defaultValue = "默认分类")
                    String categories,
            @ApiParam(name = "allowComment", value = "是否允许评论", required = true)
            @RequestParam(name = "allowComment", required = true)
                    Boolean allowComment
    ){
        ContentDomain contentDomain = new ContentDomain();
        contentDomain.setTitle(title);
        contentDomain.setTitlePic(titlePic);
        contentDomain.setSlug(slug);
        contentDomain.setContent(content);
        contentDomain.setType(type);
        contentDomain.setStatus(status);
        contentDomain.setTags(type.equals(Types.ARTICLE.getType()) ? tags : null);
        //只允许博客文章有分类，防止作品被收入分类
        contentDomain.setCategories(type.equals(Types.ARTICLE.getType()) ? categories : null);
        contentDomain.setAllowComment(allowComment ? 1 : 0);
        contentService.addArticle(contentDomain);
        return APIResponse.success();
    }

    /**
     * 删除操作
     */
    @PostMapping("/delete")
    @ResponseBody
    public APIResponse deleteArticle(
            @RequestParam(value = "cid",required = true)
            Integer cid,
            HttpServletRequest request
    ){
        contentService.deleteArticleById(cid);
        return APIResponse.success();
    }

}
