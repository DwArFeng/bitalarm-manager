package com.dwarfeng.bitalarmmanager.node.controller.v1;

import com.dwarfeng.bitalarm.sdk.bean.entity.FastJsonAlarmTypeIndicator;
import com.dwarfeng.bitalarm.sdk.bean.entity.WebInputAlarmTypeIndicator;
import com.dwarfeng.bitalarm.stack.bean.entity.AlarmTypeIndicator;
import com.dwarfeng.bitalarmmanager.stack.service.AlarmTypeIndicatorResponseService;
import com.dwarfeng.subgrade.sdk.bean.dto.FastJsonResponseData;
import com.dwarfeng.subgrade.sdk.bean.dto.JSFixedFastJsonPagedData;
import com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil;
import com.dwarfeng.subgrade.sdk.bean.dto.ResponseDataUtil;
import com.dwarfeng.subgrade.sdk.bean.key.FastJsonByteIdKey;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.sdk.interceptor.http.BindingCheck;
import com.dwarfeng.subgrade.sdk.validation.group.Insert;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.ByteIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 报警类型指示器控制器。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1")
public class AlarmTypeIndicatorController {

    @Autowired
    private AlarmTypeIndicatorResponseService service;
    @Autowired
    private ServiceExceptionMapper sem;

    @Autowired
    private BeanTransformer<AlarmTypeIndicator, FastJsonAlarmTypeIndicator> beanTransformer;

    @GetMapping("/alarm-type-indicator/{id}/exists")
    @BehaviorAnalyse
    public FastJsonResponseData<Boolean> exists(HttpServletRequest request, @PathVariable("id") byte id) {
        try {
            boolean exists = service.exists(new ByteIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(exists));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Boolean.class, e, sem));
        }
    }

    @GetMapping("/alarm-type-indicator/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<FastJsonAlarmTypeIndicator> get(HttpServletRequest request, @PathVariable("id") byte id) {
        try {
            AlarmTypeIndicator alarmTypeIndicator = service.get(new ByteIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(FastJsonAlarmTypeIndicator.of(alarmTypeIndicator)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(FastJsonAlarmTypeIndicator.class, e, sem));
        }
    }

    @PostMapping("/alarm-type-indicator")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<FastJsonByteIdKey> insert(
            HttpServletRequest request,
            @RequestBody @Validated(Insert.class) WebInputAlarmTypeIndicator alarmTypeIndicator, BindingResult bindingResult) {
        try {
            ByteIdKey insert = service.insert(WebInputAlarmTypeIndicator.toStackBean(alarmTypeIndicator));
            return FastJsonResponseData.of(ResponseDataUtil.good(FastJsonByteIdKey.of(insert)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(FastJsonByteIdKey.class, e, sem));
        }
    }

    @PatchMapping("/alarm-type-indicator")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<Object> update(
            HttpServletRequest request,
            @RequestBody @Validated WebInputAlarmTypeIndicator alarmTypeIndicator, BindingResult bindingResult) {
        try {
            service.update(WebInputAlarmTypeIndicator.toStackBean(alarmTypeIndicator));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @DeleteMapping("/alarm-type-indicator/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<Object> delete(HttpServletRequest request, @PathVariable("id") byte id) {
        try {
            service.delete(new ByteIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @GetMapping("/alarm-type-indicator/all")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<FastJsonAlarmTypeIndicator>> all(
            HttpServletRequest request, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmTypeIndicator> all = service.all(new PagingInfo(page, rows));
            PagedData<FastJsonAlarmTypeIndicator> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }
}
