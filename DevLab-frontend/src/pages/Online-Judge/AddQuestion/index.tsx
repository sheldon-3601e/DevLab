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
import { message } from 'antd';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

interface TagsOptionsType {
  label: string;
  value: string;
}

const AddQuestion: React.FC = () => {
  const navigate = useNavigate();

  const [content, setContent] = useState('');
  const [answer, setAnswer] = useState('');
  const [caseString, setCaseString] = useState('');
  const [questionTags, setQuestionTags] = useState<TagsOptionsType[]>([]);

  // TODO 获取题目可以添加的标签
  // const questionTags = [
  //   {label: '选项1', value: 1},
  //   {label: '选项2', value: 2},
  //   {label: '选项3', value: 3},
  // ]
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

  /**
   * 添加节点
   * @param value
   */
  const handleAdd = async (value: API.QuestionAddRequest) => {
    console.log({ ...value, content, answer, caseString });
    // 解析JSON字符串为JavaScript对象数组
    const judgeCases: API.JudgeCase[] = JSON.parse(caseString);
    const result = await addQuestionUsingPost({
      ...value,
      content,
      answer,
      judgeCase: judgeCases,
    });
    if (result.data) {
      message.success('添加成功');
    }
  };

  return (
    <PageContainer title={'创建题目'}>
      <ProCard>
        <ProForm<API.QuestionAddRequest>
          style={{ padding: '10px' }}
          layout={'horizontal'}
          onFinish={async (values) => handleAdd(values)}
          autoFocusFirstInput
        >
          <ProForm.Group title={'题目名称'}>
            <ProFormText
              width="md"
              name="title"
              placeholder="请输入题目名称"
              rules={[{ required: true, message: '这是必填项' }]}
            />
          </ProForm.Group>
          <ProForm.Group title={'标签'}>
            <ProFormSelect width={'md'} name={'tags'} mode={'multiple'} options={questionTags} />
          </ProForm.Group>
          <MargBottom16 />
          <ProForm.Group title={'题目描述'}>
            <MyMdEditor value={content} handleChange={setContent} />
          </ProForm.Group>
          <MargBottom16 />
          <ProForm.Group title={'题目答案'}>
            <MyMdEditor value={answer} handleChange={setAnswer} />
          </ProForm.Group>
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
            <CodeEditor value={caseString} handleChange={setCaseString} lang={'java'} />
          </ProForm.Group>
          <MargBottom16 />
        </ProForm>
      </ProCard>
    </PageContainer>
  );
};
export default AddQuestion;
