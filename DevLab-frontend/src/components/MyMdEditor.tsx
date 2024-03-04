import React from 'react';
import MarkdownIt from 'markdown-it';
import MdEditor from 'react-markdown-editor-lite';
import 'react-markdown-editor-lite/lib/index.css';

// 初始化 Markdown 解析器
const mdParser = new MarkdownIt(/* Markdown-it options */);

interface EditorProps {
  value?: string;
  onChange?: (v: string) => void;
}

// Markdown 编辑器组件
const MyMdEditor: React.FC<EditorProps> = (props) => {
  // 编辑器内容变化处理函数

  return (
    <MdEditor
      style={{ height: '500px', width: '800px' }}
      value={props.value}
      renderHTML={(text) => mdParser.render(text)}
      onChange={(data, event) => {
        // console.log(data, event);
        if (props.onChange) {
          props.onChange(data.text)
        }
      }}
    />
  );
};

export default MyMdEditor;
