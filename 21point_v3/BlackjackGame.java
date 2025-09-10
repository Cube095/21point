
/**
 * 21点游戏引擎类
 * 管理21点游戏的核心逻辑、状态和流程
 * 负责初始化游戏、处理玩家操作、控制AI行为、判定游戏胜负等核心功能
 * 
 * 该类实现了Serializable接口，支持游戏状态的保存和加载
 * 游戏采用状态模式进行状态管理，通过GameState枚举定义了所有可能的游戏状态
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;

public class BlackjackGame implements Serializable {
    /**
     * 序列化版本ID
     * 用于确保类的序列化和反序列化版本兼容性
     */
    private static final long serialVersionUID = 1L;

    /**
     * 游戏使用的牌组
     * 包含52张标准扑克牌，初始包含四种花色(♠、♥、♦、♣)，每种花色13张牌(1-13)
     * 在游戏过程中，牌组中的牌会被逐渐抽走
     */
    private List<Card> deck = new ArrayList<>(52);

    /**
     * 代表人类玩家的对象
     * 由用户通过控制台输入进行控制
     */
    private HumanPlayer human = new HumanPlayer();

    /**
     * 代表电脑玩家的对象
     * 由AI决策逻辑自动控制其行为
     */
    private ComputerPlayer computer = new ComputerPlayer();

    /**
     * 当前游戏的状态
     * 决定游戏流程和玩家可执行的操作
     */
    private GameState state = GameState.NOT_STARTED;

    /**
     * 游戏状态枚举
     * 定义了21点游戏中所有可能的状态
     * 
     * NOT_STARTED - 游戏尚未开始
     * PLAYING - 游戏正在进行中
     * HUMAN_BUST - 人类玩家爆牌（点数超过21）
     * COMPUTER_BUST - 电脑玩家爆牌（点数超过21）
     * HUMAN_WIN - 人类玩家获胜
     * COMPUTER_WIN - 电脑玩家获胜
     * DRAW - 游戏平局
     */
    public enum GameState {
        NOT_STARTED, PLAYING, HUMAN_BUST, COMPUTER_BUST, HUMAN_WIN, COMPUTER_WIN, DRAW
    }

    /**
     * 开始新的21点游戏
     * 执行以下操作：
     * 1. 重置玩家状态（清空手牌，重置停牌状态）
     * 2. 初始化牌组（创建52张标准扑克牌）
     * 3. 洗牌（随机打乱牌组顺序）
     * 4. 发初始牌（每位玩家发一张牌）
     * 5. 设置游戏状态为进行中
     */
    public void startGame() {
        // 重置玩家状态
        human.reset();
        computer.reset();

        // 初始化牌组
        String[] suits = { "\u2660", "\u2665", "\u2666", "\u2663" }; // Unicode花色符号
        deck.clear();
        for (String suit : suits) {
            for (int value = 1; value <= 13; value++) {
                deck.add(new Card(suit, value));
            }
        }

        // 洗牌
        Collections.shuffle(deck);

        // 发初始牌（每人发一张牌）
        human.addCard(drawCard());
        computer.addCard(drawCard());

        // 设置游戏状态为进行中
        state = GameState.PLAYING;
    }

    /**
     * 发第二张初始牌
     * 这个方法在游戏开始后调用，用于发每位玩家的第二张牌
     * 发完第二张牌后检查是否有玩家拿到了Blackjack（21点）
     */
    public void dealSecondInitialCard() {
        human.addCard(drawCard());
        computer.addCard(drawCard());

        // 检查是否有玩家拿到了Blackjack（21点）
        checkBlackjack();
    }

    /**
     * 为电脑玩家发第二张牌
     * 这个方法用于在玩家第一次要牌时，为电脑玩家发第二张牌
     */
    public void dealComputerSecondCard() {
        if (computer.getHand().size() == 1) {
            computer.addCard(drawCard());
            checkBlackjack();
        }
    }

    /**
     * 检查电脑是否想要要牌
     * 这个方法用于在游戏过程中显示电脑的决策意向
     * 
     * @return true表示电脑想要要牌，false表示电脑不想要牌
     */
    public boolean checkComputerWantsToHit() {
        return computer.shouldHit();
    }

    /**
     * 检查玩家是否在初始发牌后就拿到了Blackjack（21点）
     * 这是21点游戏的一个特殊规则：如果玩家在初始的两张牌就拿到21点（A+10/J/Q/K），
     * 则视为Blackjack，通常会获得额外奖励
     * 
     * 根据检查结果更新游戏状态：
     * - 如果双方都是Blackjack，游戏为平局
     * - 如果只有人类玩家是Blackjack，人类玩家获胜
     * - 如果只有电脑玩家是Blackjack，电脑玩家获胜
     */
    private void checkBlackjack() {
        boolean humanBJ = (human.getTotalScore() == 21);
        boolean computerBJ = (computer.getTotalScore() == 21);

        if (humanBJ && computerBJ) {
            state = GameState.DRAW;
        } else if (humanBJ) {
            state = GameState.HUMAN_WIN;
        } else if (computerBJ) {
            state = GameState.COMPUTER_WIN;
        }
    }

    /**
     * 从游戏牌组中抽取一张牌
     * 这是一个私有辅助方法，用于游戏内部的牌的发放逻辑
     * 
     * 如果牌组为空，会自动重新初始化并洗牌，确保游戏能够持续进行
     * 
     * @return 从牌组顶部抽取的牌
     */
    private Card drawCard() {
        if (deck.isEmpty()) {
            // 如果牌组空了，重新初始化并洗牌
            startGame();
        }
        return deck.remove(0);
    }

    /**
     * 处理人类玩家的要牌（hit）操作
     * 
     * 执行此操作的条件：
     * 1. 游戏状态必须是PLAYING
     * 2. 人类玩家尚未停牌
     * 
     * 执行操作：
     * 1. 从牌组抽取一张牌并添加到人类玩家的手牌中
     * 2. 检查人类玩家的总分是否超过21点
     * 3. 如果超过21点，将游戏状态设置为HUMAN_BUST（人类玩家爆牌）
     */
    public void humanHit() {
        if (state != GameState.PLAYING || human.isStanding())
            return;

        human.addCard(drawCard());
        if (human.getTotalScore() > 21) {
            state = GameState.HUMAN_BUST;
        }
    }

    /**
     * 处理人类玩家的停牌（stand）操作
     * 
     * 执行此操作的条件：
     * 1. 游戏状态必须是PLAYING
     * 
     * 执行操作：
     * 1. 将人类玩家的停牌状态设置为true
     */
    public void humanStand() {
        if (state != GameState.PLAYING)
            return;

        human.setStanding(true);
    }

    /**
     * 处理电脑玩家的回合
     * 根据AI决策决定是否要牌
     */
    public void computerHit() {
        if (state != GameState.PLAYING || computer.isStanding())
            return;

        boolean wantsToHit = computer.shouldHit();
        if (wantsToHit) {
            computer.addCard(drawCard());
            if (computer.getTotalScore() > 21) {
                state = GameState.COMPUTER_BUST;
            }
        } else {
            computer.setStanding(true);
        }
    }

    /**
     * 检查游戏是否结束
     * 
     * @return true表示游戏结束，false表示游戏继续
     */
    public boolean isGameOver() {
        return state != GameState.PLAYING || (human.isStanding() && computer.isStanding());
    }

    /**
     * 当游戏结束时（双方都停牌或有一方爆牌），确定最终胜负
     */
    public void finalizeGame() {
        if (state == GameState.PLAYING && human.isStanding() && computer.isStanding()) {
            determineWinner();
        }
    }

    /**
     * 根据游戏规则确定最终胜利者
     * 
     * 判定逻辑：
     * 1. 如果人类玩家爆牌（状态为HUMAN_BUST），电脑玩家获胜
     * 2. 如果电脑玩家爆牌（状态为COMPUTER_BUST），人类玩家获胜
     * 3. 否则，比较双方的手牌点数：
     * - 点数高的玩家获胜
     * - 点数相同则为平局
     * 
     * 根据判定结果更新游戏状态为HUMAN_WIN、COMPUTER_WIN或DRAW
     */
    private void determineWinner() {
        int humanScore = human.getTotalScore();
        int computerScore = computer.getTotalScore();

        if (humanScore > computerScore) {
            state = GameState.HUMAN_WIN;
        } else if (humanScore < computerScore) {
            state = GameState.COMPUTER_WIN;
        } else {
            state = GameState.DRAW;
        }
    }

    /**
     * 获取当前游戏的状态
     * 游戏状态决定了游戏流程和玩家可执行的操作
     * 
     * @return 当前的游戏状态枚举值（NOT_STARTED, PLAYING, HUMAN_BUST, COMPUTER_BUST,
     *         HUMAN_WIN, COMPUTER_WIN, DRAW）
     */
    public GameState getState() {
        return state;
    }

    /**
     * 获取人类玩家当前持有的所有牌
     * 提供防御性编程保护，返回手牌的副本以防止外部直接修改
     * 
     * @return 人类玩家手牌的副本列表
     */
    public List<Card> getHumanHand() {
        return human.getHand();
    }

    /**
     * 获取电脑玩家当前持有的所有牌
     * 提供防御性编程保护，返回手牌的副本以防止外部直接修改
     * 
     * @return 电脑玩家手牌的副本列表
     */
    public List<Card> getComputerHand() {
        return computer.getHand();
    }

    /**
     * 获取人类玩家当前手牌的最佳分数
     * 考虑A牌可以作为1点或11点的特殊规则
     * 
     * @return 人类玩家的最优分数（在不爆牌的前提下）
     */
    public int getHumanScore() {
        return human.getTotalScore();
    }

    /**
     * 获取电脑玩家当前手牌的最佳分数
     * 考虑A牌可以作为1点或11点的特殊规则
     * 
     * @return 电脑玩家的最优分数（在不爆牌的前提下）
     */
    public int getComputerScore() {
        return computer.getTotalScore();
    }

    /**
     * 格式化显示卡牌列表
     * 
     * @param cards 卡牌列表
     * @return 格式化的字符串
     */
    public static String formatCards(List<Card> cards) {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card).append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * 将当前游戏状态保存到文件中
     * 利用Java序列化机制将整个游戏对象（包括玩家状态、牌组、游戏状态）保存到文件
     * 
     * @param filename 保存游戏状态的文件名
     * @throws IOException 如果在保存过程中发生IO错误
     */
    public void saveGame(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    /**
     * 从文件中加载之前保存的游戏状态
     * 使用Java反序列化机制恢复整个游戏对象的状态
     * 
     * @param filename 包含游戏状态的文件名
     * @return 从文件中恢复的游戏对象
     * @throws IOException            如果在加载过程中发生IO错误
     * @throws ClassNotFoundException 如果加载的对象所属的类无法找到
     */
    public static BlackjackGame loadGame(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (BlackjackGame) ois.readObject();
        }
    }

    /**
     * 获取人类玩家对象的引用
     * 这个方法主要用于外部访问游戏中的人类玩家实例
     * 例如在Main类中用于展示玩家状态或在测试中使用
     * 
     * @return 游戏中的人类玩家对象
     */
    public HumanPlayer getHuman() {
        return human;
    }

    /**
     * 获取电脑玩家对象的引用
     * 这个方法主要用于外部访问游戏中的电脑玩家实例
     * 例如在Main类中用于展示玩家状态或在测试中使用
     * 
     * @return 游戏中的电脑玩家对象
     */
    public ComputerPlayer getComputer() {
        return computer;
    }
}