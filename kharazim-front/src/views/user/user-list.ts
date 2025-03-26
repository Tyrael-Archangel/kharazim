// @ts-ignore
import axios from "@/utils/http.js";
// @ts-ignore
import { fileUrl } from "@/utils/fileUrl.ts";

export interface SimpleUser {
  code: string;
  name: string;
  nickName: string;
  avatar: string;
  avatarUrl: string;
}

export async function loadSimpleUsers(keywords: string): Promise<SimpleUser[]> {
  let res = await axios.get("/kharazim-api/user/list", {
    params: { keywords: keywords },
  });
  const users: SimpleUser[] = (res.data.data || []) as SimpleUser[];
  users.forEach((user: SimpleUser) => {
    user.avatarUrl = fileUrl(user.avatar);
  });
  return users;
}
