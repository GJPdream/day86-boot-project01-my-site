package cn.itcast.service.comment.Impl;

import cn.itcast.constant.ErrorContant;
import cn.itcast.dao.CommentDao;
import cn.itcast.dao.ContentDao;
import cn.itcast.dto.cond.CommentCond;
import cn.itcast.exception.BusinessException;
import cn.itcast.model.CommentDomain;
import cn.itcast.model.ContentDomain;
import cn.itcast.service.content.ContentService;
import cn.itcast.utils.DateKit;
import cn.itcast.utils.TaleUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommentService implements cn.itcast.service.comment.CommentService {
    @Autowired
    CommentDao commentDao;
    @Autowired
    private ContentService contentService;

    private static final Map<String,String> STATUS_MAP = new ConcurrentHashMap<>();
    /**
     * 评论状态：正常
     */
    private static final String STATUS_NORMAL = "approved";
    /**
     * 评论状态：不显示
     */
    private static final String STATUS_BLANK = "not_audit";

    static {
        STATUS_MAP.put("approved",STATUS_NORMAL);
        STATUS_MAP.put("not_audit",STATUS_BLANK);
    }
    @Override
    public List<CommentDomain> getCommentsByCId(Integer cid) {
       if (cid==null)
           throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);

        return commentDao.getCommentsByCId(cid);
    }

    @Override
    public void addComment(CommentDomain comments) {
        String msg = null;
        if (null == comments) {
            msg = "评论对象为空";
        }
        if(comments != null) {
            if (StringUtils.isBlank(comments.getAuthor())) {
                comments.setAuthor("陌生人");
            }
            if (StringUtils.isNotBlank(comments.getMail()) && !TaleUtils.isEmail(comments.getMail())) {
                msg = "请输入正确的邮箱格式";
            }
            if (StringUtils.isBlank(comments.getContent())) {
                msg = "评论内容不能为空";
            }
            if (comments.getContent().length() < 5 || comments.getContent().length() > 2000) {
                msg = "评论字数在5-2000个字符";
            }
            if (null == comments.getCid()) {
                msg = "评论文章不能为空";
            }
            if (msg != null)
                throw BusinessException.withErrorCode(msg);
            ContentDomain article = contentService.getArticleById(comments.getCid());
            if (null == article)
                throw BusinessException.withErrorCode("该文章不存在");
            comments.setOwnerId(article.getAuthorId());
            comments.setStatus(STATUS_MAP.get(STATUS_BLANK));
          /*  System.out.println("123456"+STATUS_MAP.get(STATUS_BLANK));*/
            /**
             * 获取时间
             */
            comments.setCreated(DateKit.getCurrentUnixTime());
            commentDao.addComment(comments);

            ContentDomain temp = new ContentDomain();
            temp.setCid(article.getCid());
            Integer count = article.getCommentsNum();
            if (null == count) {
                count = 0;
            }
            temp.setCommentsNum(count + 1);
            contentService.updateContentByCid(temp);
        }

    }

    @Override
    public PageInfo<CommentDomain> getCommentsByCond(CommentCond commentCond, Integer page, Integer limit) {
        if (commentCond==null)
            throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
         PageHelper.startPage(page, limit);
        List<CommentDomain> comments= commentDao.getCommentsByCond(commentCond);
        PageInfo<CommentDomain> pageInfo = new PageInfo<>(comments);
        return pageInfo;
    }

    @Override
    public void updateCommentStatus(Integer coid, String status) {
      if (coid==null)
          throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
      commentDao.updateCommentStatus(coid,status);
    }

    @Override
    public void deleteCommment(Integer coid) {
       if (coid==null)
           throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
        commentDao.deleteComment(coid);
    }
}
