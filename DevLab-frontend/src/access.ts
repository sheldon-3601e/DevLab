/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: { currentUser?: API.LoginUserVO } | undefined) {
  const { currentUser } = initialState ?? {};
  return {
    canLogin: currentUser,
    canAdmin: currentUser && currentUser.userRole === 'admin',
  };
}
