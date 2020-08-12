package cn.itcast.dao;


import cn.itcast.dto.cond.MetaCond;
import cn.itcast.dto.cond.MetaDto;
import cn.itcast.model.MetaDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/*@Mapper*/
@Mapper
public interface MetaDao {
    /**
     * 根据条件查询
     * @param metaCond
     * @return
     */
    List<MetaDomain> getMetasByCond(MetaCond metaCond);

    /**
     * 添加项目
     * @param meta
     * @return
     */
    void addMeta(MetaDomain meta);

    List<MetaDto> selectFromSql(Map<String, Object> paraMap);

    MetaDomain getMetaById(Integer mid);

    void updateMeta(MetaDomain metaDomain);

    void deleteMetaById(Integer mid);
}


