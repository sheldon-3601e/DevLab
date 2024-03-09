import { Avatar } from 'antd';
import dayjs from 'dayjs';
import React from 'react';
import useStyles from './index.style';

interface ArticleListContentProps {
  userVo: API.UserVO;
}

const ArticleListContent: React.FC<ArticleListContentProps> = ({userVo}) => {
  // console.log(userVo)
  const { styles } = useStyles();
  return (
    <div>
      <div className={styles.description}>{userVo.userProfile}</div>
      <div className={styles.extra}>
        <Avatar src={userVo.userAvatar} size="small" />
        <em>{dayjs(userVo.createTime).format('YYYY-MM-DD HH:mm')}</em>
      </div>
    </div>
  );
};
export default ArticleListContent;
