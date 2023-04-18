package cd.xiaogui.demo.demo.controller;

import cd.xiaogui.demo.demo.testServer.StrategyFactory.StrategyFactory;
import cd.xiaogui.demo.demo.testServer.StrategyFactory.WorkerInterface;
import cd.xiaogui.demo.demo.testServer.StrategyFactory.annotation.WorkeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 策略做测试用的
 *
 * @author sunyawei3
 * @date 2023/3/22 5:20 下午
 */
@RestController()
@RequestMapping("/strategyTest")
public class TestStrategyFactoryController {
    @Autowired
    StrategyFactory strategyFactory;

    @RequestMapping("/getServerImpl")
    public String getServerImpl(int workecode, String next){
        WorkeEnum workeEnum = WorkeEnum.getEnumByCode(workecode);
        if (workeEnum == null) {
            return "workecode不在策略内";
        }
        WorkerInterface workerInterface = strategyFactory.getWorkerBean(workeEnum);

        return workerInterface.working(next);
    }
}
