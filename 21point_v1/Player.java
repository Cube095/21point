/**
 * 玩家抽象基类
 * 定义21点游戏中所有玩家（人类玩家和电脑玩家）共有的基本属性和行为
 * 提供手牌管理、分数计算、游戏状态控制等核心功能
 */
import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    /**
     * 玩家手中的扑克牌列表
     * 使用ArrayList存储，支持动态添加和删除牌
     */
    protected List<Card> hand = new ArrayList<>();
    
    /**
     * 表示玩家是否选择停牌
     * true表示玩家已停牌，不再继续要牌；false表示玩家仍在游戏中，可能继续要牌
     */
    protected boolean isStanding = false;
    
    /**
     * 向玩家手中添加一张牌
     * 当玩家要牌（hit）时调用此方法
     * @param card 要添加到玩家手中的牌
     */
    public void addCard(Card card) {
        hand.add(card);
    }
    
    /**
     * 计算并返回玩家手牌的总分值
     * 按照简化规则计算：
     * - 2-10：牌面数值与分值相同
     * - J、Q、K：分别为11,12,13分
     * - A：固定为1分
     * @return 玩家手牌的总分值
     */
    public int getTotalScore() {
        int score = 0;
        for (Card card : hand) {
            // 对于所有牌，直接使用它们的值作为分数
            // J(11)、Q(12)、K(13) 分别计为11、12、13分
            // A(1) 计为1分
            // 2-10 计为对应数值的分数
            score += card.getValue();
        }
        
        return score;
    }
    
    /**
     * 获取玩家当前持有的所有牌的副本
     * 返回手牌的副本而不是原引用，以防止外部代码意外修改玩家的手牌
     * 这是一种防御性编程的实践
     * @return 玩家手牌的副本列表
     */
    public List<Card> getHand() {
        return new ArrayList<>(hand); // 返回副本以防止外部修改
    }
    
    /**
     * 设置玩家的停牌状态
     * 当玩家选择停牌（stand）时，调用此方法将状态设置为true
     * @param standing 是否停牌
     */
    public void setStanding(boolean standing) {
        isStanding = standing;
    }
    
    /**
     * 检查玩家是否已停牌
     * 在游戏流程中用于判断玩家是否可以继续要牌
     * @return true表示玩家已停牌，false表示玩家未停牌
     */
    public boolean isStanding() {
        return isStanding;
    }
    
    /**
     * 重置玩家状态，准备新游戏
     * 清空玩家手中的所有牌，并将停牌状态重置为false
     * 在开始新游戏或重新开始时调用
     */
    public void reset() {
        hand.clear();
        isStanding = false;
    }
}