import MdEditor from 'for-editor';
import React from 'react';

interface EditorProps {
  value?: string;
  onChange?: (v: string) => void;
}

// Markdown 编辑器组件
const MyMdEditor: React.FC<EditorProps> = (props) => {
  /** 默认工具栏按钮全部开启, 传入自定义对象
   例如: {
   h1: true, // h1
   code: true, // 代码块
   preview: true, // 预览
   }
   此时, 工具栏只显示此三个功能键（注：传入空对象则不显示工具栏）
   */
  // 工具栏菜单
  const toolbar = {
    h1: true, // h1
    h2: true, // h2
    h3: true, // h3
    h4: true, // h4
    img: true, // 图片
    link: true, // 链接
    code: true, // 代码块
    preview: true, // 预览
    expand: true, // 全屏
    /* v0.0.9 */
    undo: true, // 撤销
    redo: true, // 重做
    save: true, // 保存
    /* v0.2.3 */
    subfield: true, // 单双栏模式
  };

  return (
    <MdEditor
      placeholder="请输入Markdown文本"
      toolbar={toolbar}
      value={props.value}
      onChange={(value) => {
        if (props.onChange) props.onChange(value);
      }}
      onSave={(value) => {
        if (props.onChange) props.onChange(value);
      }}
    />
  );
};

export default MyMdEditor;
