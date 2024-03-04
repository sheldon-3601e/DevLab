import { javascript } from '@codemirror/lang-javascript';
import { githubDark } from '@uiw/codemirror-theme-github';
import CodeMirror, { ViewUpdate } from '@uiw/react-codemirror';
import React from 'react';

interface CodeEditorPropsType {
  value?: string;
  onChange?: (v: string, viewUpdate: ViewUpdate) => void;
  lang: string;
}

const CodeEditor: React.FC<CodeEditorPropsType> = (props) => {
  // TODO 支持切换语言

  return (
    <CodeMirror
      theme={githubDark}
      value={props.value}
      minWidth={'800px'}
      height="200px"
      extensions={[javascript({ jsx: true })]}
      onChange={(value, viewUpdate) => {
        if (props.onChange) {
          props.onChange(value, viewUpdate);
        }
      }}
    />
  );
};

export default CodeEditor;
