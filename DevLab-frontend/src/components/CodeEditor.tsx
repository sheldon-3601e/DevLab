import React from 'react';
import CodeMirror, {ViewUpdate} from '@uiw/react-codemirror';
import {javascript} from '@codemirror/lang-javascript';
import {githubDark} from '@uiw/codemirror-theme-github'

interface CodeEditorPropsType {
  value: string,
  handleChange: (v: string, viewUpdate: ViewUpdate) => void,
  lang: string
}

const CodeEditor: React.FC<CodeEditorPropsType> = (props) => {
  return (
    <CodeMirror
      theme={githubDark}
      value={props.value}
      height="200px"
      extensions={[javascript({jsx: true})]}
      onChange={(value, viewUpdate) => {
        props.handleChange(value, viewUpdate)
      }}
    />
  )
}

export default CodeEditor;
