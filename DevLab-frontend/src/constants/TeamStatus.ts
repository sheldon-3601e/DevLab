// 队伍状态有关常量
export enum TeamStatusEnum {
  PUBLIC = 0,
  PRIVATE = 1,
  SECRET = 2,
}

// 根据枚举成员的字符串名称获取对应的数字值
export const getTeamStatusByKey = (enumName: string): number | undefined => {
  const enumValue = TeamStatusEnum[enumName as keyof typeof TeamStatusEnum];
  return enumValue !== undefined ? enumValue : undefined;
}
