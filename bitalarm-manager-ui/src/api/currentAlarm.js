import {
  del, get, patch, post,
} from '../util/http';

export function exists(key) {
  return get(`/current-alarm/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/current-alarm/${key}`, {});
}

export function insert(key, index, alarmMessage, alarmType, happenedDate) {
  let finalKey = { long_id: key };
  if (key === '') {
    finalKey = null;
  }
  return post('/current-alarm', {
    key: finalKey,
    index,
    alarm_message: alarmMessage,
    alarm_type: alarmType,
    happened_date: happenedDate,
  });
}

export function remove(key) {
  return del(`/current-alarm/${key}`, {});
}

export function update(key, index, alarmMessage, alarmType, happenedDate) {
  return patch('/current-alarm', {
    key: { long_id: key },
    index,
    alarm_message: alarmMessage,
    alarm_type: alarmType,
    happened_date: happenedDate,
  });
}

export function all(page, rows) {
  return get('/current-alarm/all', {
    page,
    rows,
  });
}
