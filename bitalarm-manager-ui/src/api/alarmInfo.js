import {
  del, get, patch, post,
} from '../util/http';

export function exists(key) {
  return get(`/alarm-info/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/alarm-info/${key}`, {});
}

export function insert(
  key, index, alarmMessage, alarmType, happenedDate, alarming,
) {
  let finalKey = { long_id: key };
  if (key === '') {
    finalKey = null;
  }
  return post('/alarm-info', {
    key: finalKey,
    index,
    alarm_message: alarmMessage,
    alarm_type: alarmType,
    happened_date: happenedDate,
    alarming,
  });
}

export function remove(key) {
  return del(`/alarm-info/${key}`, {});
}

export function update(
  key, index, alarmMessage, alarmType, happenedDate, alarming,
) {
  return patch('/alarm-info', {
    key: { long_id: key },
    index,
    alarm_message: alarmMessage,
    alarm_type: alarmType,
    happened_date: happenedDate,
    alarming,
  });
}

export function all(page, rows) {
  return get('/alarm-info/all', {
    page,
    rows,
  });
}
