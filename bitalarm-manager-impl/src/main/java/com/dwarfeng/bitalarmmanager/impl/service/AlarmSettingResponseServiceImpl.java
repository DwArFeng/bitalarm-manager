package com.dwarfeng.bitalarmmanager.impl.service;

import com.dwarfeng.bitalarm.stack.bean.entity.AlarmSetting;
import com.dwarfeng.bitalarm.stack.service.AlarmSettingMaintainService;
import com.dwarfeng.bitalarmmanager.stack.service.AlarmSettingResponseService;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AlarmSettingResponseServiceImpl implements AlarmSettingResponseService {

    @Autowired
    @Qualifier("alarmSettingMaintainService")
    private AlarmSettingMaintainService service;

    @Override
    public boolean exists(LongIdKey key) throws ServiceException {
        return service.exists(key);
    }

    @Override
    public AlarmSetting get(LongIdKey key) throws ServiceException {
        return service.get(key);
    }

    @Override
    public LongIdKey insert(AlarmSetting alarmSetting) throws ServiceException {
        return service.insert(alarmSetting);
    }

    @Override
    public void update(AlarmSetting alarmSetting) throws ServiceException {
        service.update(alarmSetting);
    }

    @Override
    public void delete(LongIdKey key) throws ServiceException {
        service.delete(key);
    }

    @Override
    public PagedData<AlarmSetting> all(PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(pagingInfo);
    }

    @Override
    public PagedData<AlarmSetting> childForPoint(long pointId, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(AlarmSettingMaintainService.CHILD_FOR_POINT, new Object[]{pointId}, pagingInfo);
    }

    @Override
    public PagedData<AlarmSetting> alarmMessageLike(String pattern, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(AlarmSettingMaintainService.ALARM_MESSAGE_LIKE, new Object[]{pattern}, pagingInfo);
    }

    @Override
    public PagedData<AlarmSetting> alarmTypeEquals(byte type, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(AlarmSettingMaintainService.ALARM_TYPE_EQUALS, new Object[]{type}, pagingInfo);
    }
}
