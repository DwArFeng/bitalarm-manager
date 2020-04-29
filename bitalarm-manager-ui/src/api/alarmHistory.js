import {
  del, get, patch, post,
} from '../util/http';

export function exists(key) {
  return get(`/alarm-history/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/alarm-history/${key}`, {});
}

export function insert(
  key, alarmSettingKey, index, alarmMessage, alarmType, startDate, endDate, duration,
) {
  let finalKey = { long_id: key };
  if (key === '') {
    finalKey = null;
  }
  return post('/alarm-history', {
    key: finalKey,
    alarm_setting_key: { long_id: alarmSettingKey },
    index,
    alarm_message: alarmMessage,
    alarm_type: alarmType,
    start_date: startDate,
    end_date: endDate,
    duration,
  });
}

export function remove(key) {
  return del(`/alarm-history/${key}`, {});
}

export function update(
  key, alarmSettingKey, index, alarmMessage, alarmType, startDate, endDate, duration,
) {
  return patch('/alarm-history', {
    key: { long_id: key },
    alarm_setting_key: { long_id: alarmSettingKey },
    index,
    alarm_message: alarmMessage,
    alarm_type: alarmType,
    start_date: startDate,
    end_date: endDate,
    duration,
  });
}

export function all(page, rows) {
  return get('/alarm-history/all', {
    page,
    rows,
  });
}

export function alarmMessageLike(pattern, page, rows) {
  return get('/alarm-history/alarm-message-like', {
    pattern,
    page,
    rows,
  });
}

export function alarmTypeEquals(type, page, rows) {
  return get('/alarm-history/alarm-type-equals', {
    type,
    page,
    rows,
  });
}

export function startDateBetween(startDate, endDate, page, rows) {
  return get('/alarm-history/start-date-between', {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}

export function endDateBetween(startDate, endDate, page, rows) {
  return get('/alarm-history/end-date-between', {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}

export function durationGt(duration, page, rows) {
  return get('/alarm-history/duration-gt', {
    duration,
    page,
    rows,
  });
}

export function durationLt(duration, page, rows) {
  return get('/alarm-history/duration-lt', {
    duration,
    page,
    rows,
  });
}

export function childForAlarmSetting(alarmSetting, page, rows) {
  return get(`/alarm-setting/${alarmSetting}/alarm-history`, {
    page,
    rows,
  });
}

export function childForAlarmSettingStartDateBetween(alarmSetting, startDate, endDate, page, rows) {
  return get(`/alarm-setting/${alarmSetting}/alarm-history/start-date-between`, {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}

export function childForAlarmSettingEndDateBetween(alarmSetting, startDate, endDate, page, rows) {
  return get(`/alarm-setting/${alarmSetting}/alarm-history/end-date-between`, {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}

export function childForAlarmSettingDurationGT(alarmSetting, duration, page, rows) {
  return get(`/alarm-setting/${alarmSetting}/alarm-history/duration-gt`, {
    duration,
    page,
    rows,
  });
}

export function childForAlarmSettingDurationLT(alarmSetting, duration, page, rows) {
  return get(`/alarm-setting/${alarmSetting}/alarm-history/duration-lt`, {
    duration,
    page,
    rows,
  });
}
