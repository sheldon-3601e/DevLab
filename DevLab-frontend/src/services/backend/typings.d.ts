declare namespace API {
  type BaseResponseBoolean_ = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseInt_ = {
    code?: number;
    data?: number;
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

  type BaseResponsePagePost_ = {
    code?: number;
    data?: PagePost_;
    message?: string;
  };

  type BaseResponsePagePostVO_ = {
    code?: number;
    data?: PagePostVO_;
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

  type BaseResponsePostVO_ = {
    code?: number;
    data?: PostVO;
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

  type getPostVOByIdUsingGETParams = {
    /** id */
    id?: string;
  };

  type getUserByIdUsingGETParams = {
    /** id */
    id?: string;
  };

  type getUserVOByIdUsingGETParams = {
    /** id */
    id?: string;
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

  type PagePost_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: Post[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type PagePostVO_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: PostVO[];
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

  type Post = {
    content?: string;
    createTime?: string;
    favourNum?: number;
    id?: string;
    isDelete?: number;
    tags?: string;
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    userId?: string;
  };

  type PostAddRequest = {
    content?: string;
    tags?: string[];
    title?: string;
  };

  type PostEditRequest = {
    content?: string;
    id?: string;
    tags?: string[];
    title?: string;
  };

  type PostFavourAddRequest = {
    postId?: string;
  };

  type PostFavourQueryRequest = {
    current?: number;
    pageSize?: number;
    postQueryRequest?: PostQueryRequest;
    sortField?: string;
    sortOrder?: string;
    total?: number;
    userId?: string;
  };

  type PostQueryRequest = {
    content?: string;
    current?: number;
    favourUserId?: string;
    id?: string;
    notId?: string;
    orTags?: string[];
    pageSize?: number;
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    total?: number;
    userId?: string;
  };

  type PostThumbAddRequest = {
    postId?: string;
  };

  type PostUpdateRequest = {
    content?: string;
    id?: string;
    tags?: string[];
    title?: string;
  };

  type PostVO = {
    content?: string;
    createTime?: string;
    favourNum?: number;
    hasFavour?: boolean;
    hasThumb?: boolean;
    id?: string;
    tagList?: string[];
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
    id?: string;
    maxNum?: number;
    pageSize?: number;
    searchKey?: string;
    sortField?: string;
    sortOrder?: string;
    status?: number;
    teamName?: string;
    total?: number;
    userId?: string;
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

  type userLoginByWxOpenUsingGETParams = {
    /** code */
    code: string;
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
    total?: number;
  };

  type UserQueryByTagRequest = {
    current?: number;
    pageSize?: number;
    searchKey?: string;
    sortField?: string;
    sortOrder?: string;
    tagNameList?: string[];
    total?: number;
  };

  type UserQueryRequest = {
    current?: number;
    id?: string;
    mpOpenId?: string;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    total?: number;
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
