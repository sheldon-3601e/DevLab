import MarginBottom16 from '@/components/margBottom16';
import { listMatchUSerVoUsingPost } from '@/services/backend/userController';
import { RedoOutlined } from '@ant-design/icons';
import { PageContainer, ProCard } from '@ant-design/pro-components';
import '@umijs/max';
import { Avatar, Button, message, Tag } from 'antd';
import React, { useEffect, useRef, useState } from 'react';

/**
 * 伙伴匹配 主页
 *
 * @constructor
 */
const MatchHome: React.FC = () => {
  // 初始化查询参数
  const initQueryParams = {
    current: 1,
    pageSize: 8,
    matchNum: 32,
  };
  const [queryParams, setQueryParams] = useState(initQueryParams);
  const [recommendUserList, setRecommendUserList] = useState<API.UserVO[]>([]);

  const loading = useRef<boolean>(true);

  // 获取推荐用户
  const loadData = async () => {
    loading.current = true;
    const res = await listMatchUSerVoUsingPost(queryParams);
    if (res.data) {
      setRecommendUserList(res.data.records ?? []);
    } else {
      message.error('获取匹配用户失败，请您刷新！');
    }
    loading.current = false;
  };

  // 刷新页面数据
  const refreshData = () => {
    if (queryParams.current === 4) {
      setQueryParams(initQueryParams);
    } else {
      setQueryParams({
        ...queryParams,
        current: queryParams.current + 1,
      });
    }
  };

  useEffect(() => {
    loadData();
  }, [queryParams]);

  return (
    <PageContainer title={'主页'}>
      <ProCard
        loading={loading.current}
        style={{ marginBlockStart: 8 }}
        title="推荐用户"
        extra={
          <div>
            <Button
              type="primary"
              size={'large'}
              shape="round"
              icon={<RedoOutlined />}
              onClick={refreshData}
            >
              换一批
            </Button>
          </div>
        }
        wrap
        gutter={[16, { xs: 8, sm: 16, md: 24, lg: 32 }]}
      >
        {recommendUserList.map((item, index) => (
          <ProCard
            key={index}
            colSpan={{ xs: 24, sm: 24, md: 12, lg: 12, xl: 12 }}
            headerBordered
            hoverable
            bordered
            title={
              <div>
                {item.userAvatar ? (
                  <Avatar src={item.userAvatar} style={{ marginRight: '16px' }} />
                ) : (
                  <Avatar style={{ marginRight: '16px' }}>空白</Avatar>
                )}
                {item.userName}
              </div>
            }
            extra={
              <div>
                <Button
                  key={item.id}
                  type={'primary'}
                  onClick={() => {
                    message.warning('功能正在开发，敬请期待！');
                  }}
                >
                  关注
                </Button>
              </div>
            }
          >
            {item.tags?.map((tag) => (
              <Tag color="blue" key={tag}>
                {tag}
              </Tag>
            ))}
            <MarginBottom16 />
            {item.userProfile}
          </ProCard>
        ))}
      </ProCard>
    </PageContainer>
  );
};
export default MatchHome;
