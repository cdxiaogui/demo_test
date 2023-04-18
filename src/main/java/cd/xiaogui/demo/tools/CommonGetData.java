package cd.xiaogui.demo.tools;

import cd.xiaogui.demo.dto.AutoStepReqDTO;
import cd.xiaogui.demo.dto.AutoStepResultDTO;
import com.alibaba.fastjson.JSON;
//import com.jd.retail.trade.tools.UMP;
//import com.jd.ump.profiler.CallerInfo;
//import com.jd.wj.order.structure.common.LoggerHelper;
//import com.jd.wj.order.structure.common.ump.Ump;
//import com.jd.wj.order.structure.domain.query.SearchParamemter;
//import com.jd.wj.order.worker.common.constants.UmpKeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 批量取数
 * @param <Q> 请求参数
 * @param <T> 请求返回的结果
 */
@Slf4j
public abstract class CommonGetData<T, Q extends SearchParamemter> {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * es有校验 最大一次查500
     */
    private void initBaseEsQuery(Q query) {
        //组装初始查询条件 限制下取数个数
        query.setPageSize(initEsPageSize());
        query.setPage(true);
    }

    /**
     * 默认步长
     *
     * @return 步长
     */
    protected Long initSetp() {
        // 默认5分钟
        return 5 * 60 * 1000L;
    }

    /**
     * 默认步长
     *
     * @return 步长
     */
    protected Integer initEsPageSize() {
        // 默认500 es有校验 最大一次查500
        return 500;
    }

    /**
     * 默认分页处理一页多少条
     *
     * @return 每页处理条数
     */
    protected Integer initDataHandleSize() {
        // 默认500 es有校验 最大一次查500
        return 500;
    }

    /**
     * 分块处理，每块取数的基准值
     *
     * @return 基准值
     */
    protected Integer initDataHandleStandard() {
        // 默认6000 es有最大一次查10000
        return 6000;
    }

    /**
     * * 数据处理开关
     *
     * @return 默认为开
     */
    protected Boolean dataHandleOpen() {
        return true;
    }

    public Integer dataHandle(Long start, Long end, Q query) {
//        CallerInfo info = Ump.methodReg(UmpKeyConstants.JOB.commonGetData);
//        LoggerHelper.setLogInvokeNo("数据分块处理:" + start + "-" + end);
        int allCount = 0; //整体修改数量
        try {
            // 初始化查询条件
            initBaseEsQuery(query);
            // 获取默认步长
            Long pullSetp = initSetp();
            // 设置处理窗口大小
            Long dataStart = start;
            Long dataEnd = getDataEnd(start, end, pullSetp);
            //当开始时间小于结束时间时 进行时间段的循环查询
            long loopCount = 0; //while环数


            while (dataStart < end) {
                // 每次执行时候执行的条件
                eachQueryFilter(query, dataStart, dataEnd);
                //按照时间段卡数据 查询总数
                Long count = getDataCout(query);
                log.info("查询数量结果：{} 条件是：{}", count, JSON.toJSONString(query));
                AtomicInteger ret = new AtomicInteger();
                // 动态适应计算不长
                if (count == null){
                    throw new Exception("查询数据异常，count为null，检查getDataCout实现");
                }
                AutoStepResultDTO autoStepRes = autoStep2(AutoStepReqDTO.build(count, pullSetp));
                // 步长太长，调整步长后重新执行
                if (autoStepRes.getIfReChangeStep()) {
                    log.info("步长太大，调整步长重新执行任务当前步长:{} 调整后{}", pullSetp, autoStepRes.getAfterStep());
                    // 开始标不变，截止标 重新按步长计算值
                    dataEnd = dataEnd - pullSetp + autoStepRes.getAfterStep();
                    pullSetp = autoStepRes.getAfterStep();
                    continue;
                }
                // 更新步长
                pullSetp = autoStepRes.getAfterStep();
                if (count != 0) {
                    // 处理数据核心流程
                    handleDataCoreFlow(query, loopCount, count, ret);
                }
                //当前日期完成之后，需要修改时间，将时间段向前
                dataStart = dataEnd;
                dataEnd += pullSetp;
                if (dataEnd > end) {
                    dataEnd = end;
                }
                log.info("第{}环，本次处理完成,共处理{}条数据。条件修改为st={} ,en={}", loopCount, ret, sdf.format(dataStart), sdf.format(dataEnd));
                loopCount++;
                allCount += ret.get();
            }
            log.info("本次任务处理完毕，共处理{}条记录 ", allCount);
        } catch (Exception e) {
            log.error("数据分块处理发生异常：", e);
//            Ump.funcError(info);
            throw e;
        } finally {
//            Ump.methodRegEnd(info);
//            LoggerHelper.removeLogInvokeNo();
            return allCount;
        }
    }

