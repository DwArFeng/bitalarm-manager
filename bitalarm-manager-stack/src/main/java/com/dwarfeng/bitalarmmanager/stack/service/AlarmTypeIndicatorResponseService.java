package com.dwarfeng.bitalarmmanager.stack.service;

import com.dwarfeng.bitalarm.stack.bean.entity.AlarmTypeIndicator;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.ByteIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;

/**
 * 报警类型指示器响应服务。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface AlarmTypeIndicatorResponseService extends Service {

    /**
     * 获取服务中是否有指定的报警类型指示器。
     *
     * @param key 指定的报警类型指示器的键。
     * @return 服务中是否有指定的报警类型指示器。
     * @throws ServiceException 服务异常。
     */
    boolean exists(ByteIdKey key) throws ServiceException;

    /**
     * 获取服务中指定的主键对应的报警类型指示器。
     *
     * @param key 指定的主键。
     * @return 指定的主键对应的报警类型指示器。
     * @throws ServiceException 服务异常。
     */
    AlarmTypeIndicator get(ByteIdKey key) throws ServiceException;

    /**
     * 向服务中插入指定的报警类型指示器。
     *
     * @param alarmTypeIndicator 指定的报警类型指示器。
     * @return 插入后分配的新主键。
     * @throws ServiceException 服务异常。
     */
    ByteIdKey insert(AlarmTypeIndicator alarmTypeIndicator) throws ServiceException;

    /**
     * 向服务中更新指定的报警类型指示器。
     *
     * @param alarmTypeIndicator 指定的报警类型指示器。
     * @throws ServiceException 服务异常。
     */
    void update(AlarmTypeIndicator alarmTypeIndicator) throws ServiceException;

    /**
     * 从服务中删除指定的主键对应的报警类型指示器。
     *
     * @param key 指定的主键。
     * @throws ServiceException 服务异常。
     */
    void delete(ByteIdKey key) throws ServiceException;

    /**
     * 从服务中获取所有的报警类型指示器。
     *
     * @param pagingInfo 分页信息。
     * @return 指定的数据点对应的报警类型指示器。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmTypeIndicator> all(PagingInfo pagingInfo) throws ServiceException;
}
