import TagList from '@/components/TagList';
import { useModel } from '@@/plugin-model';
import { MailOutlined, WhatsAppOutlined } from '@ant-design/icons';
import { GridContent } from '@ant-design/pro-components';
import { Card, Col, Divider, Row } from 'antd';
import React, { useState } from 'react';
import useStyles from './Center.style';
import CreatedTeamList from './components/CreatedTeamList';
import JoinedTeamList from './components/JoinedTeamList';
import FavourPostList from "@/pages/Account/Center/components/FavourPostList";

/**
 * 标签选择配置
 */
const operationTabList = [
  {
    key: 'joinedTeamList',
    tab: (
      <span>
        已加入队伍{' '}
        <span
          style={{
            fontSize: 14,
          }}
        />
      </span>
    ),
  },
  {
    key: 'createdTeamList',
    tab: (
      <span>
        已创建的队伍{' '}
        <span
          style={{
            fontSize: 14,
          }}
        />
      </span>
    ),
  },
  {
    key: 'favourPostList',
    tab: (
      <span>
        收藏帖子{' '}
        <span
          style={{
            fontSize: 14,
          }}
        />
      </span>
    ),
  },
];

const UserCenter: React.FC = () => {
  type tabKeyType = 'joinedTeamList' | 'createdTeamList' | 'favourPostList';

  const { styles } = useStyles();
  const [tabKey, setTabKey] = useState<tabKeyType>('joinedTeamList');
  const { initialState } = useModel('@@initialState');
  const [currentUser] = useState(initialState?.currentUser ?? {});

  //  渲染用户信息
  const renderUserInfo = () => {
    return (
      <div className={styles.detail}>
        <p>
          <WhatsAppOutlined
            style={{
              marginRight: 8,
            }}
          />
          {currentUser.userPhone}
        </p>
        <p>
          <MailOutlined
            style={{
              marginRight: 8,
            }}
          />
          {currentUser.userEmail}
        </p>
      </div>
    );
  };

  // 渲染tab切换
  const renderChildrenByTabKey = (tabValue: tabKeyType) => {
    if (tabValue === 'createdTeamList') {
      return <CreatedTeamList />;
    }
    if (tabValue === 'joinedTeamList') {
      return <JoinedTeamList />;
    }
    if (tabValue === 'favourPostList') {
      return <FavourPostList />;
    }
    return null;
  };

  return (
    <GridContent>
      <Row gutter={24}>
        <Col lg={7} md={24}>
          <Card
            bordered={false}
            style={{
              marginBottom: 24,
            }}
          >
            {currentUser && (
              <div>
                <div className={styles.avatarHolder}>
                  <img alt="" src={currentUser.userAvatar} />
                  <div className={styles.name}>{currentUser.userName}</div>
                  <div>{currentUser.userEmail}</div>
                </div>
                {renderUserInfo()}
                <Divider dashed />
                <TagList tags={currentUser.tags || []} />
                <Divider
                  style={{
                    marginTop: 16,
                  }}
                  dashed
                />
                <div className={styles.tags}>
                  <div className={styles.tagsTitle}>个人简介</div>
                  {currentUser.userProfile}
                </div>
              </div>
            )}
          </Card>
        </Col>
        <Col lg={17} md={24}>
          <Card
            className={styles.tabsCard}
            bordered={false}
            tabList={operationTabList}
            activeTabKey={tabKey}
            onTabChange={(_tabKey: string) => {
              setTabKey(_tabKey as tabKeyType);
            }}
          >
            {renderChildrenByTabKey(tabKey)}
          </Card>
        </Col>
      </Row>
    </GridContent>
  );
};
export default UserCenter;
