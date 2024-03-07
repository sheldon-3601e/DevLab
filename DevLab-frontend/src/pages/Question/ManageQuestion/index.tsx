import TagList from '@/components/TagList';
import {
  deleteQuestionUsingPost,
  listMyQuestionVoByPageUsingPost,
} from '@/services/backend/questionController';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import '@umijs/max';
import { Button, message, Popconfirm, Space, Tag } from 'antd';
import React, { useRef } from 'react';
import {  useNavigate } from 'react-router-dom';

/**
 * 管理题目页面
 *
 * @constructor
 */
const ManageQuestion: React.FC = () => {

  const actionRef = useRef<ActionType>();
  const navigate = useNavigate();

  /**
   * 删除题目
   * @param row
   */
  const handleDelete = async (row: API.QuestionVO) => {
    console.log(row);
    const hide = message.loading('正在删除');
    if (!row) return true;
    try {
      const res = await deleteQuestionUsingPost({
        id: row.id as any,
      });
      if (res.data) {
        hide();
        message.success('删除成功');
        actionRef.current?.reload();
        return true;
      }
    } catch (error: any) {
      hide();
      message.error('删除失败，' + error.message);
      return false;
    }
  };

  /**
   * 表格列配置
   */
  const columns: ProColumns<API.QuestionVO>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
    },
    {
      title: '题目名称',
      dataIndex: 'title',
      valueType: 'text',
    },
    {
      title: '标签',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => <TagList tags={record.tags ?? []} />,
    },
    {
      title: '提交数',
      dataIndex: 'submitNum',
      valueType: 'text',
      sorter: true,
      hideInSearch: true,
    },
    {
      title: '通过数',
      dataIndex: 'acceptedNum',
      valueType: 'textarea',
      sorter: true,
      hideInSearch: true,
    },
    {
      title: '通过率',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => (
        <Tag color="green">{((record.acceptedNum ?? 0) * 100) / (record.submitNum ?? 0)}%</Tag>
      ),
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
      title: '更新时间',
      sorter: true,
      dataIndex: 'updateTime',
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
              navigate(`/question/edit/${record.id}`);
            }}
          >
            修改
          </Button>
          <Popconfirm
            placement="left"
            title={'确认'}
            description={'你确认要删除该题目？'}
            okText="Yes"
            cancelText="No"
            onConfirm={() => handleDelete(record)}
          >
            <Button danger={true}>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];
  return (
    <PageContainer>
      <ProTable<API.QuestionVO>
        headerTitle={'题目列表'}
        key={'id'}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              navigate('/question/add');
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;

          const { data, code } = await listMyQuestionVoByPageUsingPost({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.UserQueryRequest);

          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.total) || 0,
          };
        }}
        columns={columns}
      />
    </PageContainer>
  );
};
export default ManageQuestion;
