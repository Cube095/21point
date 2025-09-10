import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 21点游戏的图形用户界面类
 * 提供可视化的游戏交互界面，严格按照21点游戏规则实现
 * 游戏流程：
 * 1. 进入程序后玩家选择开始游戏
 * 2. 系统对一副牌（52张）进行洗牌
 * 3. 然后分别给玩家和电脑发一张牌
 * 4. 之后玩家和电脑在每回合中自行选择每次发牌时要牌还是不要牌
 * 5. 双方都力争使自己接近21点且不"爆牌"，即不超过21点
 * 6. 玩家和电脑在某回合中一旦选择不要牌，则以后都不可以再要牌，只能等待游戏结束
 * 7. 如果玩家要牌后超过21点，则电脑获胜
 * 8. 如果玩家不要牌，电脑要牌后超过21点，则玩家获胜
 * 9. 如果双方都不超21点并都不要牌，则点数接近21点的获胜，点数一样则为平局
 */
public class BlackjackGUI extends JFrame implements ActionListener {
    private BlackjackGame game; // 游戏逻辑实例
    private JPanel mainPanel; // 主面板
    private JPanel humanHandPanel; // 玩家手牌面板
    private JPanel computerHandPanel; // 电脑手牌面板
    private JLabel humanScoreLabel; // 玩家分数标签
    private JLabel computerScoreLabel; // 电脑分数标签
    private JLabel gameStatusLabel; // 游戏状态标签
    private JButton hitButton; // 要牌按钮
    private JButton standButton; // 停牌按钮
    private JButton newGameButton; // 新游戏按钮
    private boolean computerCardVisible; // 控制电脑牌是否可见

    /**
     * 构造方法，初始化图形界面
     */
    public BlackjackGUI() {
        super("21点游戏");
        game = new BlackjackGame();
        computerCardVisible = false;
        initUI();
    }

    /**
     * 初始化用户界面组件
     */
    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(mainPanel);

        // 创建游戏状态标签
        gameStatusLabel = new JLabel("欢迎来到21点游戏！点击新游戏开始", JLabel.CENTER);
        gameStatusLabel.setFont(new Font("宋体", Font.BOLD, 18));
        mainPanel.add(gameStatusLabel, BorderLayout.NORTH);

        // 创建中间面板，用于显示玩家手牌
        JPanel handsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.add(handsPanel, BorderLayout.CENTER);

        // 电脑玩家面板
        JPanel computerPanel = new JPanel(new BorderLayout(10, 10));
        computerPanel.setBorder(BorderFactory.createTitledBorder("电脑玩家"));
        handsPanel.add(computerPanel);

        computerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        computerPanel.add(computerHandPanel, BorderLayout.CENTER);

        computerScoreLabel = new JLabel("点数: 0", JLabel.CENTER);
        computerScoreLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        computerPanel.add(computerScoreLabel, BorderLayout.SOUTH);

        // 人类玩家面板
        JPanel humanPanel = new JPanel(new BorderLayout(10, 10));
        humanPanel.setBorder(BorderFactory.createTitledBorder("玩家"));
        handsPanel.add(humanPanel);

        humanHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        humanPanel.add(humanHandPanel, BorderLayout.CENTER);

        humanScoreLabel = new JLabel("点数: 0", JLabel.CENTER);
        humanScoreLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        humanPanel.add(humanScoreLabel, BorderLayout.SOUTH);

        // 创建底部按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        hitButton = new JButton("要牌");
        hitButton.addActionListener(this);
        hitButton.setEnabled(false);
        buttonPanel.add(hitButton);

        standButton = new JButton("停牌");
        standButton.addActionListener(this);
        standButton.setEnabled(false);
        buttonPanel.add(standButton);

        newGameButton = new JButton("新游戏");
        newGameButton.addActionListener(this);
        buttonPanel.add(newGameButton);

