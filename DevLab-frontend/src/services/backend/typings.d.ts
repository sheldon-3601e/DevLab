declare namespace API {
  type BaseResponseBoolean_ = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseListQuestionTags_ = {
    code?: number;
    data?: QuestionTags[];
    message?: string;
  };

  type BaseResponseListTagVO_ = {
    code?: number;
    data?: TagVO[];
    message?: string;
  };

  type BaseResponseListTeamUserVO_ = {
    code?: number;
    data?: TeamUserVO[];
    message?: string;
  };

  type BaseResponseLoginUserVO_ = {
    code?: number;
    data?: LoginUserVO;
    message?: string;
  };

  type BaseResponseLong_ = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponsePageQuestion_ = {
    code?: number;
    data?: PageQuestion_;
    message?: string;
  };

  type BaseResponsePageQuestionSubmitVO_ = {
    code?: number;
    data?: PageQuestionSubmitVO_;
    message?: string;
  };

  type BaseResponsePageQuestionVO_ = {
    code?: number;
    data?: PageQuestionVO_;
    message?: string;
  };

  type BaseResponsePageTeamUserVO_ = {
    code?: number;
    data?: PageTeamUserVO_;
    message?: string;
  };

  type BaseResponsePageUser_ = {
    code?: number;
    data?: PageUser_;
    message?: string;
  };

  type BaseResponsePageUserVO_ = {
    code?: number;
    data?: PageUserVO_;
    message?: string;
  };

  type BaseResponseQuestionEditVO_ = {
    code?: number;
    data?: QuestionEditVO;
    message?: string;
  };

  type BaseResponseString_ = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponseUser_ = {
    code?: number;
    data?: User;
    message?: string;
  };

  type BaseResponseUserVO_ = {
    code?: number;
    data?: UserVO;
    message?: string;
  };

  type DeleteRequest = {
    id?: string;
  };

  type getQuestionVOByIdUsingGETParams = {
    /** id */
    id: string;
  };

  type getUserByIdUsingGETParams = {
    /** id */
    id?: string;
  };

  type getUserVOByIdUsingGETParams = {
    /** id */
    id?: string;
  };

  type JudgeCase = {
    input?: string;
    output?: string;
  };

  type JudgeConfig = {
    memoryLimit?: string;
    stackLimit?: string;
    timeLimit?: string;
  };

  type JudgeInfo = {
    memory?: string;
    message?: string;
    time?: string;
  };

  type LoginUserVO = {
    createTime?: string;
    id?: string;
    tags?: string[];
    userAvatar?: string;
    userEmail?: string;
    userGender?: string;
    userName?: string;
    userPhone?: string;
    userProfile?: string;
    userRole?: string;
  };

  type OrderItem = {
    asc?: boolean;
    column?: string;
  };

  type PageQuestion_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: Question[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type PageQuestionSubmitVO_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: QuestionSubmitVO[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type PageQuestionVO_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: QuestionVO[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type PageTeamUserVO_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: TeamUserVO[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type PageUser_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: User[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type PageUserVO_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: UserVO[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type Question = {
    acceptedNum?: number;
    answer?: string;
    content?: string;
    createTime?: string;
    favourNum?: number;
    id?: string;
    isDelete?: number;
    judgeCase?: string;
    judgeConfig?: string;
    submitNum?: number;
    tags?: string;
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    userId?: string;
  };

  type QuestionAddRequest = {
    answer?: string;
    content?: string;
    judgeCase?: JudgeCase[];
    judgeConfig?: JudgeConfig;
    tags?: string[];
    title?: string;
  };

  type QuestionEditRequest = {
    answer?: string;
    content?: string;
    id?: string;
    judgeCase?: JudgeCase[];
    judgeConfig?: JudgeConfig;
    tags?: string[];
    title?: string;
  };

  type QuestionEditVO = {
    answer?: string;
    content?: string;
    id?: string;
    judgeCase?: JudgeCase[];
    judgeConfig?: JudgeConfig;
    tags?: string[];
    title?: string;
  };

  type QuestionQueryRequest = {
    answer?: string;
    content?: string;
    current?: number;
    id?: string;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: string;
  };

  type QuestionSubmitAddRequest = {
    code?: string;
    language?: string;
    questionId?: string;
  };

  type QuestionSubmitQueryRequest = {
    current?: number;
    language?: string;
    pageSize?: number;
    questionId?: string;
    sortField?: string;
    sortOrder?: string;
    status?: number;
    userId?: string;
  };

  type QuestionSubmitVO = {
    code?: string;
    createTime?: string;
    id?: string;
    judgeInfo?: JudgeInfo;
    language?: string;
    questionId?: string;
    questionVO?: QuestionVO;
    status?: number;
    updateTime?: string;
    userId?: string;
    userVO?: UserVO;
  };

  type QuestionTags = {
    createTime?: string;
    id?: string;
    isDelete?: number;
    tagName?: string;
    updateTime?: string;
    userId?: string;
  };

  type QuestionUpdateRequest = {
    answer?: string;
    content?: string;
    id?: string;
    judgeCase?: JudgeCase[];
    judgeConfig?: JudgeConfig;
    tags?: string[];
    title?: string;
  };

  type QuestionVO = {
    acceptedNum?: number;
    content?: string;
    createTime?: string;
    favourNum?: number;
    id?: string;
    judgeConfig?: JudgeConfig;
    submitNum?: number;
    tags?: string[];
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    user?: UserVO;
    userId?: string;
  };

  type TagQueryRequest = {
    number?: number;
  };

  type TagVO = {
    id?: string;
    isParent?: number;
    parentId?: string;
    tagName?: string;
  };

  type TeamAddRequest = {
    description?: string;
    expireTime?: string;
    maxNum?: number;
    password?: string;
    status?: number;
    teamName?: string;
  };

  type TeamJoinRequest = {
    password?: string;
    teamId?: string;
  };

  type TeamQueryRequest = {
    createTime?: string;
    current?: number;
    description?: string;
    expireTime?: string;
    maxNum?: number;
    pageSize?: number;
    searchKey?: string;
    sortField?: string;
    sortOrder?: string;
    status?: number;
    teamName?: string;
  };

  type TeamQuitRequest = {
    id?: string;
  };

  type TeamUpdateRequest = {
    description?: string;
    expireTime?: string;
    id?: string;
    maxNum?: number;
    password?: string;
    status?: number;
    teamName?: string;
  };

  type TeamUserVO = {
    createTime?: string;
    createUser?: UserVO;
    description?: string;
    expireTime?: string;
    hasNum?: number;
    id?: string;
    maxNum?: number;
    status?: number;
    teamName?: string;
    userId?: string;
    userList?: UserVO[];
  };

  type uploadFileUsingPOSTParams = {
    biz?: string;
  };

  type User = {
    createTime?: string;
    id?: string;
    isDelete?: number;
    tags?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userEmail?: string;
    userGender?: string;
    userName?: string;
    userPassword?: string;
    userPhone?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserAddRequest = {
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

  type UserLoginRequest = {
    userAccount?: string;
    userPassword?: string;
  };

  type UserMatchQueryRequest = {
    current?: number;
    matchNum?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type UserQueryByTagRequest = {
    current?: number;
    pageSize?: number;
    searchKey?: string;
    sortField?: string;
    sortOrder?: string;
    tagNameList?: string[];
  };

  type UserQueryRequest = {
    current?: number;
    id?: string;
    mpOpenId?: string;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    unionId?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserRegisterRequest = {
    checkPassword?: string;
    userAccount?: string;
    userPassword?: string;
  };

  type UserUpdateMyRequest = {
    id?: string;
    tags?: string[];
    userAvatar?: string;
    userEmail?: string;
    userGender?: string;
    userName?: string;
    userPhone?: string;
    userProfile?: string;
  };

  type UserUpdateRequest = {
    id?: string;
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserVO = {
    createTime?: string;
    id?: string;
    tags?: string[];
    userAvatar?: string;
    userEmail?: string;
    userGender?: string;
    userName?: string;
    userPhone?: string;
    userProfile?: string;
    userRole?: string;
  };
}
