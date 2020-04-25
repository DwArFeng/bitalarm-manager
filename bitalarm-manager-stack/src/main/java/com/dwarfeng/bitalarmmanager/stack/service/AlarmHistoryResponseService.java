package com.dwarfeng.bitalarmmanager.stack.service;

import com.dwarfeng.bitalarm.stack.bean.entity.AlarmHistory;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;

import java.util.Date;

/**
 * 报警历史响应服务。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface AlarmHistoryResponseService extends Service {

    /**
     * 获取服务中是否有指定的报警历史。
     *
     * @param key 指定的报警历史的键。
     * @return 服务中是否有指定的报警历史。
     * @throws ServiceException 服务异常。
     */
    boolean exists(LongIdKey key) throws ServiceException;

    /**
     * 获取服务中指定的主键对应的报警历史。
     *
     * @param key 指定的主键。
     * @return 指定的主键对应的报警历史。
     * @throws ServiceException 服务异常。
     */
    AlarmHistory get(LongIdKey key) throws ServiceException;

    /**
     * 向服务中插入指定的报警历史。
     *
     * @param alarmHistory 指定的报警历史。
     * @return 插入后分配的新主键。
     * @throws ServiceException 服务异常。
     */
    LongIdKey insert(AlarmHistory alarmHistory) throws ServiceException;

    /**
     * 向服务中更新指定的报警历史。
     *
     * @param alarmHistory 指定的报警历史。
     * @throws ServiceException 服务异常。
     */
    void update(AlarmHistory alarmHistory) throws ServiceException;

    /**
     * 从服务中删除指定的主键对应的报警历史。
     *
     * @param key 指定的主键。
     * @throws ServiceException 服务异常。
     */
    void delete(LongIdKey key) throws ServiceException;

    /**
     * 从服务中获取所有的报警历史。
     *
     * @param pagingInfo 分页信息。
     * @return 指定的数据点对应的报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> all(PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取报警信息包含指定片段的报警历史。
     *
     * @param pattern    指定的片段。
     * @param pagingInfo 分页信息。
     * @return 报警信息包含指定片段的报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> alarmMessageLike(String pattern, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取指定报警类型的报警历史。
     *
     * @param type       指定的报警类型。
     * @param pagingInfo 分页信息。
     * @return 指定报警类型的报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> alarmTypeEquals(byte type, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取报警开始时间介于指定区间的报警历史。
     *
     * @param startDate  指定区间的开始日期。
     * @param endDate    指定区间的结束日期。
     * @param pagingInfo 分页信息。
     * @return 报警开始时间介于指定区间的报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> startDateBetween(
            Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取报警结束时间介于指定区间的报警历史。
     *
     * @param startDate  指定区间的开始日期。
     * @param endDate    指定区间的结束日期。
     * @param pagingInfo 分页信息。
     * @return 报警结束时间介于指定区间的报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> endDateBetween(
            Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取报警持续时间高于指定值的报警历史。
     *
     * @param duration   指定的值。
     * @param pagingInfo 分页信息。
     * @return 报警持续时间高于指定值的报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> durationGT(long duration, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取报警持续时间低于指定值的报警历史。
     *
     * @param duration   指定的值。
     * @param pagingInfo 分页信息。
     * @return 报警持续时间低于指定值的报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> durationLT(long duration, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取属于指定报警设置的所有报警历史。
     *
     * @param alarmSettingKey 指定的报警设置。
     * @param pagingInfo      分页信息。
     * @return 属于指定报警设置的所有报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> childForAlarmSetting(
            LongIdKey alarmSettingKey, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取属于指定报警设置且报警的开始时间介于指定区间的所有报警历史。
     *
     * @param alarmSettingKey 指定的报警设置。
     * @param startDate       指定区间的开始日期。
     * @param endDate         指定区间的结束日期。
     * @param pagingInfo      分页信息。
     * @return 属于指定报警设置且报警的开始时间介于指定区间的所有报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> childForAlarmSettingStartDateBetween(
            LongIdKey alarmSettingKey, Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取属于指定报警设置且报警的结束时间介于指定区间的所有报警历史。
     *
     * @param alarmSettingKey 指定的报警设置。
     * @param startDate       指定区间的开始日期。
     * @param endDate         指定区间的结束日期。
     * @param pagingInfo      分页信息。
     * @return 属于指定报警设置且报警的结束时间介于指定区间的所有报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> childForAlarmSettingEndDateBetween(
            LongIdKey alarmSettingKey, Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取属于指定报警设置且报警持续时间高于指定值的报警历史。
     *
     * @param alarmSettingKey 指定的报警设置。
     * @param duration        指定的值。
     * @param pagingInfo      分页信息。
     * @return 属于指定报警设置且报警持续时间高于指定值的报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> childForAlarmSettingDurationGT(
            LongIdKey alarmSettingKey, long duration, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取属于指定报警设置且报警持续时间低于指定值的报警历史。
     *
     * @param alarmSettingKey 指定的报警设置。
     * @param duration        指定的值。
     * @param pagingInfo      分页信息。
     * @return 属于指定报警设置且报警持续时间低于指定值的报警历史。
     * @throws ServiceException 服务异常。
     */
    PagedData<AlarmHistory> childForAlarmSettingDurationLT(
            LongIdKey alarmSettingKey, long duration, PagingInfo pagingInfo) throws ServiceException;
}
