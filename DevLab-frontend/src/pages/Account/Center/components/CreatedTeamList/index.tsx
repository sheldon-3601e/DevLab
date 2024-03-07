import EditTeam from '@/pages/Match/Team/components/EditTeam';
import {dissolveTeamUsingPost, listCreatedTeamUsingPost, updateTeamUsingPost} from '@/services/backend/teamController';
import {
  DeleteOutlined,
  DownloadOutlined,
  EditOutlined,
  ShareAltOutlined,
} from '@ant-design/icons';
import { ProCard } from '@ant-design/pro-components';
import { Avatar, Card, List, message, Tooltip } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import useStyles from './index.style';

const CreatedTeamList: React.FC = () => {
  const { styles: stylesApplications } = useStyles();

  const [createdTeamList, setCreatedTeamList] = useState<API.TeamUserVO[]>([]);
  const [modelVisible, setModelVisible] = useState<boolean>(false);

  const currTeamUserVO = useRef<API.TeamUserVO>({});

  // 获取tab列表数据
  const loadData = async () => {
    const result = await listCreatedTeamUsingPost();
    if (result.data) {
      setCreatedTeamList(result.data);
    } else {
      message.error('获取数据失败，请你重试！');
    }
  };

  /**
   * 编辑队伍
   * @param value
   */
  const editTeam = async (value: API.TeamUserVO) => {
    const result = await updateTeamUsingPost({
      ...value,
    });
    if (result.data) {
      loadData();
      message.success('修改信息成功');
    } else {
      message.error('修改信息失败，请您重试！');
    }
  };

  const dissolveTeam = async (teamId: string) => {
    const result = await dissolveTeamUsingPost({
      id: teamId
    })
    if (result.data) {
      message.success('队伍解散成功')
      loadData()
    } else {
      message.error('队伍解散失败，请您重试！')
    }
  }

  useEffect(() => {
    loadData();
  }, []);

  const CardInfo: React.FC<{
    hasUser: number;
    maxUser: number;
  }> = ({ hasUser, maxUser }) => (
    <div className={stylesApplications.cardInfo}>
      <div>
        <p>已有用户数</p>
        <p>{hasUser}</p>
      </div>
      <div>
        <p>最大用户数</p>
        <p>{maxUser}</p>
      </div>
    </div>
  );

  return (
    <ProCard>
      <List<API.TeamUserVO>
        rowKey="id"
        className={stylesApplications.filterCardList}
        grid={{
          gutter: 24,
          xxl: 3,
          xl: 2,
          lg: 2,
          md: 2,
          sm: 2,
          xs: 1,
        }}
        dataSource={createdTeamList}
        renderItem={(item) => (
          <List.Item key={item.id}>
            <Card
              hoverable
              bodyStyle={{
                paddingBottom: 20,
              }}
              actions={[
                <Tooltip key="download" title="解散">
                  <DeleteOutlined onClick={async () => dissolveTeam(item.id ?? '') }/>
                </Tooltip>,
                <Tooltip title="编辑" key="edit">
                  <EditOutlined
                    onClick={async () => {
                      currTeamUserVO.current = item;
                      setModelVisible(true);
                    }}
                  />
                </Tooltip>,
                <Tooltip title="分享" key="share">
                  <ShareAltOutlined onClick={() => {
                    message.warning('该功能正在开发，敬请期待！')
                  }}/>
                </Tooltip>,
              ]}
            >
              <Card.Meta
                avatar={<Avatar size="small" src={item.createUser?.userAvatar} />}
                title={item.teamName}
              />
              <div>
                <CardInfo hasUser={item.hasNum ?? 1} maxUser={item.maxNum ?? 5} />
              </div>
            </Card>
          </List.Item>
        )}
      />
      <EditTeam
        teamUserVO={currTeamUserVO.current}
        onFinish={editTeam}
        visible={modelVisible}
        setVisible={setModelVisible}
      />
    </ProCard>
  );
};
export default CreatedTeamList;
