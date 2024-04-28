<template>
  <el-upload
    v-model="image"
    :http-request="handleUpload"
    :show-file-list="false"
    list-type="picture-card"
  >
    <img v-if="image.url" :src="image.url" alt="" style="width: 100%" />
    <el-icon v-else>
      <Plus />
    </el-icon>
  </el-upload>
</template>

<script lang="ts" setup>
import { Plus } from "@element-plus/icons-vue";
import axios from "@/utils/http.js";
import { UploadFileObj } from "./UploadFileObj";
import { UploadRequestOptions } from "element-plus";

const handleUpload = async (options: UploadRequestOptions) => {
  const fileFormData = new FormData();
  fileFormData.append("file", options.file);
  fileFormData.append("fileName", options.file.name);
  let uploadResponse = await axios.request({
    url: "/kharazim-api/system/file/upload",
    method: "post",
    data: fileFormData,
    headers: { "Content-Type": "multipart/form-data" },
  });
  const { fileId, url } = uploadResponse.data.data;
  image.value.fileId = fileId;
  image.value.name = options.file.name;
  image.value.url = url;
};

const image = defineModel<UploadFileObj>();
</script>