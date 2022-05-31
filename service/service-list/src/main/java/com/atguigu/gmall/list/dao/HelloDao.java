package com.atguigu.gmall.list.dao;

import com.atguigu.gmall.list.bean.Hello;
import org.springframework.data.repository.CrudRepository;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/29 9:51
 */
public interface HelloDao extends CrudRepository<Hello, Long> {

}
