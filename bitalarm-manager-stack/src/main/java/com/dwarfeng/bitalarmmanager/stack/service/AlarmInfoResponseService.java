package com.dwarfeng.bitalarmmanager.stack.service;

import com.dwarfeng.bitalarm.stack.bean.entity.AlarmInfo;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;

/**
 * 报警信息响应服务。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface AlarmInfoResponseService extends Service {

    /**
     * 获取服务中是否有指定的报警信息。
     *
     * @param key 指定的报警信息的键。
     * @return 服务中是否有指定的报警信息。
     * @throws ServiceException 服务异常。
     */
    boolean exists(LongIdKey key) throws ServiceException;

    /**
     * 获取服务中指定的主键对应的报警信息。
     *
     * @param key 指定的主键。
     * @return 指定的主键对应的报警信息。
     * @throws ServiceException 服务异常。
     */
    AlarmInfo get(LongIdKey key) throws ServiceException;

    /**
     * 向服务中插入指定的报警信息。
     *
     * @param alarmInfo 指定的报警信息。
     * @return 插入后分配的新主键。
     * @throws ServiceException 服务异常。
     */
    LongIdKey insert(AlarmInfo alarmInfo) throws ServiceException;

    /**
     * 向服务中更新指定的报警信息。
     *
     * @param alarmInfo 指定的报警信息。
     * @throws ServiceException 服务异常。
     */
    void update(AlarmInfo alarmInfo) throws ServiceException;

    /**
     * 从服务中删除指定的主键对应的报警信息。
     *
     * @param key 指定的主键。
     * @throws ServiceException 服务异常。
     */
    void delete(LongIdKey key) throws ServiceException;

    /**
     * 从服务中获取所有的报警信息。
     *
     * @param pagingInfo 分页信息。
     * @return 指定的数据点对应的报警信息。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmInfo> all(PagingInfo pagingInfo) throws ServiceException;
}
