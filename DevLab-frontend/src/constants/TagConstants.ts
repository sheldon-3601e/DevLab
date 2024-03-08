/**
 * 标签相关常量
 */

export const TAG_IS_PARENT = 1;
export const TAG_IS_NOT_PARENT = 0;

interface TagType {
  label: string;
  value: string;
}

export const TAG_LIST: TagType[] = [
  {
    label: 'JavaScript',
    value: 'javascript',
  },
  {
    label: 'Java',
    value: 'java',
  },
  {
    label: 'Python',
    value: 'python',
  },
  {
    label: 'HTML',
    value: 'html',
  },
  {
    label: 'CSS',
    value: 'css',
  },
  {
    label: 'React',
    value: 'react',
  },
  {
    label: 'Node.js',
    value: 'nodejs',
  },
  {
    label: 'Angular',
    value: 'angular',
  },
  {
    label: 'Vue.js',
    value: 'vuejs',
  },
  {
    label: 'Ruby',
    value: 'ruby',
  },
  {
    label: 'PHP',
    value: 'php',
  },
  {
    label: 'C#',
    value: 'csharp',
  },
  {
    label: 'C++',
    value: 'cplusplus',
  },
  {
    label: 'Swift',
    value: 'swift',
  },
  {
    label: 'Kotlin',
    value: 'kotlin',
  },
  {
    label: 'Android',
    value: 'android',
  },
  {
    label: 'iOS',
    value: 'ios',
  },
  {
    label: 'Django',
    value: 'django',
  },
  {
    label: 'Flask',
    value: 'flask',
  },
];

