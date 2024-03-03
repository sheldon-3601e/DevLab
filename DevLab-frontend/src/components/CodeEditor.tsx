import React, {useState} from 'react';
import CodeMirror, {ViewUpdate} from '@uiw/react-codemirror';
import {javascript} from '@codemirror/lang-javascript';
import {githubDark} from '@uiw/codemirror-theme-github'

interface CodeEditorPropsType {
  value: string,
  handleChange: (v: string, viewUpdate: ViewUpdate) => void,
  lang: string
}



const CodeEditor: React.FC<CodeEditorPropsType> = (props) => {

  const langType = "javascript" | "java";
  const [lang, setLang] = useState(props.lang)
  const langOptions = () => {
    if (lang === "javascript") {
      return []
    }
  }

  return (
    <CodeMirror
      theme={githubDark}
      value={props.value}
      minWidth={'800px'}
      height="200px"
      extensions={[javascript({jsx: true})]}
      onChange={(value, viewUpdate) => {
        props.handleChange(value, viewUpdate)
      }}
    />
  )
}

export default CodeEditor;
