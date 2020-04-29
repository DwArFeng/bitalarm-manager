package com.dwarfeng.bitalarmmanager.impl.service;

import com.dwarfeng.bitalarm.stack.bean.entity.AlarmTypeIndicator;
import com.dwarfeng.bitalarm.stack.service.AlarmTypeIndicatorMaintainService;
import com.dwarfeng.bitalarmmanager.stack.service.AlarmTypeIndicatorResponseService;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.ByteIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AlarmTypeIndicatorResponseServiceImpl implements AlarmTypeIndicatorResponseService {

    @Autowired
    @Qualifier("alarmTypeIndicatorMaintainService")
    private AlarmTypeIndicatorMaintainService service;

    @Override
    public boolean exists(ByteIdKey key) throws ServiceException {
        return service.exists(key);
    }

    @Override
    public AlarmTypeIndicator get(ByteIdKey key) throws ServiceException {
        return service.get(key);
    }

    @Override
    public ByteIdKey insert(AlarmTypeIndicator alarmTypeIndicator) throws ServiceException {
        return service.insert(alarmTypeIndicator);
    }

    @Override
    public void update(AlarmTypeIndicator alarmTypeIndicator) throws ServiceException {
        service.update(alarmTypeIndicator);
    }

    @Override
    public void delete(ByteIdKey key) throws ServiceException {
        service.delete(key);
    }

    @Override
    public PagedData<AlarmTypeIndicator> all(PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(pagingInfo);
    }
}
