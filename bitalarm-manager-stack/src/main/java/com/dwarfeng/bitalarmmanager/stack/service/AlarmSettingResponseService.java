package com.dwarfeng.bitalarmmanager.stack.service;

import com.dwarfeng.bitalarm.stack.bean.entity.AlarmSetting;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;

/**
 * 报警设置响应服务。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface AlarmSettingResponseService extends Service {

    /**
     * 获取服务中是否有指定的报警设置。
     *
     * @param key 指定的报警设置的键。
     * @return 服务中是否有指定的报警设置。
     * @throws ServiceException 服务异常。
     */
    boolean exists(LongIdKey key) throws ServiceException;

    /**
     * 获取服务中指定的主键对应的报警设置。
     *
     * @param key 指定的主键。
     * @return 指定的主键对应的报警设置。
     * @throws ServiceException 服务异常。
     */
    AlarmSetting get(LongIdKey key) throws ServiceException;

    /**
     * 向服务中插入指定的报警设置。
     *
     * @param alarmSetting 指定的报警设置。
     * @return 插入后分配的新主键。
     * @throws ServiceException 服务异常。
     */
    LongIdKey insert(AlarmSetting alarmSetting) throws ServiceException;

    /**
     * 向服务中更新指定的报警设置。
     *
     * @param alarmSetting 指定的报警设置。
     * @throws ServiceException 服务异常。
     */
    void update(AlarmSetting alarmSetting) throws ServiceException;

    /**
     * 从服务中删除指定的主键对应的报警设置。
     *
     * @param key 指定的主键。
     * @throws ServiceException 服务异常。
     */
    void delete(LongIdKey key) throws ServiceException;

    /**
     * 获取所有报警设置。
     *
     * @param pagingInfo 分页信息。
     * @return 所有报警设置构成的分页数据。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmSetting> all(PagingInfo pagingInfo) throws ServiceException;

    /**
     * 获取服务中属于指定数据点的所有报警信息。
     *
     * @param pointId    指定的数据点。
     * @param pagingInfo 分页信息。
     * @return 服务中属于指定数据点的所有报警信息。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmSetting> childForPoint(long pointId, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 获取服务中报警信息中含有指定片段的所有报警设置。
     *
     * @param pattern    指定的片段
     * @param pagingInfo 分页信息。
     * @return 报警信息中含有指定片段的所有报警设置。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmSetting> alarmMessageLike(String pattern, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 获取服务中报警类型为指定值的所有报警设置。
     *
     * @param type       指定的类型。
     * @param pagingInfo 分页信息。
     * @return 报警类型为指定值的所有报警设置。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmSetting> alarmTypeEquals(byte type, PagingInfo pagingInfo) throws ServiceException;
}
