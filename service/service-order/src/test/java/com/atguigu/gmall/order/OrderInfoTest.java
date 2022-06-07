package com.atguigu.gmall.order;
import java.math.BigDecimal;
import com.google.common.collect.Lists;
import java.util.Date;
import com.atguigu.gmall.model.activity.CouponInfo;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/5 18:06
 */
@SpringBootTest
public class OrderInfoTest {

    @Resource
    private OrderInfoMapper orderInfoMapper;

//    @Transactional
//    @Test
    public void addTest(){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setConsignee("23");
        orderInfo.setConsigneeTel("234");
        orderInfo.setTotalAmount(new BigDecimal("111"));
        orderInfo.setOrderStatus("35341231");
        orderInfo.setUserId(3L);
        orderInfo.setPaymentWay("agdfgdf");
        orderInfo.setDeliveryAddress("dfgdgd");
        orderInfo.setOrderComment("dgds");
        orderInfo.setOutTradeNo("dgd");
        orderInfo.setTradeBody("dsgdg");
        orderInfo.setCreateTime(new Date());
        orderInfo.setExpireTime(new Date());
        orderInfo.setProcessStatus("ewweg");
        orderInfo.setTrackingNo("dfgfdd");
        orderInfo.setParentOrderId(0L);
        orderInfo.setImgUrl("gdgdgdg");
        orderInfo.setOrderDetailList(Lists.newArrayList());
        orderInfo.setWareId("dgfdgdfdfg");
        orderInfo.setProvinceId(0L);
        orderInfo.setActivityReduceAmount(new BigDecimal("0"));
        orderInfo.setCouponAmount(new BigDecimal("0"));
        orderInfo.setOriginalTotalAmount(new BigDecimal("0"));
        orderInfo.setRefundableTime(new Date());
        orderInfo.setFeightFee(new BigDecimal("0"));
        orderInfo.setOperateTime(new Date());
        orderInfo.setOrderDetailVoList(Lists.newArrayList());
        orderInfo.setCouponInfo(new CouponInfo());

        orderInfoMapper.insert(orderInfo);
    }

}
