package com.dwarfeng.bitalarmmanager.stack.service;

import com.dwarfeng.bitalarm.stack.bean.entity.CurrentAlarm;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;

/**
 * 当前报警响应服务。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface CurrentAlarmResponseService extends Service {

    /**
     * 获取服务中是否有指定的当前报警。
     *
     * @param key 指定的当前报警的键。
     * @return 服务中是否有指定的当前报警。
     * @throws ServiceException 服务异常。
     */
    boolean exists(LongIdKey key) throws ServiceException;

    /**
     * 获取服务中指定的主键对应的当前报警。
     *
     * @param key 指定的主键。
     * @return 指定的主键对应的当前报警。
     * @throws ServiceException 服务异常。
     */
    CurrentAlarm get(LongIdKey key) throws ServiceException;

    /**
     * 向服务中插入指定的当前报警。
     *
     * @param currentAlarm 指定的当前报警。
     * @return 插入后分配的新主键。
     * @throws ServiceException 服务异常。
     */
    LongIdKey insert(CurrentAlarm currentAlarm) throws ServiceException;

    /**
     * 向服务中更新指定的当前报警。
     *
     * @param currentAlarm 指定的当前报警。
     * @throws ServiceException 服务异常。
     */
    void update(CurrentAlarm currentAlarm) throws ServiceException;

    /**
     * 从服务中删除指定的主键对应的当前报警。
     *
     * @param key 指定的主键。
     * @throws ServiceException 服务异常。
     */
    void delete(LongIdKey key) throws ServiceException;

    /**
     * 从服务中获取所有的当前报警。
     *
     * @param pagingInfo 分页信息。
     * @return 指定的数据点对应的当前报警。
     * @throws ServiceException 服务异常。
     */
    PagedData<CurrentAlarm> all(PagingInfo pagingInfo) throws ServiceException;
}
