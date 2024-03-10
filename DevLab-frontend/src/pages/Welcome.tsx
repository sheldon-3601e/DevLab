import { PageContainer } from '@ant-design/pro-components';
import { Card, Divider, theme } from 'antd';
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
            backgroundImage: "url('/match_home.jpg')",
          }}
        >
          <div className="section">
            <h2>在 Dev-lab，我们为编程学习者提供了一个全方位的学习平台</h2>
            <Divider />
            <p>致力于让编程学习更加轻松、有趣和高效。</p>
          </div>

          <div className="section">
            <h3>1、个性化学习体验</h3>
            <p>无论你是初学者还是有经验的开发者，我们都为你量身定制了个性化的学习路径。</p>
            <p>
              通过我们智能的标签匹配系统，你可以找到与你兴趣和技能水平相符的学习内容，让学习更加高效。
            </p>
          </div>

          <div className="section">
            <h3>2、编程学习小队</h3>
            <p>在我们的编程学习小队中，你可以与志同道合的伙伴一起学习、交流，共同成长。</p>
            <p>不再孤单地面对编程挑战，与小伙伴们一起助力彼此前进。</p>
          </div>

          <div className="section">
            <h3>3、在线OJ系统</h3>
            <p>挑战自我，提升编程技能！通过我们的在线OJ系统，你可以参与各种编程挑战和竞赛，</p>
            <p>与全球的编程爱好者一较高下，收获荣誉和成就感。</p>
          </div>

          <div className="section">
            <h3>4、帖子交流论坛</h3>
            <p>
              在我们的论坛中，你可以自由地分享你的学习心得、经验和疑惑，与来自世界各地的编程爱好者交流思想 ，
            </p>
            <p>
              共同解决问题，激发创意。
            </p>
          </div>

          <div className="section">
            <br />
            <p>现在就加入我们，一起探索无限可能！</p>
          </div>

          <hr />

          <div className="login-message">
            <h3>本站的功能需要登录才能使用，请您点击右上角登录键</h3>
          </div>
        </div>
      </Card>
    </PageContainer>
  );
};

export default Welcome;
