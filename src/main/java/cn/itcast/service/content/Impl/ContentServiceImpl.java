package cn.itcast.service.content.Impl;

import cn.itcast.constant.ErrorContant;
import cn.itcast.constant.Types;
import cn.itcast.constant.WebConst;
import cn.itcast.dao.ContentDao;
import cn.itcast.dao.RelationShipDao;
import cn.itcast.dto.cond.ContentCond;
import cn.itcast.exception.BusinessException;
import cn.itcast.model.ContentDomain;
import cn.itcast.model.RelationShipDomain;
import cn.itcast.service.MetaService;
import cn.itcast.service.content.ContentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.autoconfigure.PageHelperProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.service.Tags;

import java.util.List;


@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
  private ContentDao contentDao;

    @Autowired
    MetaService metaService;
    @Autowired
    RelationShipDao relationShipDao;
    @Override
    public void addArticle(ContentDomain contentDomain) {
  if (contentDomain==null)
      throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
   if (StringUtils.isBlank(contentDomain.getTitle()))
       throw BusinessException.withErrorCode(ErrorContant.Article.TITLE_CAN_NOT_EMPTY);
        if (contentDomain.getTitle().length() > WebConst.MAX_TITLE_COUNT)
            throw BusinessException.withErrorCode(ErrorContant.Article.TITLE_IS_TOO_LONG);
        if (StringUtils.isBlank(contentDomain.getContent()))
            throw BusinessException.withErrorCode(ErrorContant.Article.CONTENT_CAN_NOT_EMPTY);
        if (contentDomain.getContent().length() > WebConst.MAX_TEXT_COUNT)
            throw BusinessException.withErrorCode(ErrorContant.Article.CONTENT_IS_TOO_LONG);
       /*标签分类*/
        String tags = contentDomain.getTags();
        String categories = contentDomain.getCategories();
        contentDao.addArticle(contentDomain);
        int cid = contentDomain.getCid();
     /*   System.out.println(cid);*/
        metaService.addMetas(cid, tags, "tag");
        metaService.addMetas(cid, categories, "category");
    }

    @Override
    public void updateArticleById(ContentDomain contentDomain) {
       //标签和分类
        String tags = contentDomain.getTags();
        String categories = contentDomain.getCategories();
        contentDao.updateArticleById(contentDomain);
        Integer cid = contentDomain.getCid();
      relationShipDao.deleteRelationShipByCid(cid);
        metaService.addMetas(cid, tags,Types.TAG.getType());
        metaService.addMetas(cid,categories,Types.CATEGORY.getType());
    }

    @Override
    public PageInfo getArticlesByCond(ContentCond contentCond, int page, int limit) {
               if (contentCond==null)
            throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
        PageHelper.startPage(page,limit);
        List<ContentDomain> re=contentDao.getArticlesByCond(contentCond);
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(re);
        return pageInfo;
    }

    /**
     * 通过cid查找到内容然后封装起来
     * @param cid
     * @return
     */
    @Override
    public ContentDomain getArticlesById(Integer cid) {
        if (cid==null)
            throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
        return contentDao.getArticlesById(cid);
    }

    /**
     * 删除
     * @param cid
     */
    @Override
    public void deleteArticleById(Integer cid) {
       if (cid==null)
           throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
       contentDao.deleteArticleById(cid);

    }

    @Override
    public void updateContentByCid(ContentDomain content) {
        if (null != content && null != content.getCid()) {
            contentDao.updateArticleById(content);
        }
    }

    @Override
    public ContentDomain getArticleById(Integer cid) {
       if (cid==null)
           throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
        return contentDao.getArticleById(cid);
    }

    @Override
    public void updateCategory(String ordinal, String newCatefory) {
        ContentCond cond = new ContentCond();
        cond.setCategory(ordinal);
        List<ContentDomain> atricles = contentDao.getArticlesByCond(cond);
        atricles.forEach(atricle -> {
            //遍历一个然后就更改一个；
            atricle.setCategories(atricle.getCategories().replace(ordinal, newCatefory));
            contentDao.updateArticleById(atricle);
        });
    }
}
