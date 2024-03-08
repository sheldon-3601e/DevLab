import TagList from '@/components/TagList';
import { listPostVoByPageUsingPost } from '@/services/backend/postController';
import { doPostFavourUsingPost } from '@/services/backend/postFavourController';
import { doThumbUsingPost } from '@/services/backend/postThumbController';
import { LikeFilled, LikeOutlined, StarFilled, StarOutlined } from '@ant-design/icons';
import { ActionType, PageContainer, ProCard, ProList } from '@ant-design/pro-components';
import { Divider, Tabs, TabsProps } from 'antd';
import Search from 'antd/es/input/Search';
import React, { useEffect, useRef, useState } from 'react';
import {useNavigate} from "react-router-dom";

/**
 * 文章列表
 * @constructor
 */
const ListPost: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const [searchKey, setSearchKey] = useState<string>('');

  const [sortField, setSortField] = useState('createTime');

  const navigate = useNavigate()

  /**
   * 点赞操作
   */
  const handleThumb = async (id: string) => {
    const res = await doThumbUsingPost({
      postId: id,
    });
    if (res.data) {
      actionRef.current?.reload();
    }
  };

  /**
   * 收藏操作
   */
  const handleFavour = async (id: string) => {
    const res = await doPostFavourUsingPost({
      postId: id,
    });
    if (res.data) {
      actionRef.current?.reload();
    }
  };

  const handleRowClick = (postId: string) => {
    console.log(postId);
    navigate(`/post/view/${postId}`)
  };

  const onSearch = (value: string) => {
    setSearchKey(value);
    // @ts-ignore
    actionRef.current?.reload({ current: 1 });
  };

  useEffect(() => {
    actionRef.current?.reload();
  }, [sortField]);

  const items: TabsProps['items'] = [
    {
      key: 'createTime',
      label: '时间优先',
    },
    {
      key: 'thumbNum',
      label: '热度优先',
    },
  ];

  return (
    <div id={'listPost'} className={'page-content-center'}>
      <PageContainer className={'page-center'}>
        <Search
          placeholder="请输入搜索内容"
          allowClear
          enterButton="搜索"
          size="large"
          onSearch={onSearch}
        />
        <Divider />
        <ProCard>
          <Tabs defaultActiveKey="1" items={items} onChange={setSortField} />
          <ProList<API.PostVO>
            id={searchKey}
            itemLayout="vertical"
            rowKey="id"
            actionRef={actionRef}
            split
            pagination={{
              pageSize: 10,
            }}
            request={async (params, sort, filter) => {
              const sortOrder = 'desc';

              const { data, code } = await listPostVoByPageUsingPost({
                ...params,
                sortField,
                sortOrder,
                ...filter,
                searchText: searchKey,
              });
              return {
                success: code === 0,
                data: data?.records || [],
                total: Number(data?.total) || 0,
              };
            }}
            metas={{
              title: {
                render: (_, row) => (
                  <>{row.id && <div onClick={() => handleRowClick(row.id ?? '')}>{row.title}</div>}</>
                ),
              },
              description: {
                render: (_, row) => <>{row.tagList && <TagList tags={row.tagList} />}</>,
              },
              actions: {
                render: (_, row) => [
                  <>
                    <span key="thumb">
                      {row.hasThumb ? (
                        <LikeFilled
                          style={{ marginRight: '4px' }}
                          onClick={() => handleThumb(row.id ?? '')}
                        />
                      ) : (
                        <LikeOutlined
                          style={{ marginRight: '4px' }}
                          onClick={() => handleThumb(row.id ?? '')}
                        />
                      )}
                      {row.thumbNum}
                    </span>
                    <span key="favour">
                      {row.hasFavour ? (
                        <StarFilled
                          style={{ marginRight: '4px' }}
                          onClick={() => handleFavour(row.id ?? '')}
                        />
                      ) : (
                        <StarOutlined
                          style={{ marginRight: '4px' }}
                          onClick={() => handleFavour(row.id ?? '')}
                        />
                      )}
                      {row.favourNum}
                    </span>
                  </>,
                ],
              },
              content: {
                render: (_, row) => {
                  return <div>{row.description}</div>;
                },
              },
            }}
          />
        </ProCard>
      </PageContainer>
    </div>
  );
};
export default ListPost;
