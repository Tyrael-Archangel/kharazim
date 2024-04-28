<template>
  <el-form :model="formData">
    <el-form-item label="多图片上传">
      <MultiImageUpload v-model="formData.fileList" />
    </el-form-item>
    <el-form-item label="单图片上传">
      <SingleImageUpload v-model="formData.image" />
    </el-form-item>
    <div class="dialog-footer">
      <el-button type="primary" @click="confirmForm">确定</el-button>
    </div>
  </el-form>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import { UploadFileObj } from "@/components/upload/UploadFileObj";
import MultiImageUpload from "@/components/upload/MultiImageUpload.vue";
import SingleImageUpload from "@/components/upload/SingleImageUpload.vue";
import { ElMessageBox } from "element-plus";

const formData = ref({
  fileList: [] as UploadFileObj[],
  image: {} as UploadFileObj,
});

function confirmForm() {
  console.log(formData.value);
  ElMessageBox.confirm(formData.value, "表单内容", {
    confirmButtonText: "OK",
    draggable: true,
    width: "1500px",
  });
}
</script>
<style scoped></style>