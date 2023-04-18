package cd.xiaogui.demo.dto;

import lombok.Data;

/**
 *
 * 动态调整步长结果
 * @author sunyawei3
 */
@Data
public class AutoStepResultDTO {
    /**
     * 步长重新调整标识
     */
    private Boolean ifReChangeStep = false;

    /**
     * 下一步要调整的步长
     */
    private Long afterStep;

    public AutoStepResultDTO(Boolean ifReChangeStep, Long afterStep) {
        this.ifReChangeStep = ifReChangeStep;
        this.afterStep = afterStep;
    }

    public AutoStepResultDTO(Long afterStep) {
        this.afterStep = afterStep;
    }

    public AutoStepResultDTO() {
    }
}
