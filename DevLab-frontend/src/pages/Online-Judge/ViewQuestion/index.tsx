import TagList from '@/components/TagList';
import {
  deleteQuestionUsingPost,
  listQuestionVoByPageUsingPost,
} from '@/services/backend/questionController';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {PageContainer, ProTable} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, message, Space, Tag} from 'antd';
import React, {useRef} from 'react';
import {useNavigate} from 'react-router-dom';

/**
 * 用户管理页面
 *
 * @constructor
 */
const ViewQuestion: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const navigate = useNavigate();
  /**
   * 删除节点
   *
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
      dataIndex: 'title',
      valueType: 'text',
      hideInTable: true,
      hideInSearch: true,
    },
    {
      title: '题目名称',
      dataIndex: 'title',
      valueType: 'text',
    },
    {
      title: '标签',
      dataIndex: 'tags',
      hideInSearch: true,
      render: (_, record) => <TagList tags={record.tags ?? []}/>,
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
              navigate(`/question/view/${record.id}`);
            }}
          >
            详情
          </Button>
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
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              navigate('/question/add');
            }}
          >
            <PlusOutlined/> 新建
          </Button>,
        ]}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;

          const {data, code} = await listQuestionVoByPageUsingPost({
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
export default ViewQuestion;