        // 显示窗口
        setVisible(true);
    }

    /**
     * 开始新游戏
     * 根据流程图：初始化扑克牌数组、初始化玩家数据、洗牌、给人类玩家发牌、给电脑玩家发牌、返回游戏状态为进行中
     */
    private void startNewGame() {
        // 初始化游戏
        game.startGame(); // 这会自动初始化玩家状态、牌组、洗牌并各发一张牌
        computerCardVisible = false;
        updateUI();
        gameStatusLabel.setText("游戏开始！请选择要牌或停牌");

        // 恢复按钮的原始文本和状态
        hitButton.setText("要牌");
        standButton.setText("停牌");
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        newGameButton.setEnabled(false);
    }

    /**
     * 更新UI显示
     */
    private void updateUI() {
        // 更新电脑玩家手牌
        computerHandPanel.removeAll();
        List<Card> computerHand = game.getComputerHand();
        for (Card card : computerHand) {
            if (computerCardVisible || game.isGameOver()) {
                computerHandPanel.add(createCardPanel(card.toString()));
            } else {
                // 游戏进行中，电脑的牌保持隐藏
                computerHandPanel.add(createCardPanel("背面"));
            }
        }

        // 更新人类玩家手牌
        humanHandPanel.removeAll();
        for (Card card : game.getHumanHand()) {
            humanHandPanel.add(createCardPanel(card.toString()));
        }

        // 更新分数显示
        humanScoreLabel.setText("点数: " + game.getHumanScore());

        // 电脑分数只在游戏结束时显示
        if (computerCardVisible || game.isGameOver()) {
            computerScoreLabel.setText("点数: " + game.getComputerScore());
        } else {
            computerScoreLabel.setText("点数: ?");
        }

        // 重新绘制界面
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * 创建卡牌显示面板
     * 改进版本：添加美观的花色图案显示
     */
    private JPanel createCardPanel(String cardText) {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(90, 130));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cardPanel.setBackground(cardText.equals("背面") ? Color.BLUE : Color.WHITE);
        cardPanel.setLayout(new BorderLayout());

        if (cardText.equals("背面")) {
            // 背面显示
            JLabel cardLabel = new JLabel("背面", JLabel.CENTER);
            cardLabel.setFont(new Font("宋体", Font.BOLD, 16));
            cardLabel.setForeground(Color.WHITE);
            cardPanel.add(cardLabel, BorderLayout.CENTER);
        } else {
            // 正面显示，分离花色和数值
            String suit = "";
            String value = "";
            Color suitColor = Color.BLACK;

            // 解析花色和数值
            if (cardText.startsWith("♠")) {
                suit = "♠";
                value = cardText.substring(1);
                suitColor = Color.BLACK;
            } else if (cardText.startsWith("♥")) {
                suit = "♥";
                value = cardText.substring(1);
                suitColor = Color.RED;
            } else if (cardText.startsWith("♦")) {
                suit = "♦";
                value = cardText.substring(1);
                suitColor = Color.RED;
            } else if (cardText.startsWith("♣")) {
                suit = "♣";
                value = cardText.substring(1);
                suitColor = Color.BLACK;
            }

            // 创建主要内容面板
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setOpaque(false);

            // 左上角显示数值
            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("Arial", Font.BOLD, 14));
            valueLabel.setForeground(suitColor);
            valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
            contentPanel.add(valueLabel, BorderLayout.NORTH);

            // 中央显示大号花色图案
            JLabel suitLabel = new JLabel(suit, JLabel.CENTER);
            suitLabel.setFont(new Font("Arial", Font.BOLD, 36));
            suitLabel.setForeground(suitColor);
            contentPanel.add(suitLabel, BorderLayout.CENTER);

            // 右下角显示旋转的数值和花色（模拟真实扑克牌）
            JPanel cornerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
            cornerPanel.setOpaque(false);
            JLabel cornerLabel = new JLabel(
                    "<html><div style='transform: rotate(180deg);'>" + value + suit + "</div></html>");
            cornerLabel.setFont(new Font("Arial", Font.BOLD, 10));
            cornerLabel.setForeground(suitColor);
            cornerPanel.add(cornerLabel);
            contentPanel.add(cornerPanel, BorderLayout.SOUTH);

            cardPanel.add(contentPanel, BorderLayout.CENTER);
        }

        return cardPanel;
    }

    /**
     * 显示游戏结果
     */
    private void showGameResult() {
        // 显示所有电脑的牌和分数
        computerCardVisible = true;
        updateUI();

        // 确保在显示结果前正确确定胜负状态
        if (!game.isGameOver() ||
                (game.getState() == BlackjackGame.GameState.PLAYING &&
                        game.getHuman().isStanding() && game.getComputer().isStanding())) {
            game.finalizeGame();
        }

        // 获取游戏结果
        String resultText = "";
        BlackjackGame.GameState state = game.getState();

        switch (state) {
            case HUMAN_BUST:
                resultText = "你爆牌了！电脑获胜";
                break;
            case COMPUTER_BUST:
                resultText = "电脑爆牌！你赢了";
                break;
            case HUMAN_WIN:
                resultText = "恭喜你赢了！";
                break;
            case COMPUTER_WIN:
                resultText = "电脑赢了！";
                break;
            case DRAW:
                resultText = "平局";
                break;
            default:
                // 如果游戏状态未正确设置，手动计算结果
                int humanScore = game.getHumanScore();
                int computerScore = game.getComputerScore();

                if (humanScore > 21) {
                    resultText = "你爆牌了！电脑获胜";
                } else if (computerScore > 21) {
                    resultText = "电脑爆牌！你赢了";
                } else if (humanScore > computerScore) {
                    resultText = "恭喜你赢了！";
                } else if (humanScore < computerScore) {
                    resultText = "电脑赢了！";
                } else {
                    resultText = "平局";
                }
        }

        gameStatusLabel.setText(resultText);
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        newGameButton.setEnabled(true);

        // 显示结果对话框
        JOptionPane.showMessageDialog(this, resultText, "游戏结果", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 处理按钮点击事件
     * 严格按照游戏流程图：玩家先决定要牌还是停牌，再轮到电脑进行决策
     * - 人类玩家要牌：给玩家发牌 → 检查爆牌 → 电脑回合
     * - 人类玩家不要牌：设置玩家状态 → 电脑回合
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == hitButton) {
            // 人类玩家要牌流程：给人类玩家发牌
            game.humanHit();
            updateUI();

            // 检查是否爆牌
            if (game.getState() == BlackjackGame.GameState.HUMAN_BUST) {
                // 返回游戏状态为人类玩家爆牌
                showGameResult();
            } else {
                // 进入电脑回合决策
                gameStatusLabel.setText("电脑回合...");
                gameStatusLabel.setForeground(Color.RED); // 电脑回合使用红色

                // 禁用按钮防止用户在电脑回合操作
                hitButton.setEnabled(false);
                standButton.setEnabled(false);

                // 使用SwingWorker在后台线程中处理电脑回合，避免UI卡顿
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // 增加延迟时间，增强回合感
                        Thread.sleep(1500);
                        return null;
                    }

                    @Override
                    protected void done() {
                        computerTurn();
                    }
                }.execute();
            }
        } else if (e.getSource() == standButton) {
            // 人类玩家不要牌流程：设置人类玩家状态
            game.humanStand();
            updateUI();
            gameStatusLabel.setText("电脑回合...");
            gameStatusLabel.setForeground(Color.RED); // 电脑回合使用红色

            // 禁用按钮防止用户在电脑回合操作
            hitButton.setEnabled(false);
            standButton.setEnabled(false);

            // 使用SwingWorker在后台线程中处理电脑回合
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Thread.sleep(1500);
                    return null;
                }

                @Override
                protected void done() {
                    computerTurn();
                }
            }.execute();
        } else if (e.getSource() == newGameButton) {
            // 开始新游戏
            startNewGame();
        }
    }

    /**
     * 处理电脑回合
     * 严格按照游戏规则：在玩家停牌前，电脑只能在玩家要牌一张后要牌一张或停牌，不能要两张牌
     * - 如果电脑只有一张牌，先补发到两张牌（这不算作一次要牌决策）
     * - 如果玩家未停牌，电脑完成一次决策后必须回到玩家回合
     * - 如果玩家已停牌，电脑将连续决策直到停牌或爆牌
     */
    private void computerTurn() {
        boolean isInitialDeal = false;

        // 确保电脑有两张牌（这不算作一次要牌决策）
        if (game.getComputer().getHand().size() == 1) {
            game.dealComputerSecondCard();
            isInitialDeal = true; // 标记为初始补牌
            updateUI();
            try {
                Thread.sleep(1500); // 增加延迟时间
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        // 电脑回合，根据AI逻辑决定是否要牌
        if (!game.getComputer().isStanding() && game.getState() == BlackjackGame.GameState.PLAYING) {
            // 如果是初始补牌，不进行额外的AI决策，直接返回玩家回合
            if (isInitialDeal) {
                // 回到玩家回合
                gameStatusLabel.setText("请选择要牌或停牌");
                gameStatusLabel.setForeground(Color.BLUE); // 玩家回合使用蓝色
                // 重新启用按钮
                hitButton.setEnabled(true);
                standButton.setEnabled(true);
            } else {
                boolean wantsToHit = game.checkComputerWantsToHit();

                if (wantsToHit) {
                    // 电脑选择要牌：给电脑玩家发牌
                    game.computerHit();
                    updateUI();

                    // 检查电脑是否爆牌
                    if (game.getState() == BlackjackGame.GameState.COMPUTER_BUST) {
                        // 返回游戏状态为电脑玩家爆牌
                        showGameResult();
                    } else {
                        // 电脑要牌后，添加延迟，增强回合感
                        try {
                            Thread.sleep(1500); // 增加延迟时间
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }

                        if (game.getHuman().isStanding()) {
                            // 如果玩家已经停牌，电脑需要连续决策直到停牌或爆牌
                            computerTurn(); // 递归调用，继续电脑的回合决策
                        } else {
                            // 如果玩家未停牌，回到玩家回合
                            gameStatusLabel.setText("请选择要牌或停牌");
                            gameStatusLabel.setForeground(Color.BLUE); // 玩家回合使用蓝色
                            // 重新启用按钮
                            hitButton.setEnabled(true);
                            standButton.setEnabled(true);
                        }
                    }
                } else {
                    // 电脑选择停牌：设置电脑玩家状态
                    game.getComputer().setStanding(true);
                    updateUI();

                    try {
                        Thread.sleep(1500); // 增加延迟时间
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    // 检查游戏是否结束（双方都停牌）
                    if (game.getHuman().isStanding()) {
                        // 双方都停牌，计分，返回游戏结果
                        game.finalizeGame();
                        showGameResult();
                    } else {
                        // 回到玩家回合
                        gameStatusLabel.setText("请选择要牌或停牌");
                        gameStatusLabel.setForeground(Color.BLUE); // 玩家回合使用蓝色
                        // 重新启用按钮
                        hitButton.setEnabled(true);
                        standButton.setEnabled(true);
                    }
                }
            }
        } else {
            // 检查游戏是否结束
            if (game.isGameOver()) {
                showGameResult();
            } else {
                // 如果游戏未结束，回到玩家回合
                gameStatusLabel.setText("请选择要牌或停牌");
                gameStatusLabel.setForeground(Color.BLUE); // 玩家回合使用蓝色
                // 重新启用按钮
                hitButton.setEnabled(true);
                standButton.setEnabled(true);
            }
        }
    }

    /**
     * 主方法，启动游戏GUI
     */
    public static void main(String[] args) {
        // 在事件调度线程中创建GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BlackjackGUI();
            }
        });
    }
}