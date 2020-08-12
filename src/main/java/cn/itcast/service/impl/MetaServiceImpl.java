package cn.itcast.service.impl;

import cn.itcast.constant.ErrorContant;
import cn.itcast.constant.WebConst;
import cn.itcast.dao.MetaDao;
import cn.itcast.dao.RelationShipDao;
import cn.itcast.dto.cond.MetaCond;
import cn.itcast.dto.cond.MetaDto;
import cn.itcast.exception.BusinessException;
import cn.itcast.model.MetaDomain;
import cn.itcast.model.RelationShipDomain;
import cn.itcast.service.MetaService;
import cn.itcast.service.content.ContentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MetaServiceImpl implements MetaService {
    @Autowired
    MetaDao metaDao;

    @Autowired
    private RelationShipDao relationShipDao;
    @Autowired
    ContentService contentService;

    @Override
    public List<MetaDomain> getMetas(MetaCond metaCond) {
        return metaDao.getMetasByCond(metaCond);
    }

    @Override
    /*@CacheEvict(value={"metaCaches","metaCache"},allEntries=true,beforeInvocation=true)*/
    public void addMeta(MetaDomain meta) {
        if (null == meta)
            throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
        metaDao.addMeta(meta);

    }
    @Override
    public void addMetas(Integer cid, String names, String type) {
        if (null == cid)
            throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
        if (StringUtils.isNoneBlank(names) && StringUtils.isNoneBlank(type)) {
            String[] nameArr = StringUtils.split(names, ",");
            for (String name : nameArr) {
                this.saveOrUpdate(cid, name, type);
            }
        }


    }

    @Override
    public void saveOrUpdate(Integer cid, String name, String type) {
        MetaCond metaCond = new MetaCond();
        metaCond.setName(name);
        metaCond.setType(type);
        List<MetaDomain> metas = this.getMetas(metaCond);

        int mid;
        MetaDomain metaDomain;
        if (metas.size() == 1){
            MetaDomain meta = metas.get(0);
            mid = meta.getMid();
        }else if (metas.size() > 1){
            throw BusinessException.withErrorCode(ErrorContant.Meta.NOT_ONE_RESULT);
        } else {
            metaDomain = new MetaDomain();
            metaDomain.setSlug(name);
            metaDomain.setName(name);
            metaDomain.setType(type);
            this.addMeta(metaDomain);
            mid = metaDomain.getMid();
    }
        if (mid != 0){
            Long count = relationShipDao.getCountById(cid, mid);
            if (count == 0){
                RelationShipDomain relationShip = new RelationShipDomain();
                relationShip.setCid(cid);
                relationShip.setMid(mid);
                relationShipDao.addRelationShip(relationShip);
            }

        }
    }

    @Override
    public List<MetaDto> getMetaList(String type,  String orderby, int limit) {
       if (StringUtils.isNotBlank(type)){
           if (StringUtils.isBlank(orderby)){
               orderby="count desc ,a.mid desc";
           }
           if (limit<1||limit> WebConst.MAX_POSTS){
               limit=10;
           }
           Map<String,Object> paraMap=new HashMap<>();
           paraMap.put("type",type);
           paraMap.put("order",orderby);
           paraMap.put("limit",limit);
           return metaDao.selectFromSql(paraMap);
       }
        return null;
    }

    @Override
    public void saveMeta(String type, String cname, Integer mid) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(cname))
        {
            MetaCond metaCond=new MetaCond();
            metaCond.setName(cname);
            metaCond.setType(type);
            List<MetaDomain> metas = metaDao.getMetasByCond(metaCond);
            if (metas==null||metas.size()==0) {
                MetaDomain metaDomain = new MetaDomain();
                metaDomain.setName(cname);
                if (mid!=null){
                    MetaDomain meta= metaDao.getMetaById(mid);
                       if(meta!=null){
                           metaDomain.setMid(mid);
                       }
                       metaDao.updateMeta(metaDomain);
                    if(meta !=null) {
                        contentService.updateCategory(meta.getName(), cname);
                    }
                } else {
                    metaDomain.setType(type);
                    metaDao.addMeta(metaDomain);
                }
                } else {
                throw BusinessException.withErrorCode(ErrorContant.Meta.META_IS_EXIST);

            }
        }
    }

    @Override
    public void deleteMetaById(Integer mid) {
        if (mid==null)
            throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
        metaDao.deleteMetaById(mid);
    }
}