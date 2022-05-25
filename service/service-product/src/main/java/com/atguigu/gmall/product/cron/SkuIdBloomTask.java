package com.atguigu.gmall.product.cron;

import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.impl.SkuInfoServiceImpl;
import com.atguigu.gmall.service.service.SkuBloomTask;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/24 16:45
 */
@Slf4j
@Component
public class SkuIdBloomTask implements SkuBloomTask {

    @Autowired
    SkuInfoService skuInfoService;

    @Qualifier("skuIdBloom")
    @Autowired
    private RBloomFilter<Object> skuIdBloom;

    //秒 分 时 日 月 星期 年
    @Scheduled(cron = "0 0 3 * ? 3")
    public void rebuildBloom(){
        log.info("正在重建布隆....");
        //删除bloom
        skuIdBloom.delete();
        //重建bloom
        skuIdBloom.tryInit(5000000,0.000001);
        initData(skuIdBloom);
    }

    @Override
    public void initData(RBloomFilter<Object> skuIdBloom){
        log.info("正在初始化skuId布隆数据....");
        List<Long> ids = skuInfoService.getSkuInfoIds();
        for (Long id : ids) {
            skuIdBloom.add(id);
        }
    }


}
