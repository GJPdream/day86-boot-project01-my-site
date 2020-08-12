package cn.itcast.controller;

import cn.itcast.constant.ErrorContant;
import cn.itcast.constant.Types;
import cn.itcast.constant.WebConst;
import cn.itcast.exception.BusinessException;
import cn.itcast.model.CommentDomain;
import cn.itcast.model.ContentDomain;
import cn.itcast.service.comment.CommentService;
import cn.itcast.service.content.ContentService;
import cn.itcast.utils.*;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文章的主页面
 */
@Controller
public class HomeController {
    @Autowired
    ContentService contentService;
    @Autowired
    CommentService commentService;
    protected MapCache cache = MapCache.single();
    @GetMapping(value = "/blog/article/{cid}")
    public String post(
            @PathVariable("cid")
            Integer cid,
            HttpServletRequest request
    ){
        ContentDomain atricle = contentService.getArticlesById(cid);
        request.setAttribute("article",atricle);
        //文章的点击量
        this.updateArticleHit(atricle.getCid(),atricle.getHits());
        List<CommentDomain> commentsPaginator = commentService.getCommentsByCId(cid);
        request.setAttribute("comments", commentsPaginator);
        request.setAttribute("active","blog");
        return "site/blog-details";
    }

    /**
     * 更新文章的点击率
     *
     * @param cid
     * @param chits
     */
    private void updateArticleHit(Integer cid, Integer chits) {
        Integer hits = cache.hget("article", "hits");
        if (chits == null) {
            chits = 0;
        }
        hits = null == hits ? 1 : hits + 1;
        if (hits >= WebConst.HIT_EXCEED) {
            ContentDomain temp = new ContentDomain();
            temp.setCid(cid);
            temp.setHits(chits + hits);
            contentService.updateContentByCid(temp);
            cache.hset("article", "hits", 1);
        } else {
            cache.hset("article", "hits", hits);
        }
    }


    /**
     * 评论提交的controller
     */
    @PostMapping(value ="/blog/comment")
    @ResponseBody
    public APIResponse comment(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(name = "cid", required = true) Integer cid,
            @RequestParam(name = "coid", required = false) Integer coid,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "mail", required = false) String mail,
            @RequestParam(name = "url", required = false) String url,
            @RequestParam(name = "text", required = true) String text,
            @RequestParam(name = "_csrf_token", required = true) String _csrf_token){

        /**
         * 这个就是得到请求头的里面的值就是"Referer"
         * 返回来一个就是连接地址
         * http://localhost:8888/blog/article/68
         */
        String ref = request.getHeader("Referer");
     /*   if (StringUtils.isBlank(ref) || StringUtils.isBlank(_csrf_token)) {
            return APIResponse.fail("访问失败");
        }*/

        /*String token = cache.hget(Types.CSRF_TOKEN.getType(), _csrf_token);
        System.out.println(token);*/
      /*  if (StringUtils.isBlank(token)) {
            return APIResponse.fail("访问失败");
        }*/

        if (null == cid || StringUtils.isBlank(text)) {
            return APIResponse.fail("请输入完整后评论");
        }

        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return APIResponse.fail("姓名过长");
        }

        if (StringUtils.isNotBlank(mail) && !TaleUtils.isEmail(mail)) {
            return APIResponse.fail("请输入正确的邮箱格式");
        }

        if (StringUtils.isNotBlank(url) && !PatternKit.isURL(url)) {
            return APIResponse.fail("请输入正确的URL格式");
        }

        if (text.length() > 200) {
            return APIResponse.fail("请输入200个字符以内的评论");
        }

        String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return APIResponse.fail("您发表评论太快了，请过会再试");
        }

        author = TaleUtils.cleanXSS(author);
        text = TaleUtils.cleanXSS(text);

        author = EmojiParser.parseToAliases(author);
        text = EmojiParser.parseToAliases(text);

        CommentDomain comments = new CommentDomain();
        comments.setAuthor(author);
        comments.setCid(cid);
        comments.setIp(request.getRemoteAddr());
        comments.setUrl(url);
        comments.setContent(text);
        comments.setMail(mail);
        comments.setParent(coid);
        try {
            commentService.addComment(comments);
            cookie("tale_remember_author", URLEncoder.encode(author, "UTF-8"), 7 * 24 * 60 * 60, response);
            cookie("tale_remember_mail", URLEncoder.encode(mail, "UTF-8"), 7 * 24 * 60 * 60, response);
            if (StringUtils.isNotBlank(url)) {
                cookie("tale_remember_url", URLEncoder.encode(url, "UTF-8"), 7 * 24 * 60 * 60, response);
            }
            // 设置对每个文章1分钟可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY.getType(), val, 1, 60);

            return APIResponse.success();
        } catch (Exception e) {
            throw BusinessException.withErrorCode(ErrorContant.Comment.ADD_NEW_COMMENT_FAIL);
        }
    }

    /**
     * 设置cookie
     *
     * @param name
     * @param value
     * @param maxAge
     * @param response
     */
    private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }
}
