package com.atguigu.gmall.model.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/7 19:19
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderCreateTo {

    private Long userId;

    private Long orderId;

}
