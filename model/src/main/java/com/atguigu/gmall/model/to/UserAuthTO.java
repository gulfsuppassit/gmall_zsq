package com.atguigu.gmall.model.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/1 18:58
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAuthTO {

    private Long userId;

    private String UserTempId;

}
