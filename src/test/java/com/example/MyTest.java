package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.HtmlUtils;

/**
 * @author Javen
 * @date 2022/2/3
 */
public class MyTest {

    @Test
    public void test() {
        String str = "#测试转义：#<table id=\"testid\"><tr>test1;test2</tr></table>";
        System.out.println(HtmlUtils.htmlEscape(str));
        String s = HtmlUtils.htmlEscape(str);
        System.out.println(HtmlUtils.htmlUnescape(s));
    }
}
