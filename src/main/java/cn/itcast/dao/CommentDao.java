package cn.itcast.dao;

import cn.itcast.dto.cond.CommentCond;
import cn.itcast.model.CommentDomain;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CommentDao {

    List<CommentDomain> getCommentsByCId(Integer cid);
    /**
     * 新增评论
     * @param commentDomain
     * @return
     */
    void addComment(CommentDomain comments);

    List<CommentDomain> getCommentsByCond(CommentCond commentDomain);

    void updateCommentStatus(Integer coid, String status);

    /**
     * 删除评论
     * @param coid
     */
    void deleteComment(Integer coid);
}
