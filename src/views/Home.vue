<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const cities = [
  { name: '北京', tag: '皇家文化', emoji: '🏛️', bg: 'linear-gradient(135deg,#667eea,#764ba2)' },
  { name: '上海', tag: '都市时尚', emoji: '🌆', bg: 'linear-gradient(135deg,#f093fb,#f5576c)' },
  { name: '成都', tag: '美食之都', emoji: '🐼', bg: 'linear-gradient(135deg,#4facfe,#00f2fe)' },
  { name: '三亚', tag: '阳光海滩', emoji: '🏖️', bg: 'linear-gradient(135deg,#43e97b,#38f9d7)' },
  { name: '西安', tag: '古都华章', emoji: '🏯', bg: 'linear-gradient(135deg,#fa709a,#fee140)' },
  { name: '杭州', tag: '人间天堂', emoji: '🍵', bg: 'linear-gradient(135deg,#a18cd1,#fbc2eb)' },
]

const features = [
  { icon: '🤖', title: 'AI 智能规划', desc: '基于大语言模型，秒速生成专业行程方案' },
  { icon: '📅', title: '每日行程详情', desc: '早中晚合理安排，景点交通一网打尽' },
  { icon: '💰', title: '预算精确把控', desc: '住宿餐饮门票交通，费用分类清晰透明' },
]

function goToChat(city) {
  router.push({ path: '/chat', query: city ? { city } : {} })
}
</script>

<template>
  <div class="home">
    <!-- Header -->
    <div class="home-header">
      <span class="header-logo">✈️</span>
      <span class="header-title">旅行规划助手</span>
    </div>

    <!-- Hero Banner -->
    <div class="hero-banner">
      <div class="hero-content">
        <div class="hero-tag">AI 驱动 · 智能规划</div>
        <h1 class="hero-title">发现最美目的地</h1>
        <p class="hero-sub">告诉我城市、天数和预算<br>AI 为你定制专属行程</p>
        <van-button type="primary" round size="normal" class="hero-btn" @click="goToChat(null)">
          立即开始规划 →
        </van-button>
      </div>
    </div>

    <!-- Popular Cities -->
    <div class="section-title">热门目的地</div>
    <div class="cities-grid">
      <div
        v-for="city in cities"
        :key="city.name"
        class="city-card"
        :style="{ background: city.bg }"
        @click="goToChat(city.name)"
      >
        <div class="city-emoji">{{ city.emoji }}</div>
        <div class="city-name">{{ city.name }}</div>
        <div class="city-tag">{{ city.tag }}</div>
      </div>
    </div>

    <!-- Features -->
    <div class="section-title">为什么选择我们</div>
    <div class="features-list">
      <div v-for="feat in features" :key="feat.title" class="feature-item">
        <div class="feature-icon">{{ feat.icon }}</div>
        <div class="feature-body">
          <div class="feature-title">{{ feat.title }}</div>
          <div class="feature-desc">{{ feat.desc }}</div>
        </div>
      </div>
    </div>

    <!-- Bottom padding -->
    <div style="height: 24px"></div>
  </div>
</template>

<style scoped>
.home {
  background: #f7f8fa;
  min-height: 100%;
}

.home-header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px;
  background: #fff;
  font-size: 18px;
  font-weight: 600;
  color: #1a1a2e;
  border-bottom: 1px solid #f0f0f0;
}

.header-logo {
  font-size: 22px;
}

/* Hero */
.hero-banner {
  margin: 12px;
  border-radius: 16px;
  background: linear-gradient(135deg, #1a73e8 0%, #0d47a1 60%, #6a1b9a 100%);
  padding: 32px 20px 28px;
  color: #fff;
  overflow: hidden;
  position: relative;
}

.hero-banner::after {
  content: '🗺️';
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 80px;
  opacity: 0.15;
}

.hero-tag {
  display: inline-block;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 4px 12px;
  font-size: 12px;
  margin-bottom: 12px;
}

.hero-title {
  font-size: 26px;
  font-weight: 700;
  margin-bottom: 8px;
  line-height: 1.2;
}

.hero-sub {
  font-size: 14px;
  opacity: 0.85;
  line-height: 1.6;
  margin-bottom: 20px;
}

.hero-btn {
  background: #fff !important;
  color: #1a73e8 !important;
  border: none !important;
  font-weight: 600;
  padding: 0 24px;
}

/* Cities grid */
.cities-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  padding: 0 12px;
}

.city-card {
  border-radius: 12px;
  padding: 16px 10px 14px;
  color: #fff;
  text-align: center;
  cursor: pointer;
  transition: transform 0.15s, box-shadow 0.15s;
}

.city-card:active {
  transform: scale(0.96);
}

.city-emoji {
  font-size: 28px;
  margin-bottom: 6px;
}

.city-name {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 2px;
}

.city-tag {
  font-size: 11px;
  opacity: 0.85;
}

/* Features */
.features-list {
  display: flex;
  flex-direction: column;
  gap: 2px;
  margin: 0 12px;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  padding: 16px;
  border-bottom: 1px solid #f5f5f5;
}

.feature-item:last-child {
  border-bottom: none;
}

.feature-icon {
  font-size: 28px;
  flex-shrink: 0;
  line-height: 1;
  margin-top: 2px;
}

.feature-title {
  font-size: 15px;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 4px;
}

.feature-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
}
</style>
