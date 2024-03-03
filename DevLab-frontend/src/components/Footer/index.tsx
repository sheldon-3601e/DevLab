import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import '@umijs/max';
import React from 'react';

const Footer: React.FC = () => {
  const defaultMessage = 'sheldon';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'github',
          title: (
            <>
              <GithubOutlined href={'https://github.com/sheldon-3601e'} /> 个人主页
            </>
          ),
          href: 'https://github.com/sheldon-3601e',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
