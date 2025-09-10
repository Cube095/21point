/**
 * 21点游戏的主入口类
 * 实现控制台界面、游戏流程控制和用户交互
 * 
 * 主要功能：
 * - 初始化游戏环境和用户输入处理
 * - 实现游戏的主循环（支持多次游戏）
 * - 处理玩家的输入和决策（要牌或停牌）
 * - 显示游戏状态和结果
 */
import java.util.Scanner;

public class Main {
    /**
     * 主方法，程序的入口点
     * 负责初始化游戏对象、设置用户输入处理、实现游戏的主循环
     * 
     * @param args 命令行参数（本程序不使用命令行参数）
     */
    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("===== 欢迎来到21点游戏 =====");
        
        // 游戏的主循环，支持玩家进行多局游戏
        // 只有当玩家选择不再继续游戏时才会退出循环
        boolean playAgain = true;
        while (playAgain) {
            // 开始新游戏
            game.startGame();
            
            System.out.println("新游戏开始！");
            
            // 显示初始手牌
            System.out.println("发牌中...");
            System.out.println("你的手牌: " + BlackjackGame.formatCards(game.getHumanHand()));
            System.out.println("你的点数: " + game.getHumanScore());
            System.out.println("电脑手牌: [隐藏]");
            // 停顿一下，增加游戏体验
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // 游戏进行中的循环，直到游戏结束
            while (!game.isGameOver()) {
                // 玩家回合
                if (!game.getHuman().isStanding() && game.getState() == BlackjackGame.GameState.PLAYING) {
                    System.out.print("操作: (1)要牌 (2)停牌 > ");
                    
                    int choice;
                    try {
                        choice = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("无效输入，请输入1或2");
                        scanner.nextLine(); // 清除输入缓冲区，防止输入错误导致的无限循环
                        continue;
                    }
                    
                    if (choice == 1) {
                        game.humanHit();
                        // 显示新的手牌和分数
                        System.out.println("你的手牌: " + BlackjackGame.formatCards(game.getHumanHand()));
                        System.out.println("你的点数: " + game.getHumanScore());
                        
                        // 如果玩家爆牌，游戏结束
                        if (game.getState() == BlackjackGame.GameState.HUMAN_BUST) {
                            break;
                        }
                    } else if (choice == 2) {
                        game.humanStand();
                        System.out.println("你选择了停牌！");
                    } else {
                        System.out.println("无效输入，请输入1或2");
                        continue;
                    }
                }
                
                // 电脑回合（自动执行，无需用户输入）
                if (!game.getComputer().isStanding() && game.getState() == BlackjackGame.GameState.PLAYING) {
                    System.out.println("电脑正在思考...");
                    try {
                        Thread.sleep(1000); // 增加思考时间，提升游戏体验
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    // 电脑自动决定是否要牌
                    boolean wasStanding = game.getComputer().isStanding();
                    game.computerHit();
                    
                    // 显示电脑是否要牌
                    if (game.getComputer().isStanding() && !wasStanding) {
                        System.out.println("电脑选择了停牌！");
                    } else if (!game.getComputer().isStanding()) {
                        System.out.println("电脑选择了要牌！");
                    }
                    
                    // 如果电脑爆牌，游戏结束
                    if (game.getState() == BlackjackGame.GameState.COMPUTER_BUST) {
                        break;
                    }
                }
                
                // 如果双方都停牌，游戏结束
                if (game.getHuman().isStanding() && game.getComputer().isStanding()) {
                    break;
                }
                
                // 停顿一下，增加游戏体验
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            // 确定最终胜负
            game.finalizeGame();
            
            // 游戏结束后，显示最终结果
            printGameResult(game);
            
            // 询问玩家是否想再玩一次
            System.out.print("再玩一次？(1)是 (0)否 > ");
            try {
                playAgain = (scanner.nextInt() == 1);
            } catch (Exception e) {
                playAgain = false;
            }
        }
        
        System.out.println("谢谢游玩，再见！");
        scanner.close();
    }
    
    /**
     * 打印游戏的最终结果
     * 显示双方的完整手牌、点数和胜负情况
     * 
     * @param game 当前游戏对象，包含了游戏的完整状态
     */
    private static void printGameResult(BlackjackGame game) {
        System.out.println("\n===== 游戏结果 =====");
        System.out.println("你的手牌: " + BlackjackGame.formatCards(game.getHumanHand()));
        System.out.println("你的点数: " + game.getHumanScore());
        System.out.println("电脑手牌: " + BlackjackGame.formatCards(game.getComputerHand()));
        System.out.println("电脑点数: " + game.getComputerScore());
        
        // 根据游戏状态打印不同的结果信息
        // 涵盖了21点游戏中所有可能的结局情况
        switch (game.getState()) {
            case HUMAN_BUST:
                System.out.println("你爆牌了！电脑获胜");
                break;
            case COMPUTER_BUST:
                System.out.println("电脑爆牌！你赢了");
                break;
            case HUMAN_WIN:
                System.out.println("恭喜你赢了！");
                break;
            case COMPUTER_WIN:
                System.out.println("电脑赢了！");
                break;
            case DRAW:
                System.out.println("平局");
                break;
        }
    }
}