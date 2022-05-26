package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/25 20:37
 */
@SpringBootTest
public class SpelTest {

    SpelExpressionParser expressionParser = new SpelExpressionParser();

    @Test
    public void spelTest() {
        String expression = "sku:detail:#{#args[0]}";

        ParserContext parserContext = ParserContext.TEMPLATE_EXPRESSION;

        Expression parseExpression = expressionParser.parseExpression(expression,
                parserContext);

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("args", Arrays.asList(33L, 44L, 55L));

        Object value = parseExpression.getValue(context, Object.class);
        System.out.println("value = " + value);
    }
}
