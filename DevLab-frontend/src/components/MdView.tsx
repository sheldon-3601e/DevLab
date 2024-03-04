import 'github-markdown-css';
import React from 'react';
import ReactMarkdown from 'react-markdown';
import rehypeRaw from 'rehype-raw'; // 解析标签，支持html语法
import remarkGfm from 'remark-gfm';

interface MdViewType {
  value?: string;
}

const MdView: React.FC<MdViewType> = (props) => {
  return (
    <ReactMarkdown
      remarkPlugins={[remarkGfm]}
      rehypePlugins={[rehypeRaw]}
      className={'markdown-body'}
    >
      {props.value}
    </ReactMarkdown>
  );
};

export default MdView;
