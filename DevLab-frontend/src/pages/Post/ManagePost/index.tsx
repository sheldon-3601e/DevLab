import TagList from '@/components/TagList';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {PageContainer, ProTable} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, message, Popconfirm, Space} from 'antd';
import React, {useRef} from 'react';
import {useNavigate} from 'react-router-dom';
import {deletePostUsingPost, listMyPostVoByPageUsingPost} from "@/services/backend/postController";

/**
 * 管理文章页面
 *
 * @constructor
 */
const ManagePost: React.FC = () => {

  const actionRef = useRef<ActionType>();
  const navigate = useNavigate();

  /**
   * 删除文章
   * @param row
   */
  const handleDelete = async (row: API.QuestionVO) => {
    // console.log(row);
    const hide = message.loading('正在删除');
    if (!row) return true;
    try {
      const res = await deletePostUsingPost({
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
  const columns: ProColumns<API.PostVO>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInTable: true
    },
    {
      title: '题目',
      dataIndex: 'title',
      valueType: 'text',
    },
    {
      title: '标签',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => <TagList tags={record.tagList ?? []} />,
    },
    {
      title: '点赞数',
      dataIndex: 'submitNum',
      valueType: 'text',
      sorter: true,
      hideInSearch: true,
    },
    {
      title: '收藏数',
      dataIndex: 'acceptedNum',
      valueType: 'textarea',
      sorter: true,
      hideInSearch: true,
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
              navigate('/post/add', { state: { id: record.id } });
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
              navigate('/post/add');
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;

          const { data, code } = await listMyPostVoByPageUsingPost({
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
export default ManagePost;
