package cn.itcast.dao;

import cn.itcast.dto.cond.ContentCond;
import cn.itcast.model.ContentDomain;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Component
public interface ContentDao {
    /**
     * 更新文章
     * @param contentDomain
     * @return
     */
    int updateArticleById(ContentDomain contentDomain);

    /**
     * 添加文章
     */
    int addArticle(ContentDomain contentDomain);
    /**
     * 根据条件获取文章列表
     * @param contentCond
     * @return
     */
    List<ContentDomain> getArticlesByCond(ContentCond contentCond);

    ContentDomain getArticlesById(Integer cid);

    void deleteArticleById(Integer cid);
    /**
     * 根据编号获取文章
     * @param cid
     * @return
     */
    ContentDomain getArticleById(Integer cid);
}
