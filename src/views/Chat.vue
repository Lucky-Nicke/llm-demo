<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { showToast, showFailToast } from 'vant'

const route = useRoute()

const form = reactive({ city: '', days: 3, budget: 2000 })
const loading = ref(false)
const result = ref(null)
const activeDay = ref(0)

// Accept city query param from Home page shortcut
onMounted(() => {
  if (route.query.city) form.city = route.query.city
})

async function handleSubmit() {
  if (!form.city.trim()) { showToast('请输入目的地城市'); return }
  const days = Number(form.days)
  const budget = Number(form.budget)
  if (!days || days < 1 || days > 30) { showToast('天数请填写 1-30 之间'); return }
  if (!budget || budget < 100) { showToast('预算不能低于 100 元'); return }

  loading.value = true
  result.value = null
  activeDay.value = 0

  try {
    const res = await fetch('/api/travel/recommend', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ city: form.city.trim(), days, budget }),
    })
    if (!res.ok) throw new Error(`服务器错误 ${res.status}`)
    const json = await res.json()
    if (json.code === 200 && json.data?.success) {
      result.value = json.data
    } else {
      throw new Error(json.message || '规划失败，请重试')
    }
  } catch (err) {
    showFailToast(err.message)
  } finally {
    loading.value = false
  }
}

const currentDay = computed(() =>
  result.value?.dailyItinerary?.[activeDay.value] ?? null
)

const slots = computed(() => {
  if (!currentDay.value) return []
  return [
    { key: 'morning',   label: '早上', icon: '🌅', color: '#f39c12', bg: '#fffbf0', data: currentDay.value.morning },
    { key: 'afternoon', label: '下午', icon: '☀️', color: '#1989fa', bg: '#f0f7ff', data: currentDay.value.afternoon },
    { key: 'evening',   label: '晚上', icon: '🌙', color: '#7232dd', bg: '#f8f0ff', data: currentDay.value.evening },
  ]
})

const budgetItems = computed(() => {
  if (!result.value?.budgetBreakdown) return []
  const b = result.value.budgetBreakdown
  const total = result.value.totalBudget || 1
  return [
    { label: '住宿', amount: b.accommodation, color: '#1989fa' },
    { label: '餐饮', amount: b.food,          color: '#07c160' },
    { label: '交通', amount: b.transportation, color: '#ff976a' },
    { label: '门票', amount: b.tickets,       color: '#ee0a24' },
    { label: '其他', amount: b.other,         color: '#7232dd' },
  ].map(item => ({ ...item, pct: Math.round((item.amount / total) * 100) }))
})
</script>

