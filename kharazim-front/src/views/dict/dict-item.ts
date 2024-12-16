// @ts-ignore
import axios from "@/utils/http.js";

export interface DictOption {
  key: string;
  value: string;
}

export async function loadDictOptions(dictCode: string): Promise<DictOption[]> {
  let res = await axios.get(`/kharazim-api/system/dict/${dictCode}/items`);
  return (res.data.data || []) as DictOption[];
}
