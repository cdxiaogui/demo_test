package cd.xiaogui.demo.demo.controller;

import cd.xiaogui.demo.demo.dto.PreStartBeanDTO;
import cd.xiaogui.demo.tools.SpringUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 测试的controller
 *
 * @author sunyawei3
 * @date 2023/3/20 6:14 下午
 */
@RestController
public class TestController {

    @Autowired
    ApplicationContext context;

    @RequestMapping("/testBean")
    public String testBean(){
        PreStartBeanDTO dto= SpringUtil.getBean("preStartBeanDTO");
        PreStartBeanDTO dto2= (PreStartBeanDTO) context.getBean("preStartBeanDTO");

        return JSON.toJSONString(dto) + "  是否内容相等" + Objects.equals(dto2,dto);
    }

}
