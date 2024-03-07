export default [
  { path: '/user', layout: false, routes: [{ path: '/user/login', component: './User/Login' }] },
  { path: '/welcome', icon: 'smile', component: './Welcome', name: '欢迎' },
  {
    icon: 'userAddOutlined',
    path: '/friend',
    name: '码友',
    access: 'canLogin',
    routes: [
      {
        path: '/friend/match',
        component: './Match/Home',
        name: '推荐码友',
      },
      {
        path: '/friend/search',
        component: './Match/search',
        name: '寻找码友',
      },
    ],
  },
  {
    icon: 'teamOutlined',
    path: '/match/team',
    name: '队伍',
    access: 'canLogin',
    routes: [
      {
        path: '/match/team',
        redirect: '/match/team/show',
      },
      {
        path: '/match/team/show',
        component: './Match/Team',
        name: '队伍列表',
      },
      {
        path: '/match/team/add',
        component: './Match/Team/AddTeam',
        name: '创建队伍',
      },
    ],
  },
  {
    icon: 'editFilled',
    path: '/question',
    name: 'OJ平台',
    access: 'canLogin',
    routes: [
      {
        path: '/question/list',
        icon: 'smile',
        component: './Question/ListQuestion',
        name: '题目列表',
      },
      {
        path: '/question/submit/list',
        icon: 'smile',
        component: './Question/ListQuestionSubmit',
        name: '提交列表',
      },
      {
        path: '/question/manage',
        icon: 'smile',
        component: './Question/ManageQuestion',
        name: '管理题目',
      },
      {
        path: '/question/add',
        icon: 'smile',
        component: './Question/AddQuestion',
        name: '创建题目',
      },
    ],
  },
  {
    icon: 'userOutlined',
    path: '/account',
    name: '个人',
    access: 'canLogin',
    routes: [
      {
        path: '/account/center',
        icon: 'smile',
        component: './Account/Center',
        name: '个人中心',
      },
      {
        path: '/account/settings',
        icon: 'smile',
        component: './Account/Settings',
        name: '个人设置',
      },
    ],
  },
  {
    path: '/admin',
    icon: 'crown',
    name: '管理页',
    access: 'canAdmin',
    routes: [
      { path: '/admin', redirect: '/admin/user' },
      { icon: 'table', path: '/admin/user', component: './Admin/User', name: '用户管理' },
    ],
  },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
