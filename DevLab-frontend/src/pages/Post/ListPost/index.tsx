import TagList from '@/components/TagList';
import { listPostVoByPageUsingPost } from '@/services/backend/postController';
import { doPostFavourUsingPost } from '@/services/backend/postFavourController';
import { doThumbUsingPost } from '@/services/backend/postThumbController';
import { LikeFilled, LikeOutlined, StarFilled, StarOutlined } from '@ant-design/icons';
import { ActionType, PageContainer, ProList } from '@ant-design/pro-components';
import React, { useRef } from 'react';

/**
 * 文章列表
 * @constructor
 */
const ListPost: React.FC = () => {
  const actionRef = useRef<ActionType>();
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

  return (
    <div id={'listPost'} className={'page-content-center'}>
      <PageContainer className={'page-center'}>
        <ProList<API.PostVO>
          itemLayout="vertical"
          rowKey="id"
          actionRef={actionRef}
          split
          pagination={{
            pageSize: 10,
          }}
          request={async (params, sort, filter) => {
            const sortField = 'createTime';
            const sortOrder = 'desc';

            const { data, code } = await listPostVoByPageUsingPost({
              ...params,
              sortField,
              sortOrder,
              ...filter,
            });

            return {
              success: code === 0,
              data: data?.records || [],
              total: Number(data?.total) || 0,
            };
          }}
          metas={{
            title: {
              dataIndex: 'title',
            },
            description: {
              render: (_, row) => <>{row.tagList && <TagList tags={row.tagList} />}</>,
            },
            actions: {
              render: (_, row) => [
                <span key={row.id}>
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
                </span>,
                <span key={row.id}>
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
                </span>,
              ],
            },
            content: {
              render: (_, row) => {
                return <div>{row.description}</div>;
              },
            },
          }}
        />
      </PageContainer>
    </div>
  );
};
export default ListPost;
