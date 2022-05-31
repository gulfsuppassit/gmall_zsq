package com.atguigu.gmall.list.dao;

import com.atguigu.gmall.model.list.Goods;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/29 10:08
 */
@Repository
public interface GoodsDao extends CrudRepository<Goods, Long> {

}
