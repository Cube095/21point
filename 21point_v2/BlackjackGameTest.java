/**
 * 21点游戏测试类
 * 测试21点游戏的核心逻辑和功能
 * 
 * 包含的测试用例：
 * - 玩家爆牌场景测试
 * - 电脑智能决策逻辑测试
 * - A牌特殊处理逻辑测试
 * - 游戏胜负判定逻辑测试
 * 
 * 注意：本测试类使用手动测试方法，通过条件判断和控制台输出来验证测试结果
 */
import java.util.Arrays;

public class BlackjackGameTest {
    
    /**
     * 主方法，用于运行所有测试
     * 初始化测试对象并依次调用所有测试方法
     * 在控制台输出测试开始和完成信息
     * 
     * @param args 命令行参数（本测试不使用命令行参数）
     */
    public static void main(String[] args) {
        System.out.println("===== 开始运行21点游戏测试 =====");
        
        BlackjackGameTest test = new BlackjackGameTest();
        
        // 运行所有测试方法
        test.testHumanBust();
        test.testComputerDecisionMaking();
        test.testAceSpecialHandling();
        test.testGameWinnerDetermination();
        
        System.out.println("===== 测试运行完成 =====");
    }

    /**
     * 测试玩家爆牌场景
     * 验证当玩家手牌点数超过21点时，游戏状态是否正确设置为HUMAN_BUST
     * 
     * 测试流程：
     * 1. 创建游戏对象并开始游戏
     * 2. 重置人类玩家并模拟发牌，使其手牌点数接近21点
     * 3. 调用humanHit方法让玩家再要一张牌，导致点数超过21点
     * 4. 验证游戏状态是否正确设置为HUMAN_BUST
     */
    public void testHumanBust() {
        BlackjackGame game = new BlackjackGame();
        game.startGame();
        
        // 假设玩家已经有一些牌，这里直接操作hand集合模拟20点
        // 由于hand是protected的，我们可以通过反射来设置，或者修改Player类添加一个setHand方法
        // 这里我们假设Player类有setHand方法（在实际代码中已经添加）
        HumanPlayer human = game.getHuman();
        human.reset();
        human.addCard(new Card("♥", 10)); // 10点
        human.addCard(new Card("♦", 10)); // 20点
        
        // 再要一张牌，导致爆牌
        game.humanHit();
        
        // 验证是否正确触发HUMAN_BUST状态
        if (game.getState() == BlackjackGame.GameState.HUMAN_BUST) {
            System.out.println("✓ 测试通过：玩家应该爆牌");
        } else {
            System.out.println("✗ 测试失败：玩家应该爆牌，但实际状态是：" + game.getState());
        }
    }
    
    /**
     * 测试电脑玩家的智能决策逻辑
     * 验证电脑玩家在不同点数下的要牌/停牌决策是否符合预期策略
     * 
     * 测试场景：
     * 1. 16点时应该有较高的概率选择要牌（约0.5概率）
     * 2. 19点时不应该选择要牌
     * 3. 12点时应该有极高的概率选择要牌（接近1.0概率）
     * 
     * 注：由于电脑决策采用概率算法，对于概率性决策使用多次测试来验证概率范围
     */
    public void testComputerDecisionMaking() {
        ComputerPlayer computer = new ComputerPlayer();
        
        // 测试场景1：16点应该要牌（高概率）
        computer.reset();
        computer.setHand(Arrays.asList(new Card("♥", 10), new Card("♦", 6))); // 16点
        
        // 由于是概率算法，我们不能断言一次调用的结果，而是多次调用后检查概率
        int hitCount = 0;
        int totalTests = 1000;
        for (int i = 0; i < totalTests; i++) {
            if (computer.shouldHit()) {
                hitCount++;
            }
        }
        
        // 16点的要牌概率应该是(18-16)*0.25 = 0.5
        // 检查是否在合理范围内
        double hitRate = (double) hitCount / totalTests;
        if (hitRate > 0.4 && hitRate < 0.6) {
            System.out.println("✓ 测试通过：16点时要牌概率应该接近0.5 (实际: " + hitRate + ")");
        } else {
            System.out.println("✗ 测试失败：16点时要牌概率应该接近0.5，但实际是：" + hitRate);
        }
        
        // 测试场景2：19点不应该要牌
        computer.reset();
        computer.setHand(Arrays.asList(new Card("♠", 10), new Card("♣", 9))); // 19点
        if (!computer.shouldHit()) {
            System.out.println("✓ 测试通过：19点时不应该要牌");
        } else {
            System.out.println("✗ 测试失败：19点时不应该要牌，但电脑选择了要牌");
        }
        
        // 测试场景3：12点应该要牌（高概率）
        computer.reset();
        computer.setHand(Arrays.asList(new Card("♠", 6), new Card("♣", 6))); // 12点
        hitCount = 0;
        for (int i = 0; i < totalTests; i++) {
            if (computer.shouldHit()) {
                hitCount++;
            }
        }
        
        // 12点的要牌概率应该是(18-12)*0.25 = 1.5，但被限制为1.0
        hitRate = (double) hitCount / totalTests;
        if (hitRate > 0.95) {
            System.out.println("✓ 测试通过：12点时要牌概率应该接近1.0 (实际: " + hitRate + ")");
        } else {
            System.out.println("✗ 测试失败：12点时要牌概率应该接近1.0，但实际是：" + hitRate);
        }
    }
    
