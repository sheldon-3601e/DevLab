import TagList from '@/components/TagList';
import { listPostVoByPageUsingPost } from '@/services/backend/postController';
import { LikeOutlined, MessageOutlined, StarOutlined } from '@ant-design/icons';
import { PageContainer, ProList } from '@ant-design/pro-components';
import { Button } from 'antd';
import React from 'react';

const IconText = ({ icon, text }: { icon: any; text: string }) => (
  <span>
    {React.createElement(icon, { style: { marginInlineEnd: 8 } })}
    {text}
  </span>
);

/**
 * 文章列表
 * @constructor
 */
const ListPost: React.FC = () => {
  return (
    <div id={'listPost'}>
      <PageContainer>
        <ProList<API.PostVO>
          itemLayout="vertical"
          rowKey="id"
          split
          pagination={{
            pageSize: 10
          }}
          request={async (params, sort, filter) => {
            const sortField = Object.keys(sort)?.[0];
            const sortOrder = sort?.[sortField] ?? undefined;

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
                <IconText icon={StarOutlined} text={(row.favourNum ?? 0).toString()} key="list-vertical-star-o" />,
                <IconText icon={LikeOutlined} text={(row.thumbNum ?? 0).toString()} key="list-vertical-like-o" />,
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
