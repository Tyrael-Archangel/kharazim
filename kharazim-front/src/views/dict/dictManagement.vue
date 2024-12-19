<template>
  <h1>字典管理</h1>
  <div>
    <el-form :inline="true" :model="pageRequest" class="page-form-block" @keyup.enter="loadDictionaries">
      <el-form-item label="关键字">
        <el-input v-model="pageRequest.keywords" clearable placeholder="关键字" />
      </el-form-item>
      <el-form-item class="page-form-block-search-block">
        <el-button type="primary" @click="loadDictionaries">查询</el-button>
        <el-button type="primary" @click="resetAndReloadDictionaries"> 重置 </el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <div>
      <el-table :data="dictPageData.data" border style="width: 100%; margin-top: 10px">
        <el-table-column align="center" type="index" width="70" />
        <el-table-column label="字典编码" prop="code" width="270" />
        <el-table-column label="字典描述信息" prop="desc" />
        <el-table-column align="center" label="是否允许编辑字典项" width="170">
          <template v-slot="{ row: item }">
            <el-icon v-if="item.allowModifyItem" color="green" size="20">
              <CircleCheck />
            </el-icon>
            <el-icon v-else color="red" size="20">
              <CircleClose />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template v-slot="{ row: item }">
            <el-button link size="small" type="primary" @click="showDictItems(item)">字典项 </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-block">
      <el-pagination
        v-model:current-page="pageRequest.pageIndex"
        v-model:page-size="pageRequest.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="dictPageData.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadDictionaries"
        @current-change="loadDictionaries"
      />
    </div>
  </div>
  <el-dialog
    v-model="dictItemsVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :title="'字典项 - ' + currentViewDict?.code"
    width="800"
  >
    <div>
      <el-button
        v-if="currentViewDict && currentViewDict.allowModifyItem"
        size="small"
        style="float: right"
        @click="startAddDictItem"
        >添加字典项
      </el-button>
    </div>
    <el-table :data="dictItems">
      <el-table-column label="字典项键" min-width="150" property="key">
        <template v-slot="{ row: item }">
          <el-input v-if="item.editing" v-model="item.key" />
          <el-text v-else>{{ item.key }}</el-text>
        </template>
      </el-table-column>
      <el-table-column label="字典项值" min-width="200" property="value">
        <template v-slot="{ row: item }">
          <el-input v-if="item.editing" v-model="item.value" />
          <el-text v-else>{{ item.value }}</el-text>
        </template>
      </el-table-column>
      <el-table-column align="center" label="排序" min-width="100" property="sort">
        <template v-slot="{ row: item }">
          <el-input-number
            v-if="item.editing"
            v-model="item.sort"
            :controls="false"
            :max="9999"
            :min="1"
            :precision="0"
            :step="1"
            step-strictly
            style="width: 80px"
          />
          <el-text v-else>{{ item.sort }}</el-text>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" min-width="150">
        <template v-slot="{ row: item }">
          <el-button
            v-if="currentViewDict && currentViewDict.allowModifyItem && !item.editing"
            link
            size="small"
            type="primary"
            @click="startEditDictItem(item)"
          >
            编辑
          </el-button>
          <el-button v-if="item.editing" size="small" type="primary" @click="saveEditDictItem(item)"> 保存 </el-button>
          <el-button v-if="item.editing" size="small" @click="cancelEditDictItem(item)"> 取消 </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";
import { CircleCheck, CircleClose } from "@element-plus/icons-vue";

interface Dict {
  code: string;
  desc: string;
  allowModifyItem: boolean;
}

interface DictItem {
  id: number | null;
  dictCode: string | undefined;
  key: string;
  value: string | null;
  sort: number;
}

interface DictItemView extends DictItem {
  id: number | null;
  dictCode: string | undefined;
  key: string;
  value: string | null;
  sort: number;
  editing: boolean; // 是否正处于编辑状态
}

const dictPageData = ref({ totalCount: 0, data: [] as Dict[] });

const initPageRequest = {
  keywords: "",
  pageIndex: 1,
  pageSize: 10,
};

const pageRequest = reactive({ ...initPageRequest });

function loadDictionaries() {
  axios.get("/kharazim-api/system/dict/page", { params: toRaw(pageRequest) }).then((response: AxiosResponse) => {
    dictPageData.value = response.data;
  });
}

function resetAndReloadDictionaries() {
  Object.assign(pageRequest, initPageRequest);
  loadDictionaries();
}

const currentViewDict = ref<Dict>();
const dictItemsVisible = ref(false);
const dictItems = ref<DictItemView[]>([]);

function showDictItems(dict: Dict) {
  currentViewDict.value = dict;
  dictItems.value = [];
  dictItemsVisible.value = true;
  loadDictItems(dict);
}

function loadDictItems(dict: Dict) {
  axios.get(`/kharazim-api/system/dict/${dict.code}/items`).then((response: AxiosResponse) => {
    const dictItemsData = (response.data.data || []) as DictItemView[];
    dictItemsData.forEach((item: DictItemView) => (item.editing = false));
    dictItems.value = dictItemsData;
  });
}

const dictItemViewEditBackUp = new Map();

function startEditDictItem(dictItem: DictItemView) {
  dictItemViewEditBackUp.set(dictItem.id, {
    key: dictItem.key,
    value: dictItem.value,
    sort: dictItem.sort,
  });
  dictItem.editing = true;
}

function saveEditDictItem(dictItem: DictItemView) {
  let saveDictItemRequest = {
    dictCode: dictItem.dictCode,
    key: dictItem.key,
    value: dictItem.value,
    sort: dictItem.sort,
  };
  const itemId = dictItem.id as number | undefined | null;
  if (itemId !== undefined && itemId !== null) {
    axios.put(`/kharazim-api/system/dict/item/${dictItem.id}`, saveDictItemRequest).then(() => {
      dictItem.editing = false;
    });
  } else {
    axios.post(`/kharazim-api/system/dict/item`, saveDictItemRequest).then((response: AxiosResponse) => {
      dictItem.id = response.data.data;
      dictItem.editing = false;
    });
  }
}

function cancelEditDictItem(dictItem: DictItemView) {
  const itemId = dictItem.id as number | undefined | null;
  if (itemId !== undefined && itemId !== null) {
    // 取消编辑
    let { key, value, sort } = dictItemViewEditBackUp.get(itemId);
    dictItem.key = key;
    dictItem.value = value;
    dictItem.sort = sort;
    dictItem.editing = false;
    dictItemViewEditBackUp.delete(itemId);
  } else {
    // 取消新增
    dictItems.value = dictItems.value.filter((x) => x !== dictItem);
  }
}

function startAddDictItem() {
  let maxSort = Math.max(...dictItems.value.map((dictItem) => dictItem.sort), 0);
  dictItems.value.push({
    id: null,
    dictCode: currentViewDict.value?.code,
    key: "",
    value: "",
    sort: maxSort + 1,
    editing: true,
  });
}

onMounted(async () => {
  loadDictionaries();
});
</script>

<style scoped></style>
