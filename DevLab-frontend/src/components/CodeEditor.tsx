import { java } from '@codemirror/lang-java';
import { javascript } from '@codemirror/lang-javascript';
import { json } from '@codemirror/lang-json';
import { githubDark } from '@uiw/codemirror-theme-github';
import CodeMirror, { Compartment, ViewUpdate } from '@uiw/react-codemirror';
import React, { useEffect, useState } from 'react';

interface CodeEditorPropsType {
  value?: string;
  onChange?: (v: string, viewUpdate: ViewUpdate) => void;
  lang: string;
}

const CodeEditor: React.FC<CodeEditorPropsType> = (props) => {
  // 支持切换语言
  const languageConf = new Compartment();

  const [extensions, setExtensions] = useState([languageConf.of(java())]);
  const getExtensions = () => {
    switch (props.lang) {
      case 'javascript':
        return [languageConf.of(javascript({ jsx: true, typescript: true }))];
      case 'java':
        return [languageConf.of(java())];
      case "json":
          return [languageConf.of(json())];
      default:
        return [];
    }
  };
  useEffect(() => {
    setExtensions(getExtensions());
  }, [props.lang]);

  return (
    <CodeMirror
      key={props.lang}
      theme={githubDark}
      value={props.value}
      minWidth={'800px'}
      minHeight={'100vh'}
      height="200px"
      extensions={extensions}
      // extensions={[javaLanguage]}
      onChange={(value, viewUpdate) => {
        if (props.onChange) {
          props.onChange(value, viewUpdate);
        }
      }}
    />
  );
};

export default CodeEditor;
