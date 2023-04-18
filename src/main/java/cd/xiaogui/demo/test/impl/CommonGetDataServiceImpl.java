package cd.xiaogui.demo.test.impl;

import cd.xiaogui.demo.dto.OrderDTO;
import cd.xiaogui.demo.dto.OrderQueryDto;
import cd.xiaogui.demo.test.CommonGetDataService;
import cd.xiaogui.demo.tools.CommonGetData;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * todo
 *
 * @author sunyawei3
 * @date 2023/4/18 3:49 下午
 */
@Service
@Slf4j
public class CommonGetDataServiceImpl extends CommonGetData<OrderDTO, OrderQueryDto> implements CommonGetDataService{

    @Override
    public Integer excute(List<Long> datas) {
        OrderQueryDto queryDto = new OrderQueryDto();
        return dataHandle(0L,1L, queryDto);
    }

    @Override
    public Integer handleData(OrderDTO vo) {
        log.info(JSON.toJSONString(vo));
        return 9000;
    }

    @Override
    public Long getDataCout(OrderQueryDto query) {
        return 1L;
    }

    @Override
    public List<OrderDTO> getDataList(OrderQueryDto query) {
        return null;
    }

    @Override
    public Boolean filterData(OrderDTO vo) {
        return false;
    }

    @Override
    public void eachQueryFilter(OrderQueryDto query, Long start, Long end) {
        query.setStartDate(new Date(start));
        query.setEndDate(new Date(end));
    }
}
