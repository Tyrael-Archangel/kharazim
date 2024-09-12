<template>
  <div>
    <el-form :inline="true" :model="pageRequest" class="page-form-block">
      <el-form-item label="名称" prop="name">
        <el-input v-model="pageRequest.name" clearable placeholder="名称" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="pageRequest.status" clearable placeholder="状态">
          <el-option label="正常经营" value="NORMAL" />
          <el-option label="已关闭" value="CLOSED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadClinic">查询</el-button>
      </el-form-item>
      <el-link
        :href="
          `/kharazim-api/clinic/export?name=${pageRequest.name}` +
          (pageRequest.status ? `&status=${pageRequest.status}` : '') +
          `&${ACCESS_TOKEN}=${getToken()}`
        "
        :underline="false"
        style="float: right"
      >
        <el-button plain>导出</el-button>
      </el-link>
    </el-form>
  </div>
  <div></div>
  <div class="clinic-card-area">
    <el-card v-for="clinic in clinicPageResponse.data" class="clinic-card">
      <template #header>
        <el-text size="large" tag="b">{{ clinic.name }}</el-text>
        <el-text size="default">{{ " " + clinic.englishName }}</el-text>
      </template>
      <div class="clinic-image-block">
        <el-image
          v-if="clinic.imageUrl"
          :preview-src-list="[clinic.imageUrl]"
          :src="clinic.imageUrl"
          fit="cover"
          preview-teleported
        >
        </el-image>
      </div>
      <template #footer>
        <el-descriptions>
          <el-descriptions-item label="诊所编码">
            {{ clinic.code }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="clinic.status === 'NORMAL' ? 'success' : 'danger'">
              {{ clinic.statusName }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </template>
    </el-card>
  </div>
  <div class="pagination-block">
    <el-pagination
      v-model:current-page="pageRequest.pageNum"
      v-model:page-size="pageRequest.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :total="clinicPageResponse.totalCount"
      background
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="loadClinic"
      @current-change="loadClinic"
    />
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import axios from "@/utils/http.js";
import { ACCESS_TOKEN, getToken } from "@/utils/auth.js";
import { AxiosResponse } from "axios";

interface ClinicData {
  code: string;
  name: string;
  englishName: string;
  image: string;
  imageUrl: string;
  status: string;
  statusName: string;
}

const clinicPageResponse = ref({
  data: [] as ClinicData[],
  totalCount: 0,
});

const pageRequest = reactive({
  name: "",
  status: "",
  pageNum: 1,
  pageSize: 20,
});

function loadClinic() {
  axios
    .get("/kharazim-api/clinic/page", { params: toRaw(pageRequest) })
    .then((response: AxiosResponse) => {
      clinicPageResponse.value = response.data;
    });
}

onMounted(() => loadClinic());
</script>

<style scoped>
.clinic-card-area {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}

.clinic-card {
  width: 320px;
  margin: 12px;
}

.clinic-image-block {
  height: 150px;
  align-content: center;
}
</style>
