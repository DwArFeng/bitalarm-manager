import {
  del, get, patch, post,
} from '../util/http';

export function exists(key) {
  return get(`/alarm-setting/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/alarm-setting/${key}`, {});
}

export function insert(key, pointId, enabled, index, alarmMessage, alarmType, remark) {
  let finalKey = { long_id: key };
  if (key === '') {
    finalKey = null;
  }
  return post('/alarm-setting', {
    key: finalKey,
    point_id: pointId,
    enabled,
    index,
    alarm_message: alarmMessage,
    alarm_type: alarmType,
    remark,
  });
}

export function remove(key) {
  return del(`/alarm-setting/${key}`, {});
}

export function update(key, pointId, enabled, index, alarmMessage, alarmType, remark) {
  return patch('/alarm-setting', {
    key: {
      long_id: key,
    },
    point_id: pointId,
    enabled,
    index,
    alarm_message: alarmMessage,
    alarm_type: alarmType,
    remark,
  });
}

export function all(page, rows) {
  return get('/alarm-setting/all', {
    page,
    rows,
  });
}

export function childForPoint(pointId, page, rows) {
  return get(`/point/${pointId}/alarm-setting`, {
    page,
    rows,
  });
}

export function alarmMessageLike(pattern, page, rows) {
  return get('/alarm-setting/alarm-message-like', {
    pattern,
    page,
    rows,
  });
}

export function alarmTypeEquals(type, page, rows) {
  return get('/alarm-setting/alarm-type-equals', {
    type,
    page,
    rows,
  });
}
