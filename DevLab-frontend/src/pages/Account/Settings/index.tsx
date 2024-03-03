import { PageContainer, ProCard } from '@ant-design/pro-components'; // 引入 GridContent 组件
import React from 'react'; // 引入 React 相关的库
import BaseView from './components/base'; // 引入名为 BaseView 的组件

// 定义 Settings 组件
const Settings: React.FC = () => {
  return (
    <PageContainer>
      <ProCard layout={'center'}>
        <BaseView />
      </ProCard>
    </PageContainer>
  );
};

export default Settings; // 导出 Settings 组件
