import { TAG_IS_PARENT } from '@/constants/TagConstants';
import { listTagVoUsingPost } from '@/services/backend/tagController';
import { listUserVoByTagAndPageUsingPost } from '@/services/backend/userController';
import { PageContainer, ProList } from '@ant-design/pro-components';
import { useLocation, useMatch } from '@umijs/max';
import { Button, Card, Form, Input, message, Space, Tag } from 'antd';
import type { FC } from 'react';
import { useEffect, useRef, useState } from 'react';
import StandardFormRow from './components/StandardFormRow';
import TagSelect from './components/TagSelect';

const FormItem = Form.Item;

// 初始化查询参数
interface PageParams {
  pageSize: number;
  current: number;
  sortField: string;
  sortOrder: string;
}

const initPageParams: PageParams = {
  pageSize: 10,
  current: 1,
  sortField: 'id',
  sortOrder: 'desc',
};

const Search: FC = () => {

  const location = useLocation();
  useMatch(location.pathname);

  const [form] = Form.useForm();

  // 标签列表
  const [tagList, setTagList] = useState<API.TagVO[]>([]);

  // 标签的父标签列表
  const [tagParentList, setTagParentList] = useState<API.TagVO[]>([]);

  // 当前用户选择的标签
  const [tagParentSelectedList, setTagParentSelectedList] = useState<(string | number)[]>([]);

  // 父标签所对应的子标签
  const [tagCurrentList, setTagCurrentList] = useState<API.TagVO[]>([]);

  // 用户列表
  const [userList, setUserList] = useState<API.UserVO[]>([]);

  // 查询相关参数
  const [pageParams, setPageParams] = useState({ ...initPageParams });
  const [searchKey, setSearchKey] = useState('');
  const [resultTagList, setResultTagList] = useState<(string | number)[]>([]);

  const total = useRef<number>(100);

  const [loading, setLoading] = useState(false);

  /**'
   * 加载用户数据
   */
  const loadUserList = async () => {
    setLoading(true);
    // 转化标签类型
    const tagNameList = resultTagList.map((tag) => String(tag));
    const result = await listUserVoByTagAndPageUsingPost({
      ...pageParams,
      searchKey,
      tagNameList,
    });
    if (result.data) {
      console.log('userList:', result.data);
      setUserList(result.data.records ?? []);
      total.current = parseInt(result.data.total ?? '100');
    } else {
      message.error('获取数据失败，请您刷新页面！');
    }
    setLoading(false);
  };

  /**
   * 加载标签数据
   */
  const loadTagList = async () => {
    const result = await listTagVoUsingPost();
    if (result.data) {
      setTagList(result.data);
      const parentList = result.data.filter((tag) => tag.isParent === TAG_IS_PARENT);
      setTagParentList(parentList);
    }
  };

  /**
   * 分页查询
   * @param page
   */
  const handlePageChange = (page: number) => {
    setPageParams({
      ...pageParams,
      current: page,
    });
  };

  /**
   * 搜索用户
   * @param value
   */
  const handleFormSubmit = async (value: string) => {
    setSearchKey(value);
    loadUserList();
  };

  useEffect(() => {
    loadUserList();
  }, [pageParams]);

  // 初始数据
  useEffect(() => {
    loadTagList();
    loadUserList();
  }, []);

  // 监听用户选择的父标签，筛选中对应的子标签
  useEffect(() => {
    console.log('tagParentSelectedList:', tagParentSelectedList);
    const tagCurrent = tagList.filter((tag) =>
      tagParentSelectedList.includes(String(tag.parentId)),
    );
    setTagCurrentList(tagCurrent);
  }, [tagParentSelectedList]);

  return (
    <PageContainer
      content={
        <div style={{ textAlign: 'center' }}>
          <Input.Search
            placeholder="请输入"
            enterButton="搜索"
            size="large"
            onSearch={handleFormSubmit}
            style={{ maxWidth: 522, width: '100%' }}
          />
        </div>
      }
    >
      <>
        <Card bordered={false}>
          <Form layout="inline" form={form}>
            <StandardFormRow title="标签类别" block style={{ paddingBottom: 11 }}>
              <FormItem name="parentTagList">
                <TagSelect
                  expandable
                  onChange={(value) => {
                    setTagParentSelectedList(value);
                  }}
                >
                  {tagParentList.map((tag) => (
                    <TagSelect.Option value={tag.id!} key={tag.id}>
                      {tag.tagName}
                    </TagSelect.Option>
                  ))}
                </TagSelect>
              </FormItem>
            </StandardFormRow>
            <StandardFormRow title="标签选择" block style={{ paddingBottom: 11 }}>
              <FormItem name="category">
                <TagSelect expandable onChange={(value) => setResultTagList(value)}>
                  {tagCurrentList.map((tag) => (
                    <TagSelect.Option value={tag.tagName!} key={tag.id}>
                      {tag.tagName}
                    </TagSelect.Option>
                  ))}
                </TagSelect>
              </FormItem>
            </StandardFormRow>
          </Form>
        </Card>
        <Card
          style={{ marginTop: 24 }}
          bordered={false}
          bodyStyle={{ padding: '8px 32px 32px 32px' }}
        >
          <ProList<API.UserVO>
            loading={loading}
            style={{ padding: '16px' }}
            itemLayout="vertical"
            rowKey="id"
            dataSource={userList}
            pagination={{
              onChange: handlePageChange,
              current: pageParams.current,
              pageSize: pageParams.pageSize,
              total: total.current,
            }}
            metas={{
              title: {
                dataIndex: 'userName',
              },
              avatar: {
                dataIndex: 'userAvatar',
              },
              description: {
                dataIndex: 'tags',
                render: (_, row) => {
                  return (
                    <Space size={0}>
                      {row.tags?.map((tag) => (
                        <Tag color="blue" key={tag}>
                          {tag}
                        </Tag>
                      ))}
                    </Space>
                  );
                },
              },
              actions: {
                render: (_, row) => {
                  return [
                    <Button
                      key={row.id}
                      onClick={() => {
                        message.warning('该功能正在开发，敬请期待！');
                      }}
                    >
                      联系
                    </Button>,
                  ];
                },
              },
              content: {
                dataIndex: 'userProfile',
                render: (_, row) => {
                  return <div>{row.userProfile}</div>;
                },
              },
            }}
          />
        </Card>
      </>
    </PageContainer>
  );
};

export default Search;
