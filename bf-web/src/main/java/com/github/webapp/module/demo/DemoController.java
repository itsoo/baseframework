package com.github.webapp.module.demo;

import com.github.webapp.module.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * DemoController
 *
 * <p>后端不在关注视图相关的逻辑，后端提供交换数据等接口，视图相
 * 关交给前端路由处理，这里只做一个前后端交互的演示
 *
 * @author zxy
 */
@RestController
@RequestMapping("/demo")
public class DemoController extends BaseController {

    @Autowired
    private DemoService demoService;

    @GetMapping
    public void demo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        renderPage("demo", request, response);
    }

    @PostMapping("add")
    public int add(@RequestBody Map<String, Object> param) {
        return demoService.add(param);
    }

    @GetMapping("query_list")
    public Object queryList() {
        return demoService.queryList(null);
    }
}
