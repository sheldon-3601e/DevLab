import CodeEditor from '@/components/CodeEditor';
import MargBottom16 from '@/components/margBottom16';
import MyMdEditor from '@/components/MyMdEditor';
import {
  addQuestionUsingPost, editQuestionUsingPost,
  getQuestionTagsUsingGet, getQuestionVoByIdUsingGet,
} from '@/services/backend/questionController';
import {
  PageContainer,
  ProCard,
  ProForm,
  ProFormDigit,
  ProFormSelect,
  ProFormText,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Form, message } from 'antd';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { useNavigate } from 'react-router-dom';

interface TagsOptionsType {
  label: string;
  value: string;
}

const EditQuestion: React.FC = () => {
  const navigate = useNavigate();
  const params = useParams();

  const [questionTags, setQuestionTags] = useState<TagsOptionsType[]>([]);

  const fetchQuestionTags = async () => {
    const result = await getQuestionTagsUsingGet();
    if (result.data) {
      const tagsOptions = result.data.map((tag) => ({
        label: tag.tagName ?? '',
        value: tag.tagName ?? '',
      }));
      setQuestionTags(tagsOptions);
    }
  };

  const [form] = Form.useForm()
  const fetchQuestionData = async () => {
    if (params.id) {
      const result = await getQuestionVoByIdUsingGet({
        id: params.id,
      });
      if (result.data) {
        form.setFieldsValue(result.data)
      }
    } else {
      message.error('页面错误，请你返回');
      navigate('/');
    }
  };

  useEffect(() => {
    fetchQuestionData();
    fetchQuestionTags();
  }, []);

  //添加节点
  const handleAdd = async (value: API.QuestionAddRequest) => {
    console.log(value);
    // 解析JSON字符串为JavaScript对象数组
    // @ts-ignore
    value.judgeCase = JSON.parse(value.judgeCase);
    const result = await editQuestionUsingPost({
      id: params.id,
      ...value,
    });
    if (result.data) {
      message.success('修改成功');
    }
  };
  //
  // const initData = {
  //   id: '1764331838811181058',
  //   title: '1111',
  //   content: '# test',
  //   tags: ['1', '2'],
  //   answer: '# test\n# test',
  //   judgeCase: "[{'input': '111','output': '222',},{'input': '111','output': '222',},]",
  //   judgeConfig: {
  //     timeLimit: '111',
  //     memoryLimit: '222',
  //     stackLimit: '332',
  //   },
  // };

  return (
    <PageContainer title={'创建题目'}>
      <ProCard>
        {/* TODO 加载原始数据*/}
        <ProForm<API.QuestionEditRequest>
          style={{ padding: '10px' }}
          key={'id'}
          form={form}
          layout={'vertical'}
          onFinish={async (values) => handleAdd(values)}
          autoFocusFirstInput
        >
          <ProFormText
            label={'题目名称'}
            width="md"
            name="title"
            placeholder="请输入题目名称"
            rules={[{ required: true, message: '这是必填项' }]}
          />
          <ProFormSelect
            label={'标签'}
            name={'tags'}
            width={'md'}
            mode={'multiple'}
            options={questionTags}
          />
          <MargBottom16 />
          <Form.Item label={'题目描述'} name={'content'}>
            <MyMdEditor />
          </Form.Item>
          <MargBottom16 />
          <Form.Item label={'题目答案'} name={'answer'}>
            <MyMdEditor />
          </Form.Item>
          <MargBottom16 />
          <ProForm.Group title={'判题配置'}>
            <ProFormDigit label="时间限制" name={['judgeConfig', 'timeLimit']} width="sm" min={0} />
            <ProFormDigit
              label="内存限制"
              name={['judgeConfig', 'memoryLimit']}
              width="sm"
              min={0}
            />
            <ProFormDigit
              label="堆栈限制"
              name={['judgeConfig', 'stackLimit']}
              width="sm"
              min={0}
            />
          </ProForm.Group>
          <MargBottom16 />
          <ProForm.Group title={'判题实例'} style={{ width: '200px' }}>
            <Form.Item label={'判题实例'} name={'judgeCase'}>
              <CodeEditor lang={'java'} />
            </Form.Item>
          </ProForm.Group>
          <MargBottom16 />
        </ProForm>
      </ProCard>
    </PageContainer>
  );
};
export default EditQuestion;
