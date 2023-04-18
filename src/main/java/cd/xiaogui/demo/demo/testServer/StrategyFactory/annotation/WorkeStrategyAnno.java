package cd.xiaogui.demo.demo.testServer.StrategyFactory.annotation;

import java.lang.annotation.*;

/**
 * 一个策略注解
 *
 * @author sunyawei3
 * @date 2023/3/22 4:31 下午
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface WorkeStrategyAnno {

    WorkeEnum worker()  default WorkeEnum.WORKE;

}
