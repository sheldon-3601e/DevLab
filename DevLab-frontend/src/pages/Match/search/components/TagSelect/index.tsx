import React, { FC, useState } from 'react'; // 导入 React 相关组件和 hooks
import { DownOutlined, UpOutlined } from '@ant-design/icons'; // 导入 Ant Design 图标
import { Tag } from 'antd'; // 导入 Ant Design 标签组件
import classNames from 'classnames'; // 用于动态设置 className 的工具库
import { useMergedState } from 'rc-util'; // 使用 useMergedState 自定义 hook
import useStyles from './index.style'; // 引入样式文件

// 解构出 CheckableTag 组件
const { CheckableTag } = Tag;

// 定义标签选择器选项的 Props
export interface TagSelectOptionProps {
  value: string | number; // 选项的值
  style?: React.CSSProperties; // 可选的样式属性
  checked?: boolean; // 是否选中
  onChange?: (value: string | number, state: boolean) => void; // 选项状态改变时的回调函数
  children?: React.ReactNode; // 子元素
}

// 定义标签选择器选项组件
const TagSelectOption: React.FC<TagSelectOptionProps> & {
  isTagSelectOption: boolean;
} = ({ children, checked, onChange, value }) => (
  <CheckableTag
    checked={!!checked}
    key={value}
    onChange={(state) => onChange && onChange(value, state)}
  >
    {children}
  </CheckableTag>
);

// 将 isTagSelectOption 属性标记为 true
TagSelectOption.isTagSelectOption = true;

type TagSelectOptionElement = React.ReactElement<TagSelectOptionProps, typeof TagSelectOption>;

// 定义标签选择器的 Props
export interface TagSelectProps {
  onChange?: (value: (string | number)[]) => void; // 整体选项改变时的回调函数
  expandable?: boolean; // 是否可展开
  value?: (string | number)[]; // 当前选中的值
  defaultValue?: (string | number)[]; // 默认选中的值
  style?: React.CSSProperties; // 样式属性
  hideCheckAll?: boolean; // 是否隐藏“全部”选项
  actionsText?: {
    expandText?: React.ReactNode; // 展开文本
    collapseText?: React.ReactNode; // 收起文本
    selectAllText?: React.ReactNode; // 全部文本
  };
  className?: string; // 自定义的 className
  Option?: TagSelectOptionProps; // 选项的 Props
  children?: TagSelectOptionElement | TagSelectOptionElement[]; // 子元素
}

// 定义标签选择器组件
const TagSelect: FC<TagSelectProps> & {
  Option: typeof TagSelectOption;
} = (props) => {
  // 使用样式 hook
  const { styles } = useStyles();
  // 解构出 props 中的相关属性
  const { children, hideCheckAll = false, className, style, expandable, actionsText = {} } = props;
  // 使用 useState 钩子来管理展开状态和选中值
  const [expand, setExpand] = useState<boolean>(false);
  const [value, setValue] = useMergedState<(string | number)[]>(props.defaultValue || [], {
    value: props.value,
    defaultValue: props.defaultValue,
    onChange: props.onChange,
  });

  // 判断是否为 TagSelectOption 类型的元素
  const isTagSelectOption = (node: TagSelectOptionElement) =>
    node &&
    node.type &&
    (node.type.isTagSelectOption || node.type.displayName === 'TagSelectOption');

  // 单个标签改变选中状态时的回调函数
  const handleTagChange = (tag: string | number, checked: boolean) => {
    const checkedTags: (string | number)[] = [...(value || [])];
    const index = checkedTags.indexOf(tag);
    if (checked && index === -1) {
      checkedTags.push(tag);
    } else if (!checked && index > -1) {
      checkedTags.splice(index, 1);
    }
    setValue(checkedTags);
  };

  // 解构出 actionsText 中的相关属性或使用默认值
  const { expandText: defaultExpandText = '展开', collapseText: defaultCollapseText = '收起', selectAllText: defaultSelectAllText = '全部' } = actionsText;

  // 根据展开状态和是否可展开设置 className
  const cls = classNames(styles.tagSelect, className, {
    [styles.hasExpandTag]: expandable,
    [styles.expanded]: expand,
  });

  return (
    <div className={cls} style={style}>
      {/* 遍历子元素，如果是 TagSelectOption 则克隆并传递相关属性 */}
      {children &&
        React.Children.map(children, (child: TagSelectOptionElement) => {
          if (isTagSelectOption(child)) {
            return React.cloneElement(child, {
              key: `tag-select-${child.props.value}`,
              value: child.props.value,
              checked: value && value.indexOf(child.props.value) > -1,
              onChange: handleTagChange,
            });
          }
          return child;
        })}
      {/* 如果可展开，则显示展开/收起按钮 */}
      {expandable && (
        <a
          className={styles.trigger}
          onClick={() => {
            setExpand(!expand);
          }}
        >
          {expand ? (
            <>
              {defaultCollapseText} <UpOutlined />
            </>
          ) : (
            <>
              {defaultExpandText}
              <DownOutlined />
            </>
          )}
        </a>
      )}
    </div>
  );
};

// 将 TagSelectOption 组件挂载到 TagSelect 组件上
TagSelect.Option = TagSelectOption;

export default TagSelect;