<template>
  <div class="chat-page">
    <!-- Header -->
    <div class="chat-header">
      <span class="chat-header-icon">🗺️</span>
      <span>旅行规划</span>
    </div>

    <!-- Input Form -->
    <div class="form-card">
      <van-cell-group inset>
        <van-field
          v-model="form.city"
          label="目的地"
          placeholder="输入城市名称，如：北京"
          left-icon="location-o"
          clearable
        />
        <van-field
          v-model.number="form.days"
          label="天数"
          type="number"
          placeholder="1-30"
          left-icon="calendar-o"
        >
          <template #right-icon><span class="unit-text">天</span></template>
        </van-field>
        <van-field
          v-model.number="form.budget"
          label="总预算"
          type="number"
          placeholder="最低 100"
          left-icon="gold-coin-o"
        >
          <template #right-icon><span class="unit-text">元</span></template>
        </van-field>
      </van-cell-group>
      <div class="submit-wrap">
        <van-button
          type="primary"
          block
          round
          :loading="loading"
          loading-text="AI 规划中..."
          @click="handleSubmit"
        >
          开始规划行程
        </van-button>
      </div>
    </div>

    <!-- Empty state -->
    <div v-if="!result && !loading" class="empty-state">
      <van-empty
        image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
        description="填写上方信息，AI 将为您生成专属旅行方案"
      />
    </div>

    <!-- Result -->
    <div v-if="result" class="result-wrap">
      <!-- Summary Banner -->
      <div class="result-banner">
        <div class="result-city">📍 {{ result.city }}</div>
        <div class="result-meta">
          <van-tag type="primary" size="medium">{{ result.days }} 天行程</van-tag>
          <van-tag color="#07c160" size="medium">预算 ¥{{ result.totalBudget }}</van-tag>
        </div>
      </div>

      <!-- Day Tabs -->
      <van-tabs v-model:active="activeDay" sticky animated swipeable offset-top="0">
        <van-tab
          v-for="day in result.dailyItinerary"
          :key="day.day"
          :title="day.date"
        >
          <div class="day-content">
            <div v-for="slot in slots" :key="slot.key" class="slot-card" :style="{ background: slot.bg }">
              <div class="slot-header" :style="{ color: slot.color }">
                <span class="slot-icon">{{ slot.icon }}</span>
                <span class="slot-label">{{ slot.label }}</span>
                <span class="slot-spot">{{ slot.data.spot }}</span>
              </div>
              <div class="slot-tags">
                <van-tag plain :color="slot.color">⏱ {{ slot.data.duration }}</van-tag>
                <van-tag plain :color="slot.color">
                  🎫 {{ slot.data.ticket === '0' || slot.data.ticket === 0 ? '免费' : '¥' + slot.data.ticket }}
                </van-tag>
              </div>
              <div class="slot-transport">
                <van-icon name="logistics" />
                {{ slot.data.transportation }}
              </div>
              <div class="slot-desc">{{ slot.data.description }}</div>
            </div>
          </div>
        </van-tab>
      </van-tabs>

      <!-- Budget Breakdown -->
      <div class="section-title">预算分解</div>
      <div class="budget-card">
        <div v-for="item in budgetItems" :key="item.label" class="budget-row">
          <div class="budget-label">{{ item.label }}</div>
          <div class="budget-bar-wrap">
            <div class="budget-bar" :style="{ width: item.pct + '%', background: item.color }"></div>
          </div>
          <div class="budget-amount">¥{{ item.amount }}</div>
          <div class="budget-pct">{{ item.pct }}%</div>
        </div>
      </div>

      <!-- Tips -->
      <div class="section-title">💡 实用提示</div>
      <van-cell-group inset class="tips-group">
        <van-cell
          v-for="(tip, i) in result.tips"
          :key="i"
          :label="tip"
          :title="'提示 ' + (i + 1)"
          title-class="tip-title"
        />
      </van-cell-group>

      <!-- Warnings -->
      <div class="section-title">⚠️ 注意事项</div>
      <van-cell-group inset class="warn-group">
        <van-cell
          v-for="(warn, i) in result.warnings"
          :key="i"
          :label="warn"
          :title="'注意 ' + (i + 1)"
          title-class="warn-title"
        />
      </van-cell-group>

      <div style="height: 24px"></div>
    </div>
  </div>
</template>

<style scoped>
.chat-page {
  background: #f7f8fa;
  min-height: 100%;
}

/* Header */
.chat-header {
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
.chat-header-icon { font-size: 22px; }

/* Form */
.form-card {
  margin: 12px;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0,0,0,0.06);
}
.submit-wrap {
  padding: 12px 16px 16px;
}
.unit-text {
  font-size: 13px;
  color: #999;
}

/* Empty */
.empty-state {
  padding-top: 40px;
}

/* Result */
.result-wrap { padding-bottom: 8px; }

.result-banner {
  margin: 12px;
  padding: 16px;
  background: linear-gradient(135deg, #1a73e8, #6a1b9a);
  border-radius: 12px;
  color: #fff;
}
.result-city {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 10px;
}
.result-meta { display: flex; gap: 8px; }

/* Day content */
.day-content {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.slot-card {
  border-radius: 12px;
  padding: 14px;
}
.slot-header {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
}
.slot-icon { font-size: 18px; }
.slot-label { font-size: 13px; font-weight: 600; }
.slot-spot {
  font-size: 15px;
  font-weight: 700;
  color: #1a1a2e;
  margin-left: 4px;
}
.slot-tags { display: flex; gap: 8px; margin-bottom: 8px; }
.slot-transport {
  font-size: 12px;
  color: #666;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 4px;
}
.slot-desc {
  font-size: 13px;
  color: #555;
  line-height: 1.6;
}

/* Budget */
.budget-card {
  margin: 0 12px 4px;
  background: #fff;
  border-radius: 12px;
  padding: 12px 16px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.06);
}
.budget-row {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 0;
}
.budget-label {
  width: 28px;
  font-size: 13px;
  color: #555;
  flex-shrink: 0;
}
.budget-bar-wrap {
  flex: 1;
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}
.budget-bar {
  height: 100%;
  border-radius: 4px;
  transition: width 0.4s ease;
}
.budget-amount {
  font-size: 13px;
  font-weight: 500;
  color: #333;
  width: 54px;
  text-align: right;
  flex-shrink: 0;
}
.budget-pct {
  font-size: 12px;
  color: #999;
  width: 30px;
  text-align: right;
  flex-shrink: 0;
}

/* Tips / Warnings */
.tips-group :deep(.van-cell__title) { color: #07c160 !important; }
.warn-group :deep(.van-cell__title) { color: #ee0a24 !important; }
</style>
