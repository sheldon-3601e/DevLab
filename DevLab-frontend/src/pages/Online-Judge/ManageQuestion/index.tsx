import TagList from '@/components/TagList';
import CreateModal from '@/pages/Admin/User/components/CreateModal';
import UpdateModal from '@/pages/Admin/User/components/UpdateModal';
import {deleteQuestionUsingPost, listMyQuestionVoByPageUsingPost} from '@/services/backend/questionController';
import { deleteUserUsingPost } from '@/services/backend/userController';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import '@umijs/max';
import { Button, message, Popconfirm, Space, Tag } from 'antd';
import React, { useRef, useState } from 'react';
import {useNavigate} from "react-router-dom";

/**
 * 用户管理页面
 *
 * @constructor
 */
const ManageQuestion: React.FC = () => {
  // 是否显示新建窗口
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  // 是否显示更新窗口
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  // 当前用户点击的数据
  const [currentRow, setCurrentRow] = useState<API.User>();

  /**
   * 删除节点
   *
   * @param row
   */
  const handleDelete = async (row: API.QuestionVO) => {
    console.log(row)
    const hide = message.loading('正在删除');
    if (!row) return true;
    try {
      const res = await deleteQuestionUsingPost({
        id: row.id as any,
      });
      if (res.data) {
        hide();
        message.success('删除成功');
        actionRef?.current?.reload();
        return true;
      }
    } catch (error: any) {
      hide();
      message.error('删除失败，' + error.message);
      return false;
    }
  };

  const navigate = useNavigate()

  /**
   * 表格列配置
   */
  const columns: ProColumns<API.QuestionVO>[] = [
    {
      title: 'id',
      dataIndex: 'title',
      valueType: 'text',
      hideInTable: true
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
      hideInSearch: true
    },
    {
      title: '通过数',
      dataIndex: 'acceptedNum',
      valueType: 'textarea',
      sorter: true,
      hideInSearch: true
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
              navigate(`/online_judge/edit/${record.id}`)
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
      <ProTable<API.Question>
        headerTitle={'题目列表'}
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
              navigate('/online_judge/add');
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
      <CreateModal
        visible={createModalVisible}
        columns={columns}
        onSubmit={() => {
          setCreateModalVisible(false);
          actionRef.current?.reload();
        }}
        onCancel={() => {
          setCreateModalVisible(false);
        }}
      />
      <UpdateModal
        visible={updateModalVisible}
        columns={columns}
        oldData={currentRow}
        onSubmit={() => {
          setUpdateModalVisible(false);
          setCurrentRow(undefined);
          actionRef.current?.reload();
        }}
        onCancel={() => {
          setUpdateModalVisible(false);
        }}
      />
    </PageContainer>
  );
};
export default ManageQuestion;
