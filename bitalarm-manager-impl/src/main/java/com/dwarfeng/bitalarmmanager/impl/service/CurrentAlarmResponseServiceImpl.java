package com.dwarfeng.bitalarmmanager.impl.service;

import com.dwarfeng.bitalarm.stack.bean.entity.CurrentAlarm;
import com.dwarfeng.bitalarm.stack.service.CurrentAlarmMaintainService;
import com.dwarfeng.bitalarmmanager.stack.service.CurrentAlarmResponseService;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class CurrentAlarmResponseServiceImpl implements CurrentAlarmResponseService {

    @Autowired
    @Qualifier("currentAlarmMaintainService")
    private CurrentAlarmMaintainService service;

    @Override
    public boolean exists(LongIdKey key) throws ServiceException {
        return service.exists(key);
    }

    @Override
    public CurrentAlarm get(LongIdKey key) throws ServiceException {
        return service.get(key);
    }

    @Override
    public LongIdKey insert(CurrentAlarm currentAlarm) throws ServiceException {
        return service.insert(currentAlarm);
    }

    @Override
    public void update(CurrentAlarm currentAlarm) throws ServiceException {
        service.update(currentAlarm);
    }

    @Override
    public void delete(LongIdKey key) throws ServiceException {
        service.delete(key);
    }

    @Override
    public PagedData<CurrentAlarm> all(PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(pagingInfo);
    }
}
