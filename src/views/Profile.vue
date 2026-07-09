<script setup>
const stats = [
  { label: '规划行程', value: '--', unit: '次' },
  { label: '足迹城市', value: '--', unit: '座' },
  { label: '旅行天数', value: '--', unit: '天' },
]

const menus = [
  { icon: '📋', label: '我的行程', desc: '查看历史规划记录' },
  { icon: '❤️', label: '收藏目的地', desc: '你喜欢的城市' },
  { icon: '🔔', label: '消息通知', desc: '旅行提醒与更新' },
  { icon: '⚙️', label: '设置', desc: '偏好与账号设置' },
  { icon: '❓', label: '帮助与反馈', desc: '遇到问题？联系我们' },
  { icon: 'ℹ️', label: '关于', desc: '版本 1.0.0' },
]
</script>

<template>
  <div class="profile-page">
    <!-- Header -->
    <div class="profile-header">
      <span>我的</span>
    </div>

    <!-- Avatar Card -->
    <div class="avatar-card">
      <div class="avatar-circle">🧳</div>
      <div class="avatar-info">
        <div class="avatar-name">旅行者</div>
        <div class="avatar-sub">探索世界的每个角落</div>
      </div>
      <van-button size="small" plain round type="primary">编辑资料</van-button>
    </div>

    <!-- Stats -->
    <div class="stats-row">
      <div v-for="stat in stats" :key="stat.label" class="stat-item">
        <div class="stat-value">{{ stat.value }}<span class="stat-unit">{{ stat.unit }}</span></div>
        <div class="stat-label">{{ stat.label }}</div>
      </div>
    </div>

    <!-- Menu -->
    <div class="section-title">功能菜单</div>
    <van-cell-group inset>
      <van-cell
        v-for="menu in menus"
        :key="menu.label"
        :title="menu.icon + '  ' + menu.label"
        :label="menu.desc"
        is-link
        clickable
      />
    </van-cell-group>

    <div style="height: 24px"></div>
  </div>
</template>

<style scoped>
.profile-page {
  background: #f7f8fa;
  min-height: 100%;
}

.profile-header {
  padding: 16px;
  background: #fff;
  font-size: 18px;
  font-weight: 600;
  color: #1a1a2e;
  border-bottom: 1px solid #f0f0f0;
}

.avatar-card {
  display: flex;
  align-items: center;
  gap: 14px;
  margin: 12px;
  padding: 18px;
  background: linear-gradient(135deg, #1a73e8, #6a1b9a);
  border-radius: 14px;
  color: #fff;
}

.avatar-circle {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.25);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  flex-shrink: 0;
}

.avatar-info {
  flex: 1;
}

.avatar-name {
  font-size: 17px;
  font-weight: 700;
  margin-bottom: 4px;
}

.avatar-sub {
  font-size: 12px;
  opacity: 0.8;
}

.stats-row {
  display: flex;
  background: #fff;
  margin: 0 12px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0,0,0,0.06);
}

.stat-item {
  flex: 1;
  padding: 16px 8px;
  text-align: center;
  border-right: 1px solid #f0f0f0;
}

.stat-item:last-child {
  border-right: none;
}

.stat-value {
  font-size: 20px;
  font-weight: 700;
  color: #1a1a2e;
}

.stat-unit {
  font-size: 12px;
  font-weight: 400;
  color: #999;
  margin-left: 2px;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}
</style>
