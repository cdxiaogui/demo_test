package cd.xiaogui.demo.demo.controller;

import cd.xiaogui.demo.test.CommonGetDataService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * todo
 *
 * @author sunyawei3
 * @date 2023/4/18 4:07 下午
 */
@RestController
@RequestMapping("/com")
public class CommonGetDataComtroller {

    @Autowired
    CommonGetDataService service;

    @RequestMapping("/run")
    public String excuteData(Long order){
        String res ="rest:" +service.excute(Lists.newArrayList(order));
        return res;
    }

}
