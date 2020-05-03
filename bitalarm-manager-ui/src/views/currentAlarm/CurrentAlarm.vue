<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>数据查询</el-breadcrumb-item>
      <el-breadcrumb-item>当前报警查询</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片工作区 -->
    <el-card class="box-card">
      <!-- 搜索与添加区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-input
              placeholder="请输入报警设置ID，仅支持精确查询"
              class="input-with-select"
              v-model="id2Search"
              clearable
              oninput="this.value=this.value.replace(/[^\d.]/g,'');"
            >
              <el-button slot="append" icon="el-icon-search" @click="handleIdSearch"></el-button>
            </el-input>
          </el-col>
          <el-col :span="2">
            <el-button
              class="top-el-button"
              :type="formattedButtonType"
              @click="handleMonitor">
              监视
            </el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="currentAlarm.data"
        stripe
        style="width: 100%">
        <el-table-column
          prop="key.long_id"
          label="ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="point_id"
          label="数据点ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="index"
          label="报警索引"
          width="120px">
        </el-table-column>
        <el-table-column
          prop="alarm_message"
          label="当前报警">
        </el-table-column>
        <el-table-column
          prop="alarm_type"
          label="报警类型"
          :formatter="alarmTypeFormatter"
          width="120px">
        </el-table-column>
        <el-table-column
          prop="happened_date"
          label="发生时间"
          :formatter="timestampFormatter"
          width="180px">
        </el-table-column>
      </el-table>
      <!-- 分页信息区域 -->
      <el-row type="flex" justify="end">
        <el-col>
          <div class="pagination-container">
            <el-pagination
              background
              layout="prev, pager, next"
              :page-size="pageSize"
              :total="parseInt(currentAlarm.count)"
              :hide-on-single-page="true"
              :current-page.sync="currentPage"
              @current-change="onPageChanged">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import {
  all, exists, inspect,
} from '../../api/currentAlarm';

import {
  all as allType,
} from '../../api/alarmTypeIndicator';

export default {
  name: 'CurrentAlarm',
  data() {
    return {
      currentAlarm: {},
      pageSize: 15,
      currentPage: 1,
      id2Search: '',
      timer: null,
      useTimer: true,
      alarmTypeMap: new Map(),
      alarmTypeOptions: [],
      monitorEnabled: false,
    };
  },
  created() {
    this.lookupAll();
    this.lookupAllType();
  },
  methods: {
    onPageChanged() {
      this.lookupAll();
    },
    lookupAll() {
      all(this.currentPage - 1, this.pageSize)
        .then((res) => {
          if (res.meta.code !== 0) {
            this.$message({
              showClose: true,
              message: '请求异常，请稍后再试',
              center: true,
            });
            return null;
          }
          // 当查询的页数大于总页数，自动查询最后一页。
          if (res.data.current_page >= res.data.total_pages && res.data.total_pages > 0) {
            return all(res.data.total_pages - 1, this.pageSize);
          }
          this.currentAlarm = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        })
        .catch((err) => {
          this.$message({
            showClose: true,
            message: `通信错误，异常信息: ${err.message}`,
            type: 'error',
            center: true,
          });
          return null;
        })
        .then((res) => {
          if (res == null) {
            return null;
          }
          if (res.meta.code !== 0) {
            this.$message({
              showClose: true,
              message: '请求异常，请稍后再试',
              center: true,
            });
            return null;
          }
          this.currentAlarm = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupAllType() {
      allType(0, 127)
        .then((res) => {
          if (res.meta.code !== 0) {
            this.$message({
              showClose: true,
              message: '请求异常，请稍后再试',
              center: true,
            });
            return null;
          }
          const alarmTypeMap = new Map();
          const alarmTypeOptions = [];
          res.data.data.forEach((indicator) => {
            alarmTypeMap.set(indicator.key.byte_id, indicator.label);
            alarmTypeOptions.push({ value: indicator.key.byte_id, label: indicator.label });
          });
          this.alarmTypeMap = alarmTypeMap;
          this.alarmTypeOptions = alarmTypeOptions;
          return null;
        })
        .catch((err) => {
          this.$message({
            showClose: true,
            message: `通信错误，异常信息: ${err.message}`,
            type: 'error',
            center: true,
          });
          return null;
        });
    },
    handleIdSearch() {
      if (this.id2Search === '') {
        this.lookupAll();
      } else {
        exists(this.id2Search)
          .then((res) => {
            if (res.data) {
              return inspect(this.id2Search);
            }
            this.$message({
              showClose: true,
              message: `报警设置 ${this.id2Search} 不存在`,
              type: 'warning',
              center: true,
            });
            return null;
          })
          .catch((err) => {
            this.$message({
              showClose: true,
              message: `通信错误，异常信息: ${err.message}`,
              type: 'error',
              center: true,
            });
            return null;
          })
          .then((res) => {
            if (res == null) {
              return null;
            }
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }

            this.currentAlarm.count = 1;
            this.currentAlarm.current_page = 0;
            this.currentAlarm.data = [res.data];
            this.currentAlarm.rows = this.pageSize;
            this.currentAlarm.totle_pages = 1;
            this.currentPage = 1;
            return null;
          });
      }
    },
    handleMonitor() {
      this.monitorEnabled = !this.monitorEnabled;
      this.refreshTimer();
    },
    alarmTypeFormatter(row) {
      if (this.alarmTypeMap.has(row.alarm_type)) {
        return this.alarmTypeMap.get(row.alarm_type);
      }
      return row.alarm_type;
    },
    timestampFormatter(row) {
      const timestamp = row.happened_date;
      // 时间戳为10位需*1000，时间戳为13位的话不需乘1000 var date = new Date(timestamp*1000);
      const date = new Date(timestamp);
      const Y = `${date.getFullYear()}-`;
      const M = `${date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-`;
      const D = `${date.getDate()} `;
      const h = `${date.getHours()}:`;
      const m = `${date.getMinutes()}:`;
      const s = date.getSeconds();
      return Y + M + D + h + m + s;
    },
    refreshTimer() {
      if (this.monitorEnabled) {
        this.timer = setInterval(() => {
          if (this.id2Search === '') {
            this.lookupAll();
          }
        }, 1000);
      } else if (this.timer != null) {
        clearInterval(this.timer);
      }
    },
    closeMonitorDialog() {
      this.monitorVisible = false;
      this.refreshTimer();
    },
  },
  computed: {
    formattedDate() {
      const timestamp = this.anchorCurrentAlarm.happened_date;
      // 时间戳为10位需*1000，时间戳为13位的话不需乘1000 var date = new Date(timestamp*1000);
      const date = new Date(timestamp);
      const Y = `${date.getFullYear()}-`;
      const M = `${date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-`;
      const D = `${date.getDate()} `;
      const h = `${date.getHours()}:`;
      const m = `${date.getMinutes()}:`;
      const s = date.getSeconds();
      return Y + M + D + h + m + s;
    },
    formattedAlarmType() {
      if (this.alarmTypeMap.has(this.anchorCurrentAlarm.alarm_type)) {
        return this.alarmTypeMap.get(this.anchorCurrentAlarm.alarm_type);
      }
      return this.anchorCurrentAlarm.alarm_type;
    },
    formattedButtonType() {
      if (this.monitorEnabled) {
        return 'primary';
      }
      return '';
    },
  },
};
</script>

<style scoped>
  .pagination-container {
    text-align: center;
  }

  .top-el-button {
    width: 100%;
  }
</style>
