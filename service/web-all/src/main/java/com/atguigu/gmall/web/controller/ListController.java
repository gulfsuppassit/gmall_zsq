package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.util.MD5;
import com.atguigu.gmall.feign.list.ListFeignClient;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponsePropVo;
import com.atguigu.gmall.model.list.SearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/29 13:25
 */
@Controller
public class ListController {
    @Autowired
    private ListFeignClient listFeignClient;

    @GetMapping("/list.html")
    public String list(SearchParam param, Model model, HttpServletRequest request) {

        SearchResponseVo responseVo = listFeignClient.searchGoods(param).getData();
        //设置
        model.addAttribute("searchParam",param);
        //设置品牌面包屑
        String trademark = param.getTrademark();
        String trademarkParam = null;
        if (!StringUtils.isEmpty(trademark)){
            String[] split = trademark.split(":");
            trademarkParam = "品牌: "+split[1];
        }
        model.addAttribute("trademarkParam",trademarkParam);

        //拼urlParam:访问路径
        String urlParamString = request.getQueryString();
        String urlParam = urlParamString.replace("&order=", "");
        String queryString = "list.html?" + urlParam;
        String sss = packageUrlParam(param);
        model.addAttribute("urlParam",sss);

        //设置平台属性面包屑
        String[] props = param.getProps();
        List<SearchResponsePropVo> propsParamList = new ArrayList<>();
        if (props != null && props.length > 0){
            for (String prop : props) {
                //23:8G:运行内存 => 运行内存：8G
                String[] split = prop.split(":");
                SearchResponsePropVo propVo = new SearchResponsePropVo();
                propVo.setAttrId(split[0]);
                propVo.setAttrValue(split[1]);
                propVo.setAttrName(split[2]);
                propsParamList.add(propVo);
            }
        }
        model.addAttribute("propsParamList",propsParamList);

        //设置品牌列表
        model.addAttribute("trademarkList",responseVo.getTrademarkList());
        //设置属性列表
        model.addAttribute("attrsList",responseVo.getAttrsList());
        //设置排序
        Map<String, String> orderMap = getOrderMap(param);
        model.addAttribute("orderMap",orderMap);

        //设置商品列表
        model.addAttribute("goodsList", responseVo.getGoodsList());
        //设置当前页
        model.addAttribute("pageNo", responseVo.getPageNo());
        //设置总页数
        model.addAttribute("totalPages",responseVo.getTotalPages());
        return "list/index";
    }

    private Map<String, String> getOrderMap(SearchParam param) {
        Map<String, String> orderMap = new HashMap<>();
        String order = param.getOrder();
        if (!StringUtils.isEmpty(order)){
            String[] split = order.split(":");
            orderMap.put("type",split[0]);
            orderMap.put("sort",split[1]);
        }else{
            orderMap.put("type","1");
            orderMap.put("sort","desc");
        }
        return orderMap;
    }

    String packageUrlParam(SearchParam param) {
        StringBuilder urlParamSb = new StringBuilder("list.html?"); //category1Id=1&
        if(param.getCategory1Id()!= null){
            urlParamSb.append("category1Id="+param.getCategory1Id()+"&");
        }

        if(param.getCategory2Id()!=null){
            urlParamSb.append("category2Id="+param.getCategory2Id()+"&");
        }

        if(param.getCategory3Id()!=null){
            urlParamSb.append("category3Id="+param.getCategory3Id()+"&");
        }

        if(!StringUtils.isEmpty(param.getTrademark())){
            urlParamSb.append("trademark="+param.getTrademark()+"&");
        }

        if(!StringUtils.isEmpty(param.getKeyword())){
            urlParamSb.append("keyword="+param.getKeyword()+"&");
        }

        if(param.getProps()!=null && param.getProps().length>0){
            //list.html?props=1:18GB:机身内存&props=2:18GB:运行内存
            for (String prop : param.getProps()) {
                urlParamSb.append("props="+prop+"&");
            }
        }
        //忽略了order、pageNo

//        urlParamSb.append("pageNo="+param.getPageNo()+"&");
//        urlParamSb.append("pageSize="+param.getPageSize()+"&");
        return urlParamSb.toString();
    }

}
