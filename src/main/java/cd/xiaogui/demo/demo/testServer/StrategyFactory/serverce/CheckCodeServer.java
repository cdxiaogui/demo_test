package cd.xiaogui.demo.demo.testServer.StrategyFactory.serverce;

import cd.xiaogui.demo.demo.testServer.StrategyFactory.WorkerInterface;
import cd.xiaogui.demo.demo.testServer.StrategyFactory.annotation.WorkeEnum;
import cd.xiaogui.demo.demo.testServer.StrategyFactory.annotation.WorkeStrategyAnno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 代码检测服务
 *
 * @author sunyawei3
 * @date 2023/3/22 4:54 下午
 */
@Slf4j
@Service
@WorkeStrategyAnno(worker = WorkeEnum.WORKE)
public class CheckCodeServer implements WorkerInterface {
    @Override
    public String working(String workInfo) {
        log.info("代码检测中……然后{}", workInfo);
        return "代码检测中……然后" + workInfo;
    }
}
