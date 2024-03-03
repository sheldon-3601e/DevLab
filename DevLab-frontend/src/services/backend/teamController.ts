// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addTeam POST /api/team/add */
export async function addTeamUsingPost(body: API.TeamAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong_>('/api/team/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteTeam POST /api/team/delete */
export async function deleteTeamUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/team/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** dissolveTeam POST /api/team/dissolve */
export async function dissolveTeamUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/team/dissolve', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** joinTeam POST /api/team/join */
export async function joinTeamUsingPost(
  body: API.TeamJoinRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/team/join', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listCreatedTeam POST /api/team/list/my/create */
export async function listCreatedTeamUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseListTeamUserVO_>('/api/team/list/my/create', {
    method: 'POST',
    ...(options || {}),
  });
}

/** listJoinedTeam POST /api/team/list/my/joined */
export async function listJoinedTeamUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseListTeamUserVO_>('/api/team/list/my/joined', {
    method: 'POST',
    ...(options || {}),
  });
}

/** listTeamUserVOByPage POST /api/team/list/page/vo */
export async function listTeamUserVoByPageUsingPost(
  body: API.TeamQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageTeamUserVO_>('/api/team/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** quitTeam POST /api/team/quit */
export async function quitTeamUsingPost(
  body: API.TeamQuitRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/team/quit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateTeam POST /api/team/update */
export async function updateTeamUsingPost(
  body: API.TeamUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/team/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
