// @ts-ignore
import axios from "@/utils/http.js";

export interface SimpleUser {
  code: string;
  name: string;
  nickName: string;
  avatarUrl: string;
}

export async function loadSimpleUsers(keywords: string): Promise<SimpleUser[]> {
  let res = await axios.get("/kharazim-api/user/list", {
    params: { keywords: keywords },
  });
  return (res.data.data || []) as SimpleUser[];
}
