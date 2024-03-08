import CodeEditor from '@/components/CodeEditor';
import MargBottom16 from '@/components/margBottom16';
import MyMdEditor from '@/components/MyMdEditor';
import {
  addQuestionUsingPost,
  getQuestionTagsUsingGet,
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
import { useNavigate } from 'react-router-dom';

interface TagsOptionsType {
  label: string;
  value: string;
}

const initJudgeCase =
  '[\n' + '  {\n' + '    "input":"input",\n' + '    "output":"output"\n' + '  }\n' + ']';

const AddQuestion: React.FC = () => {
  const navigate = useNavigate();

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

  useEffect(() => {
    fetchQuestionTags();
  }, []);

  //添加节点
  const handleAdd = async (value: API.QuestionAddRequest) => {
    console.log(value);
    // 解析JSON字符串为JavaScript对象数组
    // @ts-ignore
    value.judgeCase = JSON.parse(value.judgeCase);
    const result = await addQuestionUsingPost({
      ...value,
    });
    if (result.data) {
      message.success('添加成功');
      navigate('/question/manage');
    }
  };

  return (
    <PageContainer title={'创建题目'}>
      <ProCard>
        <ProForm<API.QuestionAddRequest>
          style={{ padding: '10px' }}
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
          <Form.Item
            label={'判题实例 - （ json格式 ）'}
            name={'judgeCase'}
            tooltip={'同一次输入的多个实例用空格间隔'}
            initialValue={initJudgeCase}
          >
            <CodeEditor lang={'json'} />
          </Form.Item>
          <MargBottom16 />
        </ProForm>
      </ProCard>
    </PageContainer>
  );
};
export default AddQuestion;
