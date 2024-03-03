import { Avatar, Button, List, message } from 'antd';
import React, { useEffect, useState } from 'react';
import useStyles from './index.style';
import {listJoinedTeamUsingPost, quitTeamUsingPost} from '@/services/backend/teamController';
import { PROPAGATE_HOST } from '@/constants';
import dayjs from 'dayjs';

const JoinedTeamList: React.FC = () => {
  const { styles } = useStyles();
  const [joinedTeamList, setJoinedTeamList] = useState<API.TeamUserVO[]>([]);

  /**
   * 获取已加入队伍列表数据
   */
  const loadData = async () => {
    const result = await listJoinedTeamUsingPost();
    if (result.data) {
      setJoinedTeamList(result.data);
    } else {
      message.error('获取数据失败，请刷新重试！');
    }
  };

  /**
   * 退出队伍
   * @param teamId
   */
  const quitTeam = async (teamId: string) => {
    const result = await quitTeamUsingPost({
      id: teamId,
    })
    if (result.data) {
      message.success('队伍退出成功')
      loadData()
    } else {
      message.error('队伍退出失败，请您重试！')
    }
  }

  useEffect(() => {
    loadData();
  }, []);

  return (
    <List<API.TeamUserVO>
      size="large"
      className={styles.articleList}
      rowKey="id"
      itemLayout="vertical"
      dataSource={joinedTeamList}
      renderItem={(item) => (
        <List.Item
          key={item.id}
          actions={[
            <Button key={item.id} type={'primary'} size={'small'} onClick={() => quitTeam(item.id?? '')}>
              退出
            </Button>,
          ]}
        >
          <List.Item.Meta
            title={
              <a className={styles.listItemMetaTitle} href={PROPAGATE_HOST}>
                {item.teamName}
              </a>
            }
          />
          <div className={styles.description}>{item.description}</div>
          <div>
            <div className={styles.extra}>
              <Avatar
                src={item.createUser?.userAvatar}
                size="small"
                style={{ marginRight: '8px' }}
              />
              <a href={PROPAGATE_HOST}>{item.createUser?.userName}</a> 创建于
              <em>{dayjs(item.createTime).format('YYYY-MM-DD HH:mm')}</em>
            </div>
          </div>
        </List.Item>
      )}
    />
  );
};
export default JoinedTeamList;
