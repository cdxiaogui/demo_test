package cd.xiaogui.demo.demo.dto;

import lombok.Data;

/**
 * 启动成功前bean测试
 *
 * @author sunyawei3
 * @date 2023/3/20 6:09 下午
 */

@Data
public class PreStartBeanDTO {

    /**
     * 组织名字
     */
    private String name;
    /**
     * 年纪
     */
    private Integer age;
    /**
     * 钱
     */
    private Long money;
}
