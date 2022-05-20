package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService{

    @Resource
    private BaseAttrValueMapper baseAttrValueMapper;
    @Override
    public List<BaseAttrInfo> findAttrInfobyC1IdC2IdC3Id(Long category1Id, Long category2Id, Long category3Id) {
        return baseMapper.findAttrInfobyC1IdC2IdC3Id(category1Id,category2Id,category3Id);
    }

    @Override
    @Transactional
    public void saveOrUpdateAttrInfo(BaseAttrInfo baseAttrInfo) {
        //删除attrValueList中不存在的属性值
        List<Long> noDelVIds = new ArrayList<>();
        for (BaseAttrValue baseAttrValue : baseAttrInfo.getAttrValueList()) {
            if (baseAttrValue.getId() != null){
                Long id = baseAttrValue.getId();
                noDelVIds.add(id);
            }
        }

        if (!noDelVIds.isEmpty()){
            //不为空是指有保留原有数据
            QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("attr_id", baseAttrInfo.getId());
            queryWrapper.notIn("id", noDelVIds);
            baseAttrValueMapper.delete(queryWrapper);
        }else{
            //为空指删除所有原始数据
            QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("attr_id", baseAttrInfo.getId());
            baseAttrValueMapper.delete(queryWrapper);
        }

        if (baseAttrInfo.getId() != null){
            //修改
            baseMapper.updateById(baseAttrInfo);
            for (BaseAttrValue baseAttrValue : baseAttrInfo.getAttrValueList()) {
                if (baseAttrValue.getId() == null){
                    baseAttrValue.setAttrId(baseAttrInfo.getId());
                    baseAttrValueMapper.insert(baseAttrValue);
                }else{
                    baseAttrValueMapper.updateById(baseAttrValue);
                }
            }
        }else{
            //添加
            //1.添加属性表info
            baseMapper.insert(baseAttrInfo);
            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
            for (BaseAttrValue baseAttrValue : attrValueList) {
                //2.添加数据表value
                //添加之前要设置属性名id
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }
    }
}