    /**
     * 测试A牌的特殊处理逻辑
     * 验证A牌在不同组合下是否能够正确地被视为1点或11点
     * 
     * 测试场景：
     * 1. A+2应该被计算为13点（A视为11）
     * 2. A+10+10应该被计算为21点（A视为1）
     * 3. A+J应该被计算为21点（A视为11，J视为10）
     */
    public void testAceSpecialHandling() {
        Player player = new HumanPlayer();
        
        // 测试场景1：A+2，总分应为13（A视为11）
        player.reset();
        player.addCard(new Card("♥", 1)); // A
        player.addCard(new Card("♦", 2)); // 2
        if (player.getTotalScore() == 13) {
            System.out.println("✓ 测试通过：A+2应该等于13");
        } else {
            System.out.println("✗ 测试失败：A+2应该等于13，但实际是：" + player.getTotalScore());
        }
        
        // 测试场景2：A+10+10，总分应为21（A视为1）
        player.reset();
        player.addCard(new Card("♥", 1)); // A
        player.addCard(new Card("♦", 10)); // 10
        player.addCard(new Card("♠", 10)); // 10
        if (player.getTotalScore() == 21) {
            System.out.println("✓ 测试通过：A+10+10应该等于21");
        } else {
            System.out.println("✗ 测试失败：A+10+10应该等于21，但实际是：" + player.getTotalScore());
        }
        
        // 测试场景3：A+J，总分应为21
        player.reset();
        player.addCard(new Card("♥", 1)); // A
        player.addCard(new Card("♦", 11)); // J
        if (player.getTotalScore() == 21) {
            System.out.println("✓ 测试通过：A+J应该等于21");
        } else {
            System.out.println("✗ 测试失败：A+J应该等于21，但实际是：" + player.getTotalScore());
        }
    }
    
    /**
     * 测试游戏的胜负判定逻辑
     * 验证游戏在不同情况下是否能够正确判定胜负
     * 
     * 测试场景：
     * 1. 玩家分数高于电脑时应该获胜
     * 2. 电脑爆牌时玩家应该获胜
     * 
     * 注意：测试中需要重置游戏状态并模拟不同的牌局情况
     */
    public void testGameWinnerDetermination() {
        BlackjackGame game = new BlackjackGame();
        game.startGame();
        
        HumanPlayer human = game.getHuman();
        ComputerPlayer computer = game.getComputer();
        
        // 重置游戏状态为PLAYING
        // 注意：这需要通过反射或者在BlackjackGame中添加setState方法
        // 这里我们假设可以通过某种方式设置状态
        
        // 测试场景1：玩家分数更高
        human.reset();
        computer.reset();
        human.addCard(new Card("♥", 10));
        human.addCard(new Card("♦", 9)); // 19点
        computer.addCard(new Card("♠", 10));
        computer.addCard(new Card("♣", 8)); // 18点
        
        // 让玩家停牌，触发电脑回合和胜负判定
        game.humanStand();
        
        // 验证玩家获胜
        if (game.getState() == BlackjackGame.GameState.HUMAN_WIN ||
            game.getState() == BlackjackGame.GameState.DRAW) {
            System.out.println("✓ 测试通过：玩家分数更高应该获胜或平局");
        } else {
            System.out.println("✗ 测试失败：玩家分数更高应该获胜，但实际状态是：" + game.getState());
        }
        
        // 测试场景2：电脑爆牌
        game.startGame();
        human = game.getHuman();
        computer = game.getComputer();
        
        human.reset();
        computer.reset();
        human.addCard(new Card("♥", 10));
        human.addCard(new Card("♦", 5)); // 15点
        
        // 让电脑获取22点，导致爆牌
        computer.addCard(new Card("♠", 10));
        computer.addCard(new Card("♣", 10));
        computer.addCard(new Card("♥", 2)); // 22点
        
        // 让玩家停牌，触发电脑回合
        game.humanStand();
        
        // 验证电脑爆牌，玩家获胜
        if (game.getState() == BlackjackGame.GameState.COMPUTER_BUST) {
            System.out.println("✓ 测试通过：电脑爆牌应该玩家获胜");
        } else {
            System.out.println("✗ 测试失败：电脑爆牌应该玩家获胜，但实际状态是：" + game.getState());
        }
    }
}