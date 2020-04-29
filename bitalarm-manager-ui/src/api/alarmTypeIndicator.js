import {
  del, get, patch, post,
} from '../util/http';

export function exists(key) {
  return get(`/alarm-type-indicator/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/alarm-type-indicator/${key}`, {});
}

export function insert(key, label) {
  let finalKey = { byte_id: key };
  if (key === '') {
    finalKey = null;
  }
  return post('/alarm-type-indicator', {
    key: finalKey,
    label,
  });
}

export function remove(key) {
  return del(`/alarm-type-indicator/${key}`, {});
}

export function update(key, label) {
  return patch('/alarm-type-indicator', {
    key: { byte_id: key },
    label,
  });
}

export function all(page, rows) {
  return get('/alarm-type-indicator/all', {
    page,
    rows,
  });
}
