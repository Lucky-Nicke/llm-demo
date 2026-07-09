<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const active = ref(0)

watch(
  () => route.path,
  (path) => {
    if (path === '/') active.value = 0
    else if (path === '/chat') active.value = 1
    else if (path === '/profile') active.value = 2
  },
  { immediate: true }
)
</script>

<template>
  <div>
    <div class="page-wrap">
      <router-view />
    </div>
    <van-tabbar v-model="active" route :fixed="true" :placeholder="false" safe-area-inset-bottom active-color="#1989fa">
      <van-tabbar-item to="/" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item to="/chat" icon="chat-o">对话</van-tabbar-item>
      <van-tabbar-item to="/profile" icon="contact-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>
