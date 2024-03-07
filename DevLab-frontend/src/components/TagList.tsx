import React from "react";
import useStyles from "@/pages/Account/Center/Center.style";
import {Tag} from "antd";

const TagList: React.FC<{
  tags: string[];
}> = ({ tags }) => {
  const { styles } = useStyles();

  return (
    <div className={styles.tags}>
      {tags.map((item) => (
        <Tag key={item}>{item}</Tag>
      ))}
    </div>
  );
};

export default TagList
