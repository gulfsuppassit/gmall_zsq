package com.atguigu.gmall.model.to;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/21 10:31
 */
@Data
@ApiModel(description = "用于封装前端需要的分类数据")
public class CategoryAndChildTo {

    private Long categoryId;

    private String categoryName;

    private List<CategoryAndChildTo> categoryChild;
}
