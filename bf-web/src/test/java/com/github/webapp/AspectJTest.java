package com.github.webapp;

import com.github.webapp.module.demo.DemoService;
import com.github.webapp.pojo.PageBean1;
import com.github.webapp.pojo.PageBean2;
import com.github.webapp.pojo.PageBean3;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 切面测试类
 *
 * @author zxy
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AspectJTest {

    @Autowired
    private DemoService demoService;

    @BeforeClass
    public static void beforeClass() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:jndi-spring.xml");
        DataSource ds = (DataSource) context.getBean("dataSource");
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        builder.bind("jdbc/database-pomDS_oracle", ds);
        builder.activate();
    }

    @Test
    public void test1() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", "2");
        map.put("pageSize", "20");

        Map obj = (Map) demoService.queryList(map);

        System.out.println(map);

        Assert.assertEquals(((List) obj.get("dataList")).size(), 14);
    }

    @Test
    public void test2() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageList", false);
        map.put("pageNum", "2");

        Map obj = (Map) demoService.queryList(map);

        System.out.println(map);

        Assert.assertEquals(((List) obj.get("dataList")).size(), 14);
    }

    @Test
    public void test3() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", "2");

        PageBean1 pb1 = new PageBean1();
        pb1.setPage(map);

        Map obj = (Map) demoService.queryList(pb1);

        System.out.println(pb1);

        Assert.assertEquals(((List) obj.get("dataList")).size(), 4);
    }

    @Test
    public void test4() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageList", "false");
        map.put("pageNum", "2");

        PageBean1 pb1 = new PageBean1();
        pb1.setPage(map);

        Map obj = (Map) demoService.queryList(pb1);

        System.out.println(pb1);

        Assert.assertEquals(((List) obj.get("dataList")).size(), 14);
    }

    @Test(expected = NoSuchFieldException.class)
    public void test5() {
        demoService.queryList(new PageBean2());
    }

    @Test
    public void test6() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", "2");

        PageBean3 pb3 = new PageBean3();
        pb3.setPage(map);
        pb3.setPageList(false);

        Map obj = (Map) demoService.queryList(pb3);

        System.out.println(pb3);

        Assert.assertEquals(((List) obj.get("dataList")).size(), 14);
    }
}
