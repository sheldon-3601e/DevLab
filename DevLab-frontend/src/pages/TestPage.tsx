import React from 'react';
import ReactMarkdown from 'react-markdown';

const Document = () => {
  const [mdContent, setMdContent] = React.useState('');

  return (
    <div>
      <ReactMarkdown className={'markdown-body'}>{'# test'}</ReactMarkdown>
    </div>
  );
};

export default Document;

