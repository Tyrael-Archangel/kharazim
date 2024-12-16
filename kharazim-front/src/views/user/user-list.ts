// @ts-ignore
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";

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

export function asyncLoadSimpleUsers(
  keywords: string,
  callback: (traderUsers: SimpleUser[]) => void,
) {
  axios
    .get("/kharazim-api/user/list", {
      params: { keywords: keywords },
    })
    .then((res: AxiosResponse) => {
      callback((res.data.data || []) as SimpleUser[]);
    });
}
