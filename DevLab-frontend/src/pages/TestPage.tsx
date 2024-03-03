import React, {useState} from 'react';
import CodeEditor from "@/components/CodeEditor";

const TestPage = () => {

  const [value, setValue] = useState("console.log('hello world!');");
  const onChange = (val, viewUpdate) => {
    console.log('val:', val);
    setValue(val);
  };

  return (
    <CodeEditor value={value} handleChange={onChange} lang={'java'}/>
  )
};

export default TestPage;