    private void handleDataCoreFlow(Q query, long loopCount, Long count, AtomicInteger ret) {
        //查询到数量之后 进行处理
        long totalPage = getTotalPage(count);
        for (int i = 1; i <= totalPage; i++) {
            log.info("第{}环，当前处理第{}页,总{}页", loopCount, i, totalPage);
            query.setIndex(i);
            //查询es中订单列表
            List<T> esOrderList = getDataList(query);
            if (CollectionUtils.isEmpty(esOrderList)) {
                log.info("第{}页es查询为空，不做处理！，条件：{}", i, JSON.toJSONString(query));
                continue;
            }
            esOrderList.stream().filter(this::filterData).forEach(dto -> {
                try {
                    log.info("开始处理数据 开关是否打开：{}", dataHandleOpen());
                    if (dataHandleOpen()) {
                        log.info("处理数据开始，已成功{}个===============", ret);
                        ret.addAndGet(handleData(dto));
                        log.info("处理数据结束，成功共{}个===============", ret);
                    }
                } catch (Exception e) {
                    log.error("数据处理出现异常：{}", JSON.toJSONString(dto), e);
//                    UMP.businessAlarm(UmpKeyConstants.ALARM.COMMON_GET_DATA_HANDLE, "数据处理出现异常");
                }
            });

        }
    }


    private Long getDataEnd(Long start, Long end, Long pullSetp) {
        Long dataEnd = start + pullSetp;
        if (dataEnd > end) {
            dataEnd = end;
        }
        return dataEnd;
    }

    private long getTotalPage(Long count) {
        long totalPage = count / initDataHandleSize();
        if (count % initDataHandleSize() != 0) {
            totalPage++;
        }
        return totalPage;
    }



    /**
     * 这里写核心处理逻辑，处理完1条返回1，处理失败返回0
     */
    public abstract Integer handleData(T vo);

    /**
     * 获取每一批的数量：必须实现
     *
     * @param query 查询条件
     * @return 查询数量
     */
    public abstract Long getDataCout(Q query);

    /**
     * 获取数据
     *
     * @param query 查询条件
     * @return 返回查到的数据
     */
    public abstract List<T> getDataList(Q query);

    /**
     * 不需要处理的数据，过滤数据
     */
    public abstract Boolean filterData(T vo);


    /**
     * 第一步:必须实现
     * 每轮执行的条件
     *
     * @param query 查询条件*
     */
    public abstract void eachQueryFilter(Q query, Long start, Long end);


    /**
     * 动态获取步长
     * 策略1、当前没数据，步长指数增长 ==> 没数据时候,为了加快检索速度，步长直接跨2倍
     * 策略2、基准值 > 当前数据量 > 75%基准值 ==> 已经接近处理阈值，步长不变
     * 策略3、数据长度比期望值大  ==> 按比率 缩步长
     * 策略4、数据小于75%期望值， ==> 一次扩1.5倍步长
     * 出参： resultDTO.getIfReChangeStep()为true ，需要重新调整步长，调整值为 resultDTO.getAfterStep()
     * resultDTO.getIfReChangeStep()为false，无需重新回溯调整步长，无需调整步长
     *
     *   autoStepReqDTO.dataSize 数据长度
     *   autoStepReqDTO.step     当前步长
     */
    public AutoStepResultDTO autoStep2(AutoStepReqDTO autoStepReqDTO) {
        return strategyRouter(autoStepReqDTO).apply(autoStepReqDTO);
    }

    private Function<AutoStepReqDTO, AutoStepResultDTO> strategyRouter(AutoStepReqDTO autoStepReqDTO) {
        if (autoStepReqDTO.getDataSize() == 0) return this::noDataStrategy;
        if ((autoStepReqDTO.getDataSize() < initDataHandleStandard() &&
                autoStepReqDTO.getDataSize() > initDataHandleStandard() * 0.75)) return this::nearStandardStrategy;
        if ((autoStepReqDTO.getDataSize() / initDataHandleStandard()) + 1 <= 1) return this::lessStandardStrategy;
        return this::greaterStandardStrategy;

    }

    /**
     * 无数据 策略方法
     * 没数据：步长指数增长
     */
    private AutoStepResultDTO noDataStrategy(AutoStepReqDTO autoStepReqDTO) {
        log.info("没有数据的时候，步长快增长：指数增长");
        return new AutoStepResultDTO(autoStepReqDTO.getStep() * 2);
    }

    /**
     * 少量数据 策略方法
     * 数据较少：步长缓慢增长
     */
    private AutoStepResultDTO lessStandardStrategy(AutoStepReqDTO autoStepReqDTO) {
        return new AutoStepResultDTO((long) (autoStepReqDTO.getStep() * 1.5));
    }

    /**
     * 临届数据 策略方法
     * 临届内：不调整
     */
    private AutoStepResultDTO nearStandardStrategy(AutoStepReqDTO autoStepReqDTO) {
        return new AutoStepResultDTO(autoStepReqDTO.getStep());
    }

    /**
     * 超量数据 策略方法
     * 超量：按数据量分多段重新调整步长
     */
    private AutoStepResultDTO greaterStandardStrategy(AutoStepReqDTO autoStepReqDTO) {
        Long datasize = autoStepReqDTO.getDataSize();
        long step = autoStepReqDTO.getStep();
        step /= ((datasize / initDataHandleStandard()) + 1);
        return new AutoStepResultDTO(true, step);
    }

}
