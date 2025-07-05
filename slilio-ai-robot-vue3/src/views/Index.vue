<template>
  <div class="h-screen flex flex-col overflow-y-auto" ref="chatContainer">
    <!-- 聊天记录区域 -->
    <div class="flex-1 max-w-3xl mx-auto pb-24 pt-4 px-4">
      <!-- 遍历聊天记录 -->
      <template v-for="(chat, index) in chatList" :key="index">
        <!-- 用户提问信息（靠右） -->
        <div v-if="chat.role === 'user'" class="flex justify-end mb-4">
          <div class="quesiton-container">
            <p>{{ chat.content }}</p>
          </div>
        </div>

        <!-- 大模型回复（靠左） -->
        <div v-else class="flex mb-4">
          <!-- 头像 -->
          <div class="flex-shrink-0 mr-3">
            <div class="w-8 h-8 rounded-full flex items-center justify-center border border-gray-200">
              <SvgIcon name="deepseek-logo" customCss="w-5 h-5"></SvgIcon>
            </div>
          </div>
          <!-- 回复的内容 -->
          <div class="p-1 max-w-[80%] mb-2">
            <StreamMarkdownRender :content="chat.content" />
          </div>
        </div>
      </template>
    </div>

    <!-- 提问输入框 -->
    <div class="absolute bottom-0 w-[80%] mb-5 left-1/2 -translate-x-1/2">
      <div class="bg-gray-100 rounded-3xl px-4 py-3 mx-4 border border-gray-200 flex flex-col">
        <textarea v-model="message" placeholder="向AI机器人发送消息" class="bg-transparent border-none outline-none w-full text-sm resize-none min-h-[24px]" rows="2" @input="autoResize" @keydown.enter.prevent="sendMessage" ref="textareaRef"> </textarea>

        <!-- 发送按钮 -->
        <div class="flex justify-end">
          <button @click="sendMessage" :disabled="!message.trim()" class="flex items-center justify-center bg-[#4d6bfe] rounded-full w-8 h-8 border border-[#4d6bfe] hover:bg-[#3b5bef] transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
            <SvgIcon name="up-arrow" customCss="w-5 h-5 text-white"></SvgIcon>
          </button>
        </div>
      </div>
      <!-- 提示文字 -->
      <div class="flex items-center justify-center text-xs text-gray-400 mt-2">内容由 AI 生成，请仔细甄别</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount, nextTick } from "vue";
import SvgIcon from "@/components/SvgIcon.vue";
import StreamMarkdownRender from "@/components/StreamMarkdownRender.vue";

//输入的消息
const message = ref("");

//textarea 引用
const textareaRef = ref(null);
//自动调整文本域高度
const autoResize = () => {
  const textarea = textareaRef.value;
  //文本域存在
  if (textarea) {
    //先将高度重置为 'auto'
    textarea.style.height = "auto";

    //计算高度
    const newHeight = Math.min(textarea.scrollHeight, 300);
    textarea.style.height = newHeight + "px";

    //如果内容超出 300px，则启用滚动
    textarea.style.overflowY = textarea.scrollHeight > 300 ? "auto" : "hidden";
  }
};

//聊天记录 (给个默认的问候语)
const chatList = ref([{ role: "assistant", content: "我是小哈智能 AI 助手！✨ 我可以帮你解答各种问题，无论是学习、工作，还是日常生活中的小困惑，都可以找我聊聊。有什么我可以帮你的吗？😊" }]);

//SSE连接
let eventSource = null;
//发送消息
const sendMessage = async () => {
  //校验发送的消息合法性
  if (!message.value.trim()) return;

  //将用户发送的消息添加到chatList聊天列表中
  const userMessage = message.value.trim();
  chatList.value.push({ role: "user", content: userMessage });

  //点击发送后，清空输入框
  message.value = "";
  //重置输入框高度
  if (textareaRef.value) {
    textareaRef.value.style.height = "auto";
  }

  //页面添加占位消息
  chatList.value.push({ role: "assistant", content: "" });

  try {
    //创建SSE链接
    // eventSource = new EventSource(`http://localhost:8080/v6/ai/generateStream?message=${encodeURIComponent(userMessage)}`);
    // eventSource = new EventSource(`http://localhost:8080/v7/ai/generateStream?message=${encodeURIComponent(userMessage)}&lang=Java`)
    // eventSource = new EventSource(`http://localhost:8080/v7/ai/generateStream2?message=${encodeURIComponent(userMessage)}&lang=Java`)
    eventSource = new EventSource(`http://localhost:8080/v7/ai/generateStream3?message=${encodeURIComponent(userMessage)}&lang=Java`)
    //响应的回答
    let responseText = "";
    //处理消息事件
    // eventSource.onopen = (e) => {
    //     console.log('连接已打开', e);
    // };

    eventSource.onmessage = (event) => {
      console.log("接收到数据: ", event.data + "---");
      //数据不为空
      if (event.data) {
        //处理JSON
        let response = JSON.parse(event.data);
        //持续追加内容
        responseText = responseText + response.v;
        //更新最后一条消息
        chatList.value[chatList.value.length - 1].content = responseText;
        //滚动到最新位置
        scrollToBottom();
      }
    };

    //处理错误
    eventSource.onerror = (error) => {
      //通常 SSE 在完成传输后会触发一次 error 事件，这是正常的
      if (error.eventPhase === EventSource.CLOSED) {
        console.log("SSE 正常关闭");
      } else {
        //否则提示聊天页面提示出错
        chatList.value[chatList.value.length - 1].content = "抱歉，请求出错了，请稍后重试。";
      }
      //关闭SSE
      closeSSE();
      //滚动到最新位置
      scrollToBottom();
    };
  } catch (error) {
    console.error("发送错误消息", error);
    //同时聊天页面提示出错
    chatList.value[chatList.value.length - 1].content = "抱歉，请求出错了，请稍后重试。";
    //滚动到最新位置
    scrollToBottom();
  }
};

//关闭SSE链接
const closeSSE = () => {
  if (eventSource) {
    eventSource.close();
    eventSource = null;
  }
};

//组件卸载时自动关闭连接
onBeforeUnmount(() => {
  closeSSE();
});

// 聊天容器引用
const chatContainer = ref(null);
// 滚动到底部
const scrollToBottom = async () => {
  await nextTick(); // 等待 Vue.js 完成 DOM 更新
  if (chatContainer.value) {
    // 若容器存在
    // 将容器的滚动条位置设置到最底部
    const container = chatContainer.value;
    container.scrollTop = container.scrollHeight;
  }
};
</script>

<style scoped>
.quesiton-container {
  font-size: 16px;
  line-height: 28px;
  color: #262626;
  padding: calc((44px - 28px) / 2) 20px;
  box-sizing: border-box;
  white-space: pre-wrap;
  word-break: break-word;
  background-color: #eff6ff;
  border-radius: 14px;
  max-width: calc(100% - 48px);
  position: relative;
}

/* 聊天内容区域样式 */
.overflow-y-auto {
  scrollbar-color: rgba(0, 0, 0, 0.2) transparent; /* 自定义滚动条颜色 */
}
</style>
