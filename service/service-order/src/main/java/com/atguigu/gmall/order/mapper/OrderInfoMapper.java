package com.atguigu.gmall.order.mapper;

import com.atguigu.gmall.model.order.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 86180
* @description 针对表【order_info_1(订单表 订单表)】的数据库操作Mapper
* @createDate 2022-06-05 17:21:25
* @Entity com.atguigu.gmall.order.domain.OrderInfo
*/
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    void updateStatus(@Param("orderInfo") OrderInfo orderInfo,@Param("originStatus") String originStatus);

    void updateOrderStatusToPAID(@Param("processStatus") String processStatus,
                                 @Param("orderStatus") String orderStatus,
                                 @Param("outTradeNo") String outTradeNo,
                                 @Param("userId") Long userId);
}




