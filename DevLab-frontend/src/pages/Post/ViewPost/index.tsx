import MargBottom16 from '@/components/margBottom16';
import MdView from '@/components/MdView';
import TagList from '@/components/TagList';
import { getPostVoByIdUsingGet } from '@/services/backend/postController';
import { doPostFavourUsingPost } from '@/services/backend/postFavourController';
import { doThumbUsingPost } from '@/services/backend/postThumbController';
import { LikeFilled, LikeOutlined, StarFilled, StarOutlined } from '@ant-design/icons';
import { ProCard } from '@ant-design/pro-components';
import '@umijs/max';
import { Avatar, Button, Space } from 'antd';
import dayjs from 'dayjs';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { useNavigate } from 'react-router-dom';

/**
 * 帖子详情页面
 *
 * @constructor
 */
const ViewPost: React.FC = () => {
  const params = useParams();
  const [postData, setPostData] = useState<API.PostVO>();
  const [isChange, setIsChange] = useState(false);
  const navigate = useNavigate();

  const handleBack = () => {
    navigate(-1);
  };
  const fetchPostData = async () => {
    const res = await getPostVoByIdUsingGet({ id: params.id });
    if (res.data) {
      setPostData(res.data);
    }
  };

  /**
   * 点赞操作
   */
  const handleThumb = async (id: string) => {
    const res = await doThumbUsingPost({ postId: id });
    if (res.data) {
      setIsChange(!isChange);
    }
  };

  /**
   * 收藏操作
   */
  const handleFavour = async (id: string) => {
    const res = await doPostFavourUsingPost({ postId: id });
    if (res.data) {
      setIsChange(!isChange);
    }
  };

  useEffect(() => {
    fetchPostData();
  }, [isChange]);

  return (
    <div id="view-post">
      <ProCard>
        <Button type={'primary'} onClick={handleBack} >返回</Button>
        <MargBottom16 />
        <h1>{postData?.title}</h1>
        <TagList tags={postData?.tagList ?? []} />
        <MargBottom16 />
        <Space>
          <Avatar src={postData?.user?.userAvatar} />
          <span>{postData?.user?.userName}</span>
          <span>{dayjs(postData?.createTime).format('YYYY-MM-DD')}</span>
          <span key="thumb" style={{ marginLeft: '20px' }}>
            {postData?.hasThumb ? (
              <LikeFilled
                style={{ marginRight: '4px' }}
                onClick={() => handleThumb(postData.id ?? '')}
              />
            ) : (
              <LikeOutlined
                style={{ marginRight: '4px' }}
                onClick={() => handleThumb(postData?.id ?? '')}
              />
            )}
            {postData?.thumbNum}
          </span>
          <span key="favour">
            {postData?.hasFavour ? (
              <StarFilled
                style={{ marginRight: '4px' }}
                onClick={() => handleFavour(postData.id ?? '')}
              />
            ) : (
              <StarOutlined
                style={{ marginRight: '4px' }}
                onClick={() => handleFavour(postData?.id ?? '')}
              />
            )}
            {postData?.favourNum}
          </span>
        </Space>
        <MargBottom16 />
        <MdView value={postData?.content} />
      </ProCard>
    </div>
  );
};

export default ViewPost;
