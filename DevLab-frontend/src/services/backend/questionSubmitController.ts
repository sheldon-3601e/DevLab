// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** doQuestionSubmit POST /api/question_submit/ */
export async function doQuestionSubmitUsingPost1(
  body: API.QuestionSubmitAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/question_submit/', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listQuestionSubmitByPage POST /api/question_submit/list/page */
export async function listQuestionSubmitByPageUsingPost1(
  body: API.QuestionSubmitQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestionSubmitVO_>('/api/question_submit/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
