/**
 * 电脑玩家类
 * 继承自Player基类，表示21点游戏中由计算机控制的玩家
 * 包含AI决策逻辑，用于自动决定何时要牌（hit）或停牌（stand）
 * 该AI基于智能概率算法，根据当前手牌点数动态调整要牌概率
 */
public class ComputerPlayer extends Player {
    /**
     * AI决策逻辑 - 基于点数的随机决策算法
     * 根据当前手牌点数和随机数决定是否要牌（hit）
     * 
     * 算法策略：
     * 1. 根据电脑当前得分计算决策值k
     * 2. 计算公式：k = (100 * (21 - 电脑得分) / 21 + 随机数(0-100))
     * 3. 如果k > 50则要牌，否则停牌
     * 4. 点数越小，k值越大，要牌概率越高
     * 5. 点数越接近21，k值越小，停牌概率越高
     * 
     * @return true表示AI决定要牌，false表示AI决定停牌
     */
    public boolean shouldHit() {
        int score = getTotalScore();

        // 如果已经超过21点或等于21点，直接停牌
        if (score >= 21)
            return false;

        // 根据新的决策逻辑计算k值
        // k = (100 * (21 - 电脑得分) / 21 + 随机数(0-100))
        double k = (100.0 * (21 - score) / 21.0) + (Math.random() * 100.0);

        // 如果k > 50则要牌，否则停牌
        return k > 50;
    }

    /**
     * 用于测试的方法：手动设置电脑玩家的手牌
     * 主要用于单元测试，可以直接设置特定的手牌组合来测试AI决策逻辑
     * 不建议在实际游戏运行中使用此方法，因为这会破坏游戏的公平性
     * 
     * @param hand 要设置给电脑玩家的手牌列表
     */
    public void setHand(java.util.List<Card> hand) {
        this.hand.clear();
        this.hand.addAll(hand);
    }
}