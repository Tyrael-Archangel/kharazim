export function fileUrl(fileId: string): string {
  if (fileId && fileId.trim().length > 0) {
    return "/kharazim-api/system/file/fetch/" + fileId;
  }
  return fileId;
}

export function fileUrls(fileIds: string[] | null): string[] | null {
  if (fileIds && fileIds.length > 0) {
    return fileIds.map((fileId: string) => fileUrl(fileId));
  }
  return [];
}
