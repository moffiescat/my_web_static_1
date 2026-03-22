const btn = document.getElementById('btn');
btn.addEventListener('click', () => {
    alert('IDEA开发的网页部署成功啦～');
});

function displayDateTime() {
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    const day = now.getDate();
    const dateTimeElement = document.getElementById('date-time');
    dateTimeElement.textContent = `当前日期：${year}年${month}月${day}日`;
}

async function loadProfile() {
    try {
        const response = await fetch('/api/profile');
        const profile = await response.json();
        
        document.getElementById('name').textContent = `姓名：${profile.name}`;
        document.getElementById('age').textContent = `年龄：${profile.age}`;
        document.getElementById('profession').textContent = `职业：${profile.profession}`;
        document.getElementById('email').textContent = `邮箱：${profile.email}`;
    } catch (error) {
        console.error('获取个人信息失败:', error);
    }
}

function updateAuthUI() {
    const username = localStorage.getItem('username');
    const email = localStorage.getItem('email');
    const userInfo = document.getElementById('userInfo');
    const authButtons = document.getElementById('authButtons');
    
    if (username) {
        userInfo.textContent = `欢迎，${username} (${email})`;
        authButtons.innerHTML = '<button class="logout-btn" onclick="logout()">退出登录</button>';
    } else {
        userInfo.textContent = '';
        authButtons.innerHTML = '<a href="login.html" class="login-link">登录</a>';
    }
}

function logout() {
    localStorage.removeItem('username');
    localStorage.removeItem('email');
    updateAuthUI();
    window.location.href = 'login.html';
}

// 检查登录状态
function checkLogin() {
    const username = localStorage.getItem('username');
    if (!username) {
        window.location.href = 'login.html';
    }
}

// 首页需要登录才能访问
if (window.location.pathname === '/' || window.location.pathname === '/index.html') {
    checkLogin();
}

const jokes = [
    "为什么程序员喜欢用黑暗模式？因为光天化日之下，Bug 无处遁形。",
    "程序员的三大谎言：1. 我马上就好 2. 我已经测试过了 3. 这个代码很简单",
    "产品经理：这个功能应该很简单，就像按一下按钮一样。程序员：那你自己按啊！",
    "有一天，一个程序员的妻子让他去超市买东西：买一斤包子，如果看到鸡蛋，买两斤。结果他买了两斤包子，因为他看到了鸡蛋。",
    "程序员的墓志铭：while(1) { sleep(); }",
    "为什么程序员不喜欢去海滩？因为那里全是沙（bug）。",
    "产品经理：我们需要一个既能飞又能游泳的产品。程序员：那是鸭子。",
    "程序员的口头禅：这在我电脑上是好的！",
    "为什么程序员总是混淆万圣节和圣诞节？因为 Oct 31 == Dec 25（八进制31等于十进制25）。",
    "一个程序员走进酒吧，要了 1.0000000000 杯啤酒。酒保说：你要的是一杯啤酒吧？程序员说：对啊，我就是要精确的一杯。"
];

function loadJoke() {
    const jokeText = document.getElementById('jokeText');
    const randomJoke = jokes[Math.floor(Math.random() * jokes.length)];
    jokeText.textContent = randomJoke;
}

document.getElementById('refreshJoke').addEventListener('click', loadJoke);

displayDateTime();
loadProfile();
loadJoke();
updateAuthUI();
