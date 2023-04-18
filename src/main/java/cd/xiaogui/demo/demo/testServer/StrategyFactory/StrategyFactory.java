package cd.xiaogui.demo.demo.testServer.StrategyFactory;

import cd.xiaogui.demo.demo.testServer.StrategyFactory.annotation.WorkeEnum;
import cd.xiaogui.demo.demo.testServer.StrategyFactory.annotation.WorkeStrategyAnno;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个是工厂呀
 *
 * @author sunyawei3
 * @date 2023/3/22 4:27 下午
 */
@Slf4j
@Component
public class StrategyFactory {

    @Autowired
    private Map<String, WorkerInterface> workerInterfaceMap;

    /**
     * 这个就是个路由表
     */
    private static Map<WorkeEnum, WorkerInterface> routerMap = new HashMap<>();


    public void initRoute() {
        log.info("构建策略路由开始");

    }

    /**
     * 这个就是工厂要干的事，提供对应的实例
     * @param workeEnum
     * @return
     */
    public WorkerInterface getWorkerBean(WorkeEnum workeEnum) {
        WorkerInterface workerInterface = routerMap.get(workeEnum);


        return workerInterface;
    }

    /**
     * 这个就是加载路由用的
     */
    @Autowired
    private void showWorkerInterfaceMap() {
        workerInterfaceMap.forEach((k, v) -> {
            log.info(k);
            WorkeStrategyAnno annotation = v.getClass().getAnnotation(WorkeStrategyAnno.class);
            if (null == annotation) {
                // 当从bean实例的class上获取不到注解元信息时，通过AnnotationUtils工具类递归来获取
                annotation = AnnotationUtils.findAnnotation(v.getClass(), WorkeStrategyAnno.class);
                if (null == annotation) {
                    log.warn("代码配置错误：创建订单策略实现类{}未配置WorkeStrategyAnno注解", v.getClass().getName());
                    return;
                }
            }
            routerMap.put(annotation.worker(), v);
            log.info("k:{} v:{}", annotation.worker().getCode(), annotation.worker().getValue());
            log.info("routerMap:{}", JSON.toJSONString(routerMap));
        });
    }
}
