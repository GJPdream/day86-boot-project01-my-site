package cn.itcast.service.comment;

import cn.itcast.dto.cond.CommentCond;
import cn.itcast.model.CommentDomain;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    /**
     * 根据文章编号获取评论列表--只显示通过审核的评论-正常状态的
     * @param cid 文章主键编号
     * @return
     */
    List<CommentDomain> getCommentsByCId(Integer cid);
    /**
     * 新增评论
     * @param commentDomain 评论的实体
     * @return
     */
    void addComment(CommentDomain comments);

    /**
     * 评论获取列表
     * @param commentDomain
     * @param page
     * @param limit
     * @return
     */
    PageInfo<CommentDomain> getCommentsByCond(CommentCond commentDomain, Integer page, Integer limit);
    /**
     * 更新评论的状态
     * @param coid 评论的主键编号
     * @param status 状态
     * @return
     */
    void updateCommentStatus(Integer coid, String status);

    /**
     * 删除评论操作
     * @param coid
     */
    void deleteCommment(Integer coid);
}
