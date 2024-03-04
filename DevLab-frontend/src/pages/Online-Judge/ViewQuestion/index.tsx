import CodeEditor from '@/components/CodeEditor';
import MargBottom16 from '@/components/margBottom16';
import TagList from '@/components/TagList';
import { getQuestionVoByIdUsingGet } from '@/services/backend/questionController';
import { doQuestionSubmitUsingPost } from '@/services/backend/questionSubmitController';
import { PageContainer, ProCard } from '@ant-design/pro-components';
import '@umijs/max';
import {Button, Col, Divider, message, Row, Select, Statistic, Tabs} from 'antd';
import React, { useEffect, useState } from 'react';
import ReactMarkdown from 'react-markdown';
import { useParams } from 'react-router';
import MdView from "@/components/MdView";

/**
 * 题目详情页面
 *
 * @constructor
 */
const ViewQuestion: React.FC = () => {
  const params = useParams();
  const [initData, setInitData] = useState<API.QuestionVO>();
  const [code, setCode] = useState('');
  const [language, setLanguage] = useState('java');
  const [tabKey, setTabKey] = useState("question")

  const fetchQuestionData = async () => {
    const res = await getQuestionVoByIdUsingGet({
      id: params.id,
    });
    if (res.data) {
      console.log(res.data);
      setInitData(res.data);
    }
  };

  const handleSubmit = async () => {
    const res = await doQuestionSubmitUsingPost({
      questionId: params.id,
      code,
      language,
    });
    if (res.data) {
      message.success('提交成功');
    }
  };

  {
    /* TODO 远程获取编程语言 */
  }
  const options = [
    { value: 'java', label: 'java' },
    { value: 'javascript', label: 'javascript' },
  ];

  useEffect(() => {
    fetchQuestionData();
  }, []);

  const items = [
    {
      label: '题目',
      key: 'question'
    },
    {
      label: '答案',
      key: 'answer'
    },
  ]

  const tagContent = () => {
    if (tabKey === 'question') {
      return (
        <>
          <Divider orientation={'left'}>题目限制</Divider>
          <Row gutter={16}>
            <Col span={8}>
              <Statistic title="时间限制" value={initData?.judgeConfig?.timeLimit} />
            </Col>
            <Col span={8}>
              <Statistic title="内存限制" value={initData?.judgeConfig?.memoryLimit} />
            </Col>
            <Col span={8}>
              <Statistic title="堆栈限制s" value={initData?.judgeConfig?.stackLimit} />
            </Col>
          </Row>
          <Divider orientation={'left'}></Divider>
          <div>
            <MdView value={initData?.content} />
          </div>
        </>
      );
    } else if (tabKey === 'answer') {
      return (
        // 根据需要返回答案内容
        <div>这里是答案内容</div>
      );
    } else {
      return null; // 其他情况返回空，不渲染任何内容
    }
  };


  return (
    <div id={'view-question'}>
      <PageContainer>
        <ProCard>
          <Row>
            <Col sm={24} lg={12} span={12}>
              <Tabs
                defaultActiveKey="question"
                type="card"
                size={'large'}
                items={items}
                onTabClick={(key) =>{
                  setTabKey(key)
                }
                }
              />
              {tagContent()}
            </Col>
            <Col sm={24} lg={12} span={12}>
              <Row>
                <Col span={8}>
                  <Select
                    defaultValue="java"
                    style={{ width: 120 }}
                    onChange={(value, option) => {
                      setLanguage(value);
                    }}
                    options={options}
                  />
                </Col>
                <Col span={2} offset={14}>
                  <Button type={'primary'} onClick={handleSubmit}>
                    提交
                  </Button>
                </Col>
              </Row>
              <MargBottom16 />
              <CodeEditor lang={language} value={code} onChange={setCode} />
            </Col>
          </Row>
        </ProCard>
      </PageContainer>
    </div>
  );
};
export default ViewQuestion;
