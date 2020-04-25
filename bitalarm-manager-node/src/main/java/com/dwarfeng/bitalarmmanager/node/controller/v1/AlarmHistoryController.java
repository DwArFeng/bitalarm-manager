package com.dwarfeng.bitalarmmanager.node.controller.v1;

import com.dwarfeng.bitalarm.sdk.bean.entity.JSFixedFastJsonAlarmHistory;
import com.dwarfeng.bitalarm.sdk.bean.entity.WebInputAlarmHistory;
import com.dwarfeng.bitalarm.stack.bean.entity.AlarmHistory;
import com.dwarfeng.bitalarmmanager.stack.service.AlarmHistoryResponseService;
import com.dwarfeng.subgrade.sdk.bean.dto.FastJsonResponseData;
import com.dwarfeng.subgrade.sdk.bean.dto.JSFixedFastJsonPagedData;
import com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil;
import com.dwarfeng.subgrade.sdk.bean.dto.ResponseDataUtil;
import com.dwarfeng.subgrade.sdk.bean.key.JSFixedFastJsonLongIdKey;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.sdk.interceptor.http.BindingCheck;
import com.dwarfeng.subgrade.sdk.validation.group.Insert;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 报警历史控制器。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1")
public class AlarmHistoryController {

    @Autowired
    private AlarmHistoryResponseService service;
    @Autowired
    private ServiceExceptionMapper sem;

    @Autowired
    private BeanTransformer<AlarmHistory, JSFixedFastJsonAlarmHistory> beanTransformer;

    @GetMapping("/alarm-history/{id}/exists")
    @BehaviorAnalyse
    public FastJsonResponseData<Boolean> exists(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            boolean exists = service.exists(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(exists));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Boolean.class, e, sem));
        }
    }

    @GetMapping("/alarm-history/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonAlarmHistory> get(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            AlarmHistory alarmHistory = service.get(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonAlarmHistory.of(alarmHistory)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonAlarmHistory.class, e, sem));
        }
    }

    @PostMapping("/alarm-history")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<JSFixedFastJsonLongIdKey> insert(
            HttpServletRequest request,
            @RequestBody @Validated(Insert.class) WebInputAlarmHistory alarmHistory, BindingResult bindingResult) {
        try {
            LongIdKey insert = service.insert(WebInputAlarmHistory.toStackBean(alarmHistory));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonLongIdKey.of(insert)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonLongIdKey.class, e, sem));
        }
    }

    @PatchMapping("/alarm-history")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<Object> update(
            HttpServletRequest request,
            @RequestBody @Validated WebInputAlarmHistory alarmHistory, BindingResult bindingResult) {
        try {
            service.update(WebInputAlarmHistory.toStackBean(alarmHistory));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @DeleteMapping("/alarm-history/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<Object> delete(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            service.delete(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @GetMapping("/alarm-history/all")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> all(
            HttpServletRequest request, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> all = service.all(new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/alarm-history/alarm-message-like")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> alarmMessageLike(
            HttpServletRequest request,
            @RequestParam("pattern") String pattern, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> alarmMessageLike = service.alarmMessageLike(pattern, new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(alarmMessageLike, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/alarm-history/alarm-type-equals")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> alarmTypeEquals(
            HttpServletRequest request,
            @RequestParam("type") byte type, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> alarmTypeEquals = service.alarmTypeEquals(type, new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(alarmTypeEquals, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/alarm-history/start-date-between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> startDateBetween(
            HttpServletRequest request,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> startDateBetween = service.startDateBetween(
                    new Date(startDate), new Date(endDate), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(startDateBetween, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/alarm-history/end-date-between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> endDateBetween(
            HttpServletRequest request,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> endDateBetween = service.endDateBetween(
                    new Date(startDate), new Date(endDate), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(endDateBetween, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/alarm-history/duration-gt")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> durationGT(
            HttpServletRequest request,
            @RequestParam("duration") long duration, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> durationGT = service.durationGT(duration, new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(durationGT, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/alarm-history/duration-lt")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> durationLT(
            HttpServletRequest request,
            @RequestParam("duration") long duration, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> durationLT = service.durationLT(duration, new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(durationLT, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/alarm-setting/{alarmSettingId}/alarm-history")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> childForAlarmSetting(
            HttpServletRequest request,
            @PathVariable("alarmSettingId") long alarmSettingId,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> childForAlarmSetting = service.childForAlarmSetting(new LongIdKey(alarmSettingId), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(childForAlarmSetting, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/alarm-setting/{alarmSettingId}/alarm-history/start-date-between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> childForAlarmSettingStartDateBetween(
            HttpServletRequest request,
            @PathVariable("alarmSettingId") long alarmSettingId,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> childForAlarmSettingStartDateBetween = service.childForAlarmSettingStartDateBetween(
                    new LongIdKey(alarmSettingId), new Date(startDate), new Date(endDate), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(childForAlarmSettingStartDateBetween, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/alarm-setting/{alarmSettingId}/alarm-history/end-date-between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> childForAlarmSettingEndDateBetween(
            HttpServletRequest request,
            @PathVariable("alarmSettingId") long alarmSettingId,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> childForAlarmSettingEndDateBetween = service.childForAlarmSettingEndDateBetween(
                    new LongIdKey(alarmSettingId), new Date(startDate), new Date(endDate), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(childForAlarmSettingEndDateBetween, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/alarm-setting/{alarmSettingId}/alarm-history/duration-gt")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> childForAlarmSettingDurationGT(
            HttpServletRequest request,
            @PathVariable("alarmSettingId") long alarmSettingId,
            @RequestParam("duration") long duration, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> childForAlarmSettingDurationGT = service.childForAlarmSettingDurationGT(
                    new LongIdKey(alarmSettingId), duration, new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(childForAlarmSettingDurationGT, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/alarm-setting/{alarmSettingId}/alarm-history/duration-lt")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonAlarmHistory>> childForAlarmSettingDurationLT(
            HttpServletRequest request,
            @PathVariable("alarmSettingId") long alarmSettingId,
            @RequestParam("duration") long duration, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<AlarmHistory> childForAlarmSettingDurationLT = service.childForAlarmSettingDurationLT(
                    new LongIdKey(alarmSettingId), duration, new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonAlarmHistory> transform = PagingUtil.transform(childForAlarmSettingDurationLT, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }
}
