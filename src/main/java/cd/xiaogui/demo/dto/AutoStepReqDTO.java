package cd.xiaogui.demo.dto;

import lombok.Data;

/**
 * @author sunyawei3
 * 创建时间 2022/12/9 8:38 下午
 */

@Data
public class AutoStepReqDTO {
    private long dataSize;
    private long step;

    private AutoStepReqDTO(long dataSize, long step) {
        this.dataSize = dataSize;
        this.step = step;
    }
    public static AutoStepReqDTO build(long dataSize, long step){
        return new AutoStepReqDTO(dataSize, step);
    }
}
