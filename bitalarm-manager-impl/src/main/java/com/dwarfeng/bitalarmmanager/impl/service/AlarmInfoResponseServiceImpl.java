package com.dwarfeng.bitalarmmanager.impl.service;

import com.dwarfeng.bitalarm.stack.bean.entity.AlarmInfo;
import com.dwarfeng.bitalarm.stack.service.AlarmInfoMaintainService;
import com.dwarfeng.bitalarmmanager.stack.service.AlarmInfoResponseService;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AlarmInfoResponseServiceImpl implements AlarmInfoResponseService {

    @Autowired
    @Qualifier("alarmInfoMaintainService")
    private AlarmInfoMaintainService service;

    @Override
    public boolean exists(LongIdKey key) throws ServiceException {
        return service.exists(key);
    }

    @Override
    public AlarmInfo get(LongIdKey key) throws ServiceException {
        return service.get(key);
    }

    @Override
    public LongIdKey insert(AlarmInfo alarmInfo) throws ServiceException {
        return service.insert(alarmInfo);
    }

    @Override
    public void update(AlarmInfo alarmInfo) throws ServiceException {
        service.update(alarmInfo);
    }

    @Override
    public void delete(LongIdKey key) throws ServiceException {
        service.delete(key);
    }

    @Override
    public PagedData<AlarmInfo> all(PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(pagingInfo);
    }
}
