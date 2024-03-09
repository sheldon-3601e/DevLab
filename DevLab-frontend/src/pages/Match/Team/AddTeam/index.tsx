import MargBottom16 from '@/components/margBottom16';
import { addTeamUsingPost } from '@/services/backend/teamController';
import {
  PageContainer,
  ProCard,
  ProForm,
  ProFormDatePicker,
  ProFormDigit,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import '@umijs/max';
import { message } from 'antd';
import dayjs from 'dayjs';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const CreateModal: React.FC = () => {
  const navigate = useNavigate();
  const [teamType, setTeamType] = useState<string>('0');

  /**
   * 添加节点
   * @param value
   */
  const handleAdd = async (value: API.TeamAddRequest) => {
    const hide = message.loading('正在添加');
    try {
      const result = await addTeamUsingPost(value);
      hide();
      if (result.data) {
        message.success('创建成功');
        // 跳转页面
        navigate('/match/team');
      }
    } catch (error: any) {
      hide();
      message.error('创建失败，' + error.message);
    }
  };

  return (
    <PageContainer title={'创建队伍'}>
      <ProCard>
        <ProForm<API.TeamAddRequest>
          style={{ padding: '10px' }}
          layout={'horizontal'}
          onFinish={async (values) => handleAdd(values)}
          autoFocusFirstInput
        >
          <ProForm.Group>
            <ProFormText
              width="md"
              name="teamName"
              label="队伍名称"
              placeholder="请输入队伍名称"
              rules={[{ required: true, message: '这是必填项' }]}
            />
          </ProForm.Group>
          <MargBottom16 />
          <ProFormTextArea
            name="description"
            label="队伍描述"
            allowClear
            width={'lg'}
            rules={[{ required: true, message: '这是必填项' }]}
          />
          <MargBottom16 />
          <ProForm.Group>
            <ProFormDigit
              label="最大人数"
              name="maxNum"
              max={10}
              min={3}
              initialValue={5}
              width={'md'}
            />
          </ProForm.Group>
          <ProFormDatePicker
            label={'过期时间'}
            name="expireTime"
            width={'md'}
            transform={(value) => {
              return {
                expireTime: dayjs(value).toDate(),
              };
            }}
            rules={[
              {
                validator: (_, value) => {
                  return value > dayjs(new Date()).add(1)
                    ? Promise.resolve()
                    : Promise.reject(new Error('过期时间应当大于当前时间'));
                },
                // value ? Promise.resolve() : Promise.reject(new Error('Should accept agreement')),
              },
            ]}
          />
          <MargBottom16 />
          <ProForm.Group>
            <ProFormSelect<string>
              name="status"
              label="队伍类别"
              valueEnum={{
                0: '公开',
                1: '私有',
                2: '加密',
              }}
              placeholder="请选择队伍类别"
              rules={[{ required: true, message: '请选择队伍类别!' }]}
              onChange={(value) => {
                setTeamType(value);
              }}
            />
          </ProForm.Group>
          <ProForm.Group>
            <ProFormText.Password
              label="队伍密码"
              name="password"
              // 只有当队伍类别不为公开时才渲染密码框组件
              hidden={teamType === '0'}
            />
          </ProForm.Group>
        </ProForm>
      </ProCard>
    </PageContainer>
  );
};
export default CreateModal;
