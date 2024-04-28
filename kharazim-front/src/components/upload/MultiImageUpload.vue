<template>
  <el-upload
    v-model:file-list="image"
    :http-request="handleUpload"
    list-type="picture-card"
    multiple
  >
    <el-icon>
      <Plus />
    </el-icon>

    <template #file="{ file }">
      <div>
        <img :src="file.url" alt="" class="el-upload-list__item-thumbnail" />
        <span class="el-upload-list__item-actions">
          <span
            class="el-upload-list__item-preview"
            @click="handlePictureCardPreview(file)"
          >
            <el-icon><zoom-in /></el-icon>
          </span>
          <span class="el-upload-list__item-delete" @click="handleRemove(file)">
            <el-icon><Delete /></el-icon>
          </span>
        </span>
      </div>
    </template>
  </el-upload>

  <el-dialog v-model="dialogVisible">
    <img :src="dialogImageUrl" alt="Preview Image" width="100%" />
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import { Delete, Plus, ZoomIn } from "@element-plus/icons-vue";
import axios from "@/utils/http.js";
import { UploadFileObj } from "./UploadFileObj";
import { UploadFile, UploadRawFile, UploadRequestOptions } from "element-plus";

const dialogImageUrl = ref("");
const dialogVisible = ref(false);

const handleRemove = (rowFile: UploadRawFile) => {
  const fileIndex = image.value.findIndex((file) => file.uid == rowFile.uid);
  image.value.splice(fileIndex, 1);
};

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url!;
  dialogVisible.value = true;
};

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
  for (let fileItem of image.value) {
    if (fileItem.uid == options.file.uid) {
      fileItem.fileId = fileId;
      fileItem.name = options.file.name;
      fileItem.url = url;
      break;
    }
  }
};

const image = defineModel<UploadFileObj[]>();
</script>