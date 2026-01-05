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
                { id: 1, name: '青铜剑', price: 0, effect: '速度', effectValue: 5, rarity: 'common', isFree: true },
                { id: 2, name: '黄金剑', price: 300, effect: '速度', effectValue: 20, rarity: 'rare', isFree: false },
                { id: 3, name: '钻石剑', price: 800, effect: '速度', effectValue: 40, rarity: 'legendary', isFree: false }
            ],
            armors: [
                { id: 4, name: '布甲', price: 0, effect: '提示', effectValue: 3, rarity: 'common', isFree: true },
                { id: 5, name: '锁子甲', price: 500, effect: '提示', effectValue: 10, rarity: 'rare', isFree: false },
                { id: 6, name: '龙鳞甲', price: 1500, effect: '提示', effectValue: 999, rarity: 'legendary', isFree: false }
            ],
            showPayModal: false,
            payContent: '',
            payAmount: 0,
            transactionId: '',
            unlockedTypes: []
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
                this.user = res.data.data;
                this.currentView = 'lobby';
                this.loadLevels();
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
            if (!level.isFree && !this.hasUnlocked(level.type)) {
                alert('该关卡需要付费解锁！');
                return;
            }
            this.selectedLevel = level;
            this.userCode = level.starterCode;
            this.runResult = null;
        },
        runLevel() {
            if (!this.selectedLevel) return;
            const correct = this.userCode.includes(this.selectedLevel.solution.substring(0, 10));
            this.runResult = {
                success: correct,
                message: correct ? '🎉 恭喜通关！获得 ' + this.selectedLevel.expReward + ' 经验和 ' + this.selectedLevel.coinReward + ' 金币！' : '❌ 代码输出不正确，请再试试！'
            };
            if (correct) {
                this.addRewards();
            }
        },
        async addRewards() {
            if (!this.selectedLevel) return;
            try {
                await axios.post(`/api/user/${this.user.id}/exp?exp=${this.selectedLevel.expReward}`);
                await axios.post(`/api/user/${this.user.id}/coins?coins=${this.selectedLevel.coinReward}`);
                const res = await axios.get(`/api/user/${this.username}`);
                this.user = res.data.data;
            } catch (e) {
                console.error(e);
            }
        },
        async useHint() {
            if (this.user.hintsToday <= 0) {
                alert('今日提示次数已用完！');
                return;
            }
            try {
                const res = await axios.post(`/api/user/${this.user.id}/hint`);
                if (res.data.data) {
                    this.user.hintsToday--;
                    alert('💡 提示: ' + this.selectedLevel.solution);
                }
            } catch (e) {
                console.error(e);
            }
        },
        hasUnlocked(type) {
            return this.unlockedTypes.includes(type) || this.unlockedTypes.includes('all');
        },
        showPayment(content, amount) {
            this.payContent = content;
            this.payAmount = amount;
            this.showPayModal = true;
        },
        async submitPayment() {
            if (!this.transactionId) {
                alert('请输入支付订单号！');
                return;
            }
            try {
                await axios.post('/api/payment', {
                    userId: this.user.id,
                    transactionId: this.transactionId,
                    amount: this.payAmount * 100,
                    content: this.payContent
                });
                alert('支付已提交，管理员审核后将解锁！');
                this.showPayModal = false;
                this.transactionId = '';
            } catch (e) {
                alert('提交失败');
            }
        },
        buyItem(item) {
            if (this.user.coins < item.price) {
                alert('金币不足！');
                return;
            }
            this.user.coins -= item.price;
            item.isFree = true;
            alert('购买成功！' + item.name + ' 已装备！');
        }
    }
}).mount('#app');
