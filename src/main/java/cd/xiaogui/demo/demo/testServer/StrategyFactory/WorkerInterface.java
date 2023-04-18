package cd.xiaogui.demo.demo.testServer.StrategyFactory;

/**
 * 上班的接口啊
 *
 * @author sunyawei3
 * @date 2023/3/22 4:50 下午
 */
public interface WorkerInterface {

    /**
     * 在工作什么
     * @param workInfo 工作内容
     */
    public String working(String workInfo);


}
