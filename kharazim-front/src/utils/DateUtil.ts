import { dayjs } from "element-plus";

export function dateTimeFormat(date: Date): string | null {
  if (date) {
    return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
  }
  return null;
}

export function dateFormat(date: Date): string | null {
  if (date) {
    return dayjs(date).format("YYYY-MM-DD");
  }
  return null;
}
