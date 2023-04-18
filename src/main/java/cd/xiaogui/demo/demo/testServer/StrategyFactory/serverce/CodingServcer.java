package cd.xiaogui.demo.demo.testServer.StrategyFactory.serverce;

import cd.xiaogui.demo.demo.testServer.StrategyFactory.WorkerInterface;
import cd.xiaogui.demo.demo.testServer.StrategyFactory.annotation.WorkeEnum;
import cd.xiaogui.demo.demo.testServer.StrategyFactory.annotation.WorkeStrategyAnno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * todo
 *
 * @author sunyawei3
 * @date 2023/3/22 4:58 下午
 */
@Slf4j
@Service
@WorkeStrategyAnno(worker = WorkeEnum.NO_WORKE)
public class CodingServcer implements WorkerInterface {
    @Override
    public String working(String workInfo) {
        log.info("撸完代码，然后{}", workInfo);
        return "撸完代码，然后" + workInfo;
    }
}
