import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * todo
 *
 * @author sunyawei3
 * @date 2023/3/28 4:14 下午
 */
public class Test {

//    public String batchCompare(String buId, String[] orderIds, String env){
//        List<String> total_result =new ArrayList<>();
//        List<String> exception_result =new ArrayList<>();
//        Arrays.stream(orderIds).parallel().forEach(orderId->{
//            String result = null;
//            try {
//                result = orderCompare(buId,orderId,env);
//            } catch (Exception e) {
//                // 比对有问题的记录一下，要是orderCompare有结果放进去也行
//                exception_result.add("比对有问题的" + orderIds);
//            }
//
//            total_result.add(result+"\r\n");
//        });
//        return String.valueOf(total_result) + "\r\n" + String.valueOf(exception_result);
//    }
}
