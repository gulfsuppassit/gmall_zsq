package com.atguigu.gmall.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/5 10:43
 */
@SpringBootTest
public class test {

//    @Test
    public void test(){

        List<Sku> skus1 = Arrays.asList(new Sku(1L, 1), new Sku(2L, 5), new Sku(3L, 2));

        List<Sku> skus = Arrays.asList(new Sku(2L, 1), new Sku(3L, 2), new Sku(4L, 3), new Sku(5L, 1));

        Map<Long,Sku> map = new HashMap<>();
        Stream.concat(skus.stream(), skus1.stream())
                .forEach(sku -> {
                    Sku abc = sku;
                    if (map.containsKey(abc.getId())){
                        abc = map.get(sku.getId());
                        abc.setNum(abc.getNum() + sku.getNum());
                    }
                    map.put(abc.getId(), abc);
                });
        System.out.println(map);
    }

}
@AllArgsConstructor
@NoArgsConstructor
@Data
class Sku{
    Long id;
    Integer num;
}
