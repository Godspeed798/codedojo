const { createApp } = Vue;

createApp({
    data() {
        return {
            currentView: 'login',
            username: '',
            user: {
                id: '',
                username: '',
                level: 1,
                exp: 0,
                coins: 100,
                hintsToday: 3
            },
            levels: [],
            selectedAlgo1: 'bubble',
            selectedAlgo2: 'quick',
            arraySize: 15,
            racing: false,
            raceResult: null,
            steps: [],
            currentStep: 0,
            playing: false,
            playInterval: null,
            maxVal: 100,
            selectedType: 'basic',
            selectedLevel: null,
            userCode: '',
            runResult: null,
            shopCategory: 'weapons',
            weapons: [
                { id: 1, name: '练习剑', price: 0, effect: '速度', effectValue: 5, rarity: 'common', isFree: true },
                { id: 2, name: '精钢剑', price: 2000, effect: '速度', effectValue: 20, rarity: 'rare', isFree: false },
                { id: 3, name: '屠龙刀', price: 5000, effect: '速度', effectValue: 40, rarity: 'legendary', isFree: false }
            ],
            armors: [
                { id: 4, name: '布衣', price: 0, effect: '提示', effectValue: 3, rarity: 'common', isFree: true },
                { id: 5, name: '秘银甲', price: 3000, effect: '提示', effectValue: 10, rarity: 'rare', isFree: false },
                { id: 6, name: '天龙战甲', price: 8000, effect: '提示', effectValue: 999, rarity: 'legendary', isFree: false }
            ],
            showPayModal: false,
            payContent: '',
            payAmount: 0,
            paymentId: '',
            paymentChecking: false,
            paymentCheckInterval: null,
            unlockedTypes: [],  // 按类型解锁
            unlockedLevels: [],  // 按关卡ID解锁
            websocket: null,     // WebSocket连接
            websocketConnected: false  // WebSocket连接状态
        };
    },
    computed: {
        expNeeded() {
            return 100 * this.user.level;
        },
        filteredLevels() {
            return this.levels.filter(l => l.type === this.selectedType);
        }
    },
    methods: {
        async login() {
            if (!this.username.trim()) {
                alert('请输入道号！');
                return;
            }
            try {
                const res = await axios.get(`/api/user/${this.username}`);
                this.user = this.ensureUserDefaults(res.data.data);
                this.currentView = 'lobby';
                this.loadLevels();
                await this.loadUnlockedContents();
                // 建立WebSocket连接
                this.connectWebSocket();
            } catch (e) {
                alert('登录失败');
            }
        },
        async loadLevels() {
            try {
                const res = await axios.get('/api/level');
                this.levels = res.data.data;
            } catch (e) {
                console.error(e);
            }
        },
        async loadUnlockedContents() {
            try {
                const res = await axios.get(`/api/user/${this.user.id}/unlocked`);
                this.unlockedTypes = res.data.data || [];
                console.log('📦 已加载解锁状态:', this.unlockedTypes);
                console.log('🔍 排序火山已解锁:', this.hasUnlocked('sorting'));
                console.log('🔍 查找地牢已解锁:', this.hasUnlocked('search'));
                console.log('🔍 全部内容已解锁:', this.hasUnlocked('all'));
            } catch (e) {
                console.error('加载解锁状态失败:', e);
                this.unlockedTypes = [];
            }
        },
        getRacerEmoji(algo) {
            const emojis = { bubble: '🚗', selection: '🏎️', quick: '🚀' };
            return emojis[algo] || '🚗';
        },
        getRacerName(algo) {
            const names = { bubble: '冒泡排序', selection: '选择排序', quick: '快速排序' };
            return names[algo] || algo;
        },
        getWeaponIcon(id) {
            return id === 1 ? '⚔️' : id === 2 ? '🗡️' : '🔱';
        },
        getArmorIcon(id) {
            return id === 4 ? '🥋' : id === 5 ? '🛡️' : '🦺';
        },
        async startRace() {
            this.racing = true;
            this.raceResult = null;
            this.steps = [];
            this.currentStep = 0;

            const arr = Array.from({ length: this.arraySize }, () => Math.floor(Math.random() * 90) + 10);
            this.maxVal = Math.max(...arr);

            try {
                const res = await axios.post('/api/algorithm/race', {
                    arr: arr,
                    algo1: this.selectedAlgo1,
                    algo2: this.selectedAlgo2
                });
                this.raceResult = res.data.data[0];

                const stepRes = await axios.post(`/api/algorithm/${this.selectedAlgo1}`, arr);
                this.steps = stepRes.data.data;
            } catch (e) {
                console.error(e);
            }

            this.racing = false;
        },
        getBarClass(idx) {
            const step = this.steps[this.currentStep];
            if (!step) return '';
            if (step.sorted !== undefined && idx >= this.arraySize - step.sorted - 1) return 'sorted';
            if (step.compare && step.compare.includes(idx)) return 'highlight';
            if (step.swap && step.swap.includes(idx)) return 'highlight';
            return '';
        },
        prevStep() {
            if (this.currentStep > 0) this.currentStep--;
        },
        nextStep() {
            if (this.currentStep < this.steps.length - 1) this.currentStep++;
        },
        togglePlay() {
            this.playing = !this.playing;
            if (this.playing) {
                this.playInterval = setInterval(() => {
                    if (this.currentStep >= this.steps.length - 1) {
                        this.playing = false;
                        clearInterval(this.playInterval);
                    } else {
                        this.currentStep++;
                    }
                }, 500);
            } else {
                clearInterval(this.playInterval);
            }
        },
        selectLevel(level) {
            const isFree = level.free || level.isFree || level.type === 'basic';
            if (!isFree) {
                // 检查是否已解锁（支持按类型解锁和按关卡ID解锁）
                const hasTypeAccess = this.hasUnlocked(level.type);
                const hasLevelAccess = this.hasUnlockedLevel(level.id);

                if (!hasTypeAccess && !hasLevelAccess) {
                    // 单个关卡支付5元
                    this.showPayment('level_' + level.id, 5);
                    return;
                }
            }
            this.selectedLevel = level;
            this.userCode = level.starterCode;
            this.runResult = null;
        },
        runLevel() {
            if (!this.selectedLevel) return;
            const correct = this.userCode.includes(this.selectedLevel.solution.substring(0, 10));
            const expReward = this.selectedLevel.expReward || 0;
            const coinReward = this.selectedLevel.coinReward || 0;
            this.runResult = {
                success: correct,
                message: correct ? `🎉 恭喜通关！获得 ${expReward} 经验和 ${coinReward} 金币！` : '❌ 代码输出不正确，请再试试！'
            };
            if (correct) {
                this.addRewards();
            }
        },
        async addRewards() {
            if (!this.selectedLevel) return;
            try {
                const expReward = this.selectedLevel.expReward || 0;
                const coinReward = this.selectedLevel.coinReward || 0;
                await axios.post(`/api/user/${this.user.id}/exp?exp=${expReward}`);
                await axios.post(`/api/user/${this.user.id}/coins?coins=${coinReward}`);
                const res = await axios.get(`/api/user/${this.username}`);
                this.user = this.ensureUserDefaults(res.data.data);
            } catch (e) {
                console.error(e);
            }
        },
        ensureUserDefaults(userData) {
            // 确保用户数据有默认值
            if (!userData.hintsToday || userData.hintsToday < 0) {
                userData.hintsToday = 3;
            }
            return userData;
        },
        async useHint() {
            const hintsToday = this.user.hintsToday || 0;
            if (hintsToday <= 0) {
                alert('今日提示次数已用完！');
                return;
            }
            try {
                const res = await axios.post(`/api/user/${this.user.id}/hint`);
                if (res.data.data) {
                    this.user.hintsToday = hintsToday - 1;
                    alert('💡 提示: ' + this.selectedLevel.solution);
                } else {
                    alert('提示次数不足！');
                }
            } catch (e) {
                console.error(e);
                alert('获取提示失败，请稍后重试');
            }
        },
        hasUnlockedLevel(levelId) {
            // 检查单个关卡是否已解锁
            return this.unlockedLevels.includes(levelId) || this.unlockedTypes.includes('all');
        },
        hasUnlocked(type) {
            // 保留原有方法用于向后兼容（副本解锁）
            return this.unlockedTypes.includes(type) || this.unlockedTypes.includes('all');
        },
        showPayment(content, amount) {
            this.payContent = content;
            this.payAmount = amount;
            this.showPayModal = true;
            this.paymentChecking = false;
            this.createPayment();
        },
        async createPayment() {
            try {
                const res = await axios.post('/api/payment/create', {
                    userId: this.user.id,
                    amount: this.payAmount * 100, // 转换为分
                    content: this.payContent
                });
                this.paymentId = res.data.data;
                // 开始轮询检查支付状态
                this.startPaymentCheck();
            } catch (e) {
                console.error(e);
                alert('创建支付订单失败');
                this.showPayModal = false;
            }
        },
        startPaymentCheck() {
            if (this.paymentCheckInterval) {
                clearInterval(this.paymentCheckInterval);
            }
            this.paymentChecking = true;

            // 每2秒检查一次支付状态
            this.paymentCheckInterval = setInterval(async () => {
                await this.checkPaymentStatus();
            }, 2000);
        },
        async checkPaymentStatus() {
            if (!this.paymentId) return;

            try {
                const res = await axios.get(`/api/payment/status/${this.paymentId}`);
                const status = res.data.data; // 'pending', 'paid', 'failed'

                if (status === 'paid') {
                    // 支付成功
                    clearInterval(this.paymentCheckInterval);
                    this.paymentChecking = false;

                    // 重新从服务器加载解锁状态，确保与数据库同步
                    await this.loadUnlockedContents();

                    alert('🎉 支付成功！内容已解锁！');
                    this.showPayModal = false;
                    this.paymentId = '';
                } else if (status === 'failed') {
                    // 支付失败
                    clearInterval(this.paymentCheckInterval);
                    this.paymentChecking = false;
                    alert('支付失败，请重试');
                }
            } catch (e) {
                console.error('检查支付状态失败:', e);
            }
        },
        cancelPayment() {
            if (this.paymentCheckInterval) {
                clearInterval(this.paymentCheckInterval);
            }
            this.paymentChecking = false;
            this.showPayModal = false;
            this.paymentId = '';
        },
        confirmPayment() {
            alert('✅ 支付确认已提交！\n\n请截图支付页面，联系客服验证解锁。\n\n客服微信：ysy1352895892');
            // 停止轮询，等待管理员手动验证
            if (this.paymentCheckInterval) {
                clearInterval(this.paymentCheckInterval);
            }
            this.paymentChecking = false;
        },
        buyItem(item) {
            if (this.user.coins < item.price) {
                alert('金币不足！');
                return;
            }
            this.user.coins -= item.price;
            item.isFree = true;
            alert('购买成功！' + item.name + ' 已装备！');
        },
        // WebSocket相关方法
        connectWebSocket() {
            // 如果已经连接，先断开
            if (this.websocket) {
                this.disconnectWebSocket();
            }

            // 构建WebSocket URL（根据当前页面协议自动选择ws或wss）
            const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
            const host = window.location.host;
            const wsUrl = `${protocol}//${host}/ws/unlock?userId=${this.user.id}`;

            console.log('正在连接WebSocket...', wsUrl);
            this.websocket = new WebSocket(wsUrl);

            this.websocket.onopen = () => {
                console.log('WebSocket连接成功');
                this.websocketConnected = true;
                // 每30秒发送一次心跳包，保持连接
                this.heartbeatInterval = setInterval(() => {
                    if (this.websocket && this.websocket.readyState === WebSocket.OPEN) {
                        this.websocket.send('ping');
                    }
                }, 30000);
            };

            this.websocket.onmessage = async (event) => {
                const data = event.data;

                // 处理心跳包响应（非JSON格式）
                if (data === 'pong') {
                    console.log('💓 收到心跳响应');
                    return;
                }

                // 处理JSON格式的业务消息
                try {
                    const message = JSON.parse(data);
                    console.log('收到WebSocket消息:', message);

                    // 处理解锁通知
                    if (message.type === 'unlock') {
                        await this.handleUnlockNotification(message.content);
                    }
                } catch (e) {
                    console.error('处理WebSocket消息失败:', e, '原始数据:', data);
                }
            };

            this.websocket.onerror = (error) => {
                console.error('WebSocket错误:', error);
                this.websocketConnected = false;
            };

            this.websocket.onclose = () => {
                console.log('WebSocket连接已关闭');
                this.websocketConnected = false;
                // 清除心跳定时器
                if (this.heartbeatInterval) {
                    clearInterval(this.heartbeatInterval);
                }
                // 5秒后尝试重连
                setTimeout(() => {
                    if (this.user && this.user.id) {
                        console.log('尝试重新连接WebSocket...');
                        this.connectWebSocket();
                    }
                }, 5000);
            };
        },
        disconnectWebSocket() {
            if (this.websocket) {
                this.websocket.close();
                this.websocket = null;
                this.websocketConnected = false;
                if (this.heartbeatInterval) {
                    clearInterval(this.heartbeatInterval);
                }
            }
        },
        async handleUnlockNotification(unlockedContent) {
            console.log('🎉 收到解锁通知:', unlockedContent);

            // 刷新解锁状态
            await this.loadUnlockedContents();

            // 显示友好的提示消息
            const contentNames = {
                'sorting': '排序火山',
                'search': '查找地牢',
                'all': '全部付费内容'
            };

            const contentName = contentNames[unlockedContent] || unlockedContent;
            alert(`🎉 恭喜！${contentName}已解锁！\n\n现在您可以访问新的关卡了。`);

            // 强制Vue重新渲染，确保UI更新
            this.$forceUpdate();

            console.log('✅ 解锁通知处理完成');
        }
    }
}).mount('#app');
