<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>数据查询</el-breadcrumb-item>
      <el-breadcrumb-item>报警信息查询</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片工作区 -->
    <el-card class="box-card">
      <!-- 搜索与添加区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="24">
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
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="alarmInfo.data"
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
          label="报警信息">
        </el-table-column>
        <el-table-column
          prop="alarm_type"
          label="报警类型"
          :formatter="alarmTypeFormatter"
          width="120px">
        </el-table-column>
        <el-table-column
          prop="alarming"
          label="正在报警"
          :formatter="alarmingFormatter"
          width="120px">
        </el-table-column>
        <el-table-column
          prop="happened_date"
          label="发生时间"
          :formatter="timestampFormatter"
          width="180px">
        </el-table-column>
        <el-table-column label="操作" :width="100">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                type="success"
                @click="handleShowMonitorDialog(scope.row)">监视
              </el-button>
            </el-button-group>
          </template>
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
              :total="parseInt(alarmInfo.count)"
              :hide-on-single-page="true"
              :current-page.sync="currentPage"
              @current-change="onPageChanged">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 监视对话框 -->
    <el-dialog
      title="监视"
      :visible.sync="monitorVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        status-icon
        :model="anchorAlarmInfo"
        ref="monitorForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorAlarmInfo.key"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="数据点ID" prop="point_id">
          <el-input
            v-model="anchorAlarmInfo.point_id"
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="报警索引" prop="index">
          <el-input
            v-model="anchorAlarmInfo.index"
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="报警信息" prop="alarm_message">
          <el-input
            v-model="anchorAlarmInfo.alarm_message"
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="报警类型" prop="alarm_type">
          <el-input
            v-model="formattedAlarmType"
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="发生时间" prop="happened_date">
          <el-input
            v-model="formattedDate"
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="正在报警" prop="alarming">
          <el-input
            v-model="formattedAlarming"
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="定时刷新" prop="refresh">
          <el-switch
            v-model="useTimer"
            active-text="自动刷新"
            inactive-text="不刷新"
            @change="refreshTimer"
            >
          </el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closeMonitorDialog">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  all, exists, inspect,
} from '../../api/alarmInfo';

import {
  all as allType,
} from '../../api/alarmTypeIndicator';

export default {
  name: 'AlarmInfo',
  data() {
    return {
      alarmInfo: {},
      pageSize: 15,
      currentPage: 1,
      monitorVisible: false,
      anchorAlarmInfo: {
        key: '',
        point_id: '',
        index: '',
        alarm_message: '',
        alarm_type: '',
        happened_date: '',
        alarming: '',
      },
      id2Search: '',
      timer: null,
      useTimer: true,
      alarmTypeMap: new Map(),
      alarmTypeOptions: [],
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
          this.alarmInfo = res.data;
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
          this.alarmInfo = res.data;
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

            this.alarmInfo.count = 1;
            this.alarmInfo.current_page = 0;
            this.alarmInfo.data = [res.data];
            this.alarmInfo.rows = this.pageSize;
            this.alarmInfo.totle_pages = 1;
            this.currentPage = 1;
            return null;
          });
      }
    },
    handleShowMonitorDialog(row) {
      if (this.$refs.monitorForm !== undefined) {
        this.$refs.monitorForm.resetFields();
      }
      this.anchorAlarmInfo.key = row.key.long_id;
      this.anchorAlarmInfo.point_id = row.point_id;
      this.anchorAlarmInfo.index = row.index;
      this.anchorAlarmInfo.alarm_message = row.alarm_message;
      this.anchorAlarmInfo.alarm_type = row.alarm_type;
      this.anchorAlarmInfo.happened_date = row.happened_date;
      this.anchorAlarmInfo.alarming = row.alarming;
      this.monitorVisible = true;
      this.refreshTimer();
    },
    alarmingFormatter(row) {
      return row.alarming ? '是' : '否';
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
      if (this.useTimer && this.monitorVisible) {
        this.timer = setInterval(() => {
          inspect(this.anchorAlarmInfo.key)
            .then((res) => {
              console.log(res);
              if (res.meta.code !== 0) {
                return null;
              }
              this.anchorAlarmInfo.key = res.data.key.long_id;
              this.anchorAlarmInfo.point_id = res.data.point_id;
              this.anchorAlarmInfo.index = res.data.index;
              this.anchorAlarmInfo.alarm_message = res.data.alarm_message;
              this.anchorAlarmInfo.alarm_type = res.data.alarm_type;
              this.anchorAlarmInfo.happened_date = res.data.happened_date;
              this.anchorAlarmInfo.alarming = res.data.alarming;
              return null;
            }).catch(() => null);
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
      const timestamp = this.anchorAlarmInfo.happened_date;
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
    formattedAlarming() {
      return this.anchorAlarmInfo.alarming ? '是' : '否';
    },
    formattedAlarmType() {
      if (this.alarmTypeMap.has(this.anchorAlarmInfo.alarm_type)) {
        return this.alarmTypeMap.get(this.anchorAlarmInfo.alarm_type);
      }
      return this.anchorAlarmInfo.alarm_type;
    },
  },
};
</script>

<style scoped>
  .pagination-container {
    text-align: center;
  }
</style>
