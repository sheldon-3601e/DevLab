import Footer from '@/components/Footer';
import type { RunTimeLayoutConfig } from '@umijs/max';
import { history } from '@umijs/max';
import defaultSettings from '../config/defaultSettings';
import { AvatarDropdown } from './components/RightContent/AvatarDropdown';
import { requestConfig } from './requestConfig';
import { getLoginUserUsingGet } from '@/services/backend/userController';
import { listTagVoUsingPost } from '@/services/backend/tagController';
import { message } from 'antd';

const loginPath = '/user/login';
const welcomePath = '/welcome';

export async function getInitialState(): Promise<InitialState> {

  const initialState: InitialState = {
    currentUser: undefined,
    tagList: [],
  };
  const tagRes = await listTagVoUsingPost()
  if (tagRes.data) {
    initialState.tagList = tagRes.data
  }
  // 如果不是登录页面，执行
  const { location } = history;
  if (location.pathname !== loginPath && location.pathname !== welcomePath) {
    try {
      // 获取当前的登录用户
      const result = await getLoginUserUsingGet();
      if (result.data) {
        // 用户已经登录
        initialState.currentUser = result.data;
      }
    } catch (error: any) {
      // 如果未登录
      message.error('系统错误:', error)
    }
  }
  return initialState;
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
// @ts-ignore
export const layout: RunTimeLayoutConfig = ({ initialState }) => {
  return {
    avatarProps: {
      render: () => {
        return <AvatarDropdown />;
      },
    },
    waterMarkProps: {
      content: initialState?.currentUser?.userName,
    },
    footerRender: () => <Footer />,
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    ...defaultSettings,
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request = requestConfig;
