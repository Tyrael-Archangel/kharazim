// @ts-ignore
import axios from "@/utils/http.js";

export interface SimpleCustomer {
  code: string;
  name: string;
  phone: string;
}

export async function loadSimpleCustomers(keywords: string, conditionType: string | null): Promise<SimpleCustomer[]> {
  let res = await axios.get("/kharazim-api/customer/list", {
    params: { keywords: keywords, conditionType: conditionType },
  });
  return (res.data.data || []) as SimpleCustomer[];
}
