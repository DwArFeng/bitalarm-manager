package com.dwarfeng.bitalarmmanager.impl.service;

import com.dwarfeng.bitalarm.stack.bean.entity.AlarmHistory;
import com.dwarfeng.bitalarm.stack.service.AlarmHistoryMaintainService;
import com.dwarfeng.bitalarmmanager.stack.service.AlarmHistoryResponseService;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AlarmHistoryResponseServiceImpl implements AlarmHistoryResponseService {

    @Autowired
    @Qualifier("alarmHistoryMaintainService")
    private AlarmHistoryMaintainService service;

    @Override
    public boolean exists(LongIdKey key) throws ServiceException {
        return service.exists(key);
    }

    @Override
    public AlarmHistory get(LongIdKey key) throws ServiceException {
        return service.get(key);
    }

    @Override
    public LongIdKey insert(AlarmHistory alarmHistory) throws ServiceException {
        return service.insert(alarmHistory);
    }

    @Override
    public void update(AlarmHistory alarmHistory) throws ServiceException {
        service.update(alarmHistory);
    }

    @Override
    public void delete(LongIdKey key) throws ServiceException {
        service.delete(key);
    }

    @Override
    public PagedData<AlarmHistory> all(PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(pagingInfo);
    }

    @Override
    public PagedData<AlarmHistory> alarmMessageLike(String pattern, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(AlarmHistoryMaintainService.ALARM_MESSAGE_LIKE, new Object[]{pattern}, pagingInfo);
    }

    @Override
    public PagedData<AlarmHistory> alarmTypeEquals(byte type, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(AlarmHistoryMaintainService.ALARM_TYPE_EQUALS, new Object[]{type}, pagingInfo);
    }

    @Override
    public PagedData<AlarmHistory> startDateBetween(
            Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                AlarmHistoryMaintainService.START_DATE_BETWEEN, new Object[]{startDate, endDate}, pagingInfo);
    }

    @Override
    public PagedData<AlarmHistory> endDateBetween(
            Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                AlarmHistoryMaintainService.END_DATE_BETWEEN, new Object[]{startDate, endDate}, pagingInfo);
    }

    @Override
    public PagedData<AlarmHistory> durationGT(long duration, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(AlarmHistoryMaintainService.DURATION_GT, new Object[]{duration}, pagingInfo);
    }

    @Override
    public PagedData<AlarmHistory> durationLT(long duration, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(AlarmHistoryMaintainService.DURATION_LT, new Object[]{duration}, pagingInfo);
    }

    @Override
    public PagedData<AlarmHistory> childForAlarmSetting(
            LongIdKey alarmSettingKey, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                AlarmHistoryMaintainService.CHILD_FOR_ALARM_SETTING, new Object[]{alarmSettingKey}, pagingInfo);
    }

    @Override
    public PagedData<AlarmHistory> childForAlarmSettingStartDateBetween(
            LongIdKey alarmSettingKey, Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(AlarmHistoryMaintainService.CHILD_FOR_ALARM_SETTING_START_DATE_BETWEEN,
                new Object[]{alarmSettingKey, startDate, endDate}, pagingInfo);
    }

    @Override
    public PagedData<AlarmHistory> childForAlarmSettingEndDateBetween(
            LongIdKey alarmSettingKey, Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(AlarmHistoryMaintainService.CHILD_FOR_ALARM_SETTING_END_DATE_BETWEEN,
                new Object[]{alarmSettingKey, startDate, endDate}, pagingInfo);
    }

    @Override
    public PagedData<AlarmHistory> childForAlarmSettingDurationGT(
            LongIdKey alarmSettingKey, long duration, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(AlarmHistoryMaintainService.CHILD_FOR_ALARM_SETTING_DURATION_GT,
                new Object[]{alarmSettingKey, duration}, pagingInfo);
    }

    @Override
    public PagedData<AlarmHistory> childForAlarmSettingDurationLT(
            LongIdKey alarmSettingKey, long duration, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(AlarmHistoryMaintainService.CHILD_FOR_ALARM_SETTING_DURATION_LT,
                new Object[]{alarmSettingKey, duration}, pagingInfo);
    }
}
