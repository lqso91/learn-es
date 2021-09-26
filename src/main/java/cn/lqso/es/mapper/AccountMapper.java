package cn.lqso.es.mapper;

import org.apache.ibatis.annotations.Select;
import cn.lqso.es.model.AccountInfo;

import java.util.List;

/**
 * @author luojie
 */
public interface AccountMapper {
    /**
     * 查询
     * @param size 条数
     * @return 查询结果
     */
    @Select("select yhbh, yhmc, yddz from kh_ydkh where rownum < #{size}")
    List<AccountInfo> findSome(int size);
}
