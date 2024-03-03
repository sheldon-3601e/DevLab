// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** listTagVOByTop POST /api/tag/list/top/vo */
export async function listTagVoByTopUsingPost(
  body: API.TagQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListTagVO_>('/api/tag/list/top/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listTagVO POST /api/tag/list/vo */
export async function listTagVoUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseListTagVO_>('/api/tag/list/vo', {
    method: 'POST',
    ...(options || {}),
  });
}
