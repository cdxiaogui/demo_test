package cd.xiaogui.demo.demo.testServer.StrategyFactory.annotation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 策略模式用的
 *
 * @author sunyawei3
 * @date 2023/3/22 4:33 下午
 */

public enum WorkeEnum implements Serializable {
    /**
     *
     */
    WORKE(1,"工作"),
    NO_WORKE(2,"摸鱼");
    int code;
    String value;

    WorkeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public static WorkeEnum getEnumByCode(int code){
        for (WorkeEnum workeEnum : values()) {
            if (workeEnum.code == code) {
                return workeEnum;
            }
        }

        return null;
    }
}
