package cn.itcast.service.content;

import cn.itcast.dto.cond.ContentCond;
import cn.itcast.model.ContentDomain;
import com.github.pagehelper.PageInfo;

public interface ContentService {
   void updateArticleById(ContentDomain contentDomain);
    /**
     * 添加文章
     * @param contentDomain
     * @return
     */
    void addArticle(ContentDomain contentDomain);
    /**
     * 根据条件获取文章列表
     * @param contentCond
     * @return
     */
    PageInfo<ContentDomain> getArticlesByCond(ContentCond contentCond, int page, int limit);



    /**
     * 通过cid来获取文章的。
     * @param cid
     * @return
     */
    ContentDomain getArticlesById(Integer cid);

    /**
     * 删除操作
     * @param cid
     */
    void deleteArticleById(Integer cid);

    void updateContentByCid(ContentDomain temp);
    /**
     * 根据编号获取文章
     * @param cid
     * @return
     */
    ContentDomain getArticleById(Integer cid);

    void updateCategory(String name, String cname);
}
