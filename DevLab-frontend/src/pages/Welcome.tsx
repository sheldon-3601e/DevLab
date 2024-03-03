import { PageContainer } from '@ant-design/pro-components';
import {Card, Divider, theme} from 'antd';
import React from 'react';
import defaultSettings from '../../config/defaultSettings';

const Welcome: React.FC = () => {
  const { token } = theme.useToken();
  return (
    <PageContainer>
      <Card
        style={{
          borderRadius: 8,
        }}
        bodyStyle={{
          backgroundImage:
            defaultSettings.navTheme === 'realDark'
              ? 'background-image: linear-gradient(75deg, #1A1B1F 0%, #191C1F 100%)'
              : 'background-image: linear-gradient(75deg, #FBFDFF 0%, #F5F7FF 100%)',
        }}
      >
        <div
          style={{
            backgroundPosition: '100%',
            backgroundRepeat: 'no-repeat',
            backgroundSize: '40% auto',
            backgroundImage:
              "url('/match_home.jpg')",
          }}

        >
          <div style={{ fontSize: '24px', color: token.colorTextHeading }}>
            <strong>欢迎来到我们的用户匹配平台！</strong>
          </div>
          {/*<Image src={'https://ichef.bbci.co.uk/ace/ws/640/cpsprodpb/182B7/production/_118599989_gettyimages-2187838.jpg'} />*/}
          <p style={{
            fontSize: '16px',
            color: token.colorTextSecondary,
            lineHeight: '28px',
            marginTop: '16px',
            marginBottom: '32px',
            width: '55%',
          }}>
            在这里，我们为您提供了一个全新的社交体验，让您轻松地找到志同道合的伙伴，开启属于您的精彩旅程。<br />
            我们不仅支持基本的用户管理功能，还为您提供了强大的按标签检索、推荐相似用户以及组队功能。无论您是寻找共同爱好的伙伴，还是需要组建一个高效团队，我们都能够满足您的需求。<br />
            通过我们智能的匹配算法，您可以快速地找到与您兴趣相投的人，与他们交流、合作，共同探索更多可能性。<br />
            现在就加入我们，一起探索无限可能！<br />
            <Divider />
            <h4>
              本站的功能需要登录才能使用，请您点击右上角登录键
            </h4>
          </p>
        </div>
      </Card>
    </PageContainer>
  );
};

export default Welcome;
