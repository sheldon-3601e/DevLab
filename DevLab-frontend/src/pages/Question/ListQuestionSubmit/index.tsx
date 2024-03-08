import {listQuestionSubmitByPageUsingPost} from '@/services/backend/questionController';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {PageContainer, ProTable} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, Descriptions, Modal, Space} from 'antd';
import React, {useRef, useState} from 'react';

/**
 * 提交记录展示页
 *
 * @constructor
 */


const ListQuestionSubmit: React.FC = () => {
  const actionRef = useRef<ActionType>();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [judgeInfo, setJudgeInfo] = useState<API.JudgeInfo>({});

  const handleOk = () => {
    setIsModalOpen(false);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  /**
   * 表格列配置
   */
  const columns: ProColumns<API.QuestionSubmitVO>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
    },
    {
      title: '编程语言',
      dataIndex: 'language',
      valueType: 'text',
    },
    {
      title: '判题状态',
      dataIndex: 'status',
      valueEnum: {
        0: '待判题',
        1: '判题中',
        2: '成功',
        3: '失败',
      },
    },
    {
      title: '题目 id',
      dataIndex: 'questionId',
      valueType: 'text',
    },
    {
      title: '提交用户 id',
      dataIndex: 'userId',
      valueType: 'text',
    },
    {
      title: '创建时间',
      sorter: true,
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => (
        <Space size="middle">
          <Button
            type={'dashed'}
            onClick={() => {
              console.log(record);
              setJudgeInfo(record.judgeInfo ?? {});
              setIsModalOpen(true);
            }}
          >
            判题详情
          </Button>
        </Space>
      ),
    },
  ];
  return (
    <PageContainer>
      <ProTable<API.QuestionSubmitVO>
        headerTitle={'提交列表'}
        key={'id'}
        actionRef={actionRef}
        rowKey="id"
        pagination={{
          pageSize: 10,
        }}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0] ?? 'createTime';
          const sortOrder = sort?.[sortField] ?? 'descend';

          const { data, code } = await listQuestionSubmitByPageUsingPost({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          });

          console.log(data?.records);

          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.total) || 0,
          };
        }}
        columns={columns}
      />
      <Modal title="判题信息" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
        <Descriptions
          column={1}
          bordered
          items={[
            {
              key: '1',
              label: '程序执行结果',
              children: judgeInfo.message,
            },
            {
              key: '2',
              label: '消耗时间',
              children: judgeInfo.time,
            },
            {
              key: '3',
              label: '消耗内存',
              children: judgeInfo.memory,
            },
          ]}
        />
      </Modal>
    </PageContainer>
  );
};
export default ListQuestionSubmit;
