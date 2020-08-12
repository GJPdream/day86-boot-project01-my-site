package cn.itcast.service;

import cn.itcast.dto.cond.MetaCond;
import cn.itcast.dto.cond.MetaDto;
import cn.itcast.model.MetaDomain;

import java.util.List;
public interface MetaService {
    List<MetaDomain> getMetas(MetaCond metaCond);

    /**
     * 批量添加
     * @param cid
     * @param names
     * @param type
     */
    void addMetas(Integer cid, String names, String type);
    void saveOrUpdate(Integer cid, String name, String type);
    /**
     * 添加项目
     * @param meta
     * @return
     */
    void addMeta(MetaDomain meta);
    /**
     * 根据类型查询项目列表，带项目下面的文章数
     * @param type
     * @param orderby
     * @param limit
     * @return
     */

    List<MetaDto> getMetaList(String type, String orderby, int limit);
    /**
     * 添加
     * @param type
     * @param name
     * @param mid
     */
    void saveMeta(String type, String cname, Integer mid);

    /**
     * 删除操作
     * @param mid
     */
    void deleteMetaById(Integer mid);
}
