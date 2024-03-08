import MargBottom16 from '@/components/margBottom16';
import MyMdEditor from '@/components/MyMdEditor';
import { TAG_LIST } from '@/constants/TagConstants';
import {
  PageContainer,
  ProCard,
  ProForm,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Form, message } from 'antd';
import React from 'react';
import { useNavigate } from 'react-router-dom';
import { addPostUsingPost } from '@/services/backend/postController';

const AddPost: React.FC = () => {
  const navigate = useNavigate();

  //添加节点
  const handleAdd = async (value: API.PostAddRequest) => {
    console.log(value);
    const result = await addPostUsingPost({
      ...value,
    });
    if (result.data) {
      message.success('添加成功');
      navigate('/post/list');
    }
  };

  return (
    <div id={'add_post'}>
      <PageContainer title={'发布文章'}>
        <ProCard>
          <ProForm<API.PostAddRequest>
            style={{ padding: '10px' }}
            layout={'vertical'}
            onFinish={async (values) => handleAdd(values)}
            autoFocusFirstInput
          >
            <ProFormText
              label={'标题'}
              width="md"
              name="title"
              placeholder="请输入标题"
              rules={[{ required: true, message: '这是必填项' }]}
            />
            <ProFormSelect
              label={'标签'}
              name={'tags'}
              width={'lg'}
              mode={'multiple'}
              options={TAG_LIST}
            />
            <ProFormTextArea
              label={'概述'}
              width="lg"
              name="description"
              placeholder="请输入概述"
              rules={[{ required: true, message: '这是必填项' }]}
            />
            <MargBottom16 />
            <Form.Item label={'内容'} name={'content'}>
              <MyMdEditor />
            </Form.Item>
            <MargBottom16 />
          </ProForm>
        </ProCard>
      </PageContainer>
    </div>
  );
};
export default AddPost;
