
/**
 * 21点游戏图形用户界面
 * 基于Java Swing实现的21点游戏GUI
 * 提供直观的图形界面操作和游戏状态显示
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BlackjackGUI extends JFrame {
    private BlackjackGame game;

    // GUI组件
    private JPanel playerCardsPanel;
    private JPanel computerCardsPanel;
    private JLabel playerScoreLabel;
    private JLabel computerScoreLabel;
    private JLabel gameStatusLabel;
    private JButton hitButton;
    private JButton standButton;
    private JButton newGameButton;
    private JTextArea messageArea;

    /**
     * 构造函数，初始化GUI组件和游戏逻辑
     */
    public BlackjackGUI() {
        game = new BlackjackGame();
        initializeGUI();
        startNewGame();
    }

    /**
     * 初始化图形界面组件
     */
    private void initializeGUI() {
        setTitle("21点游戏 - Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // 创建主面板
        setLayout(new BorderLayout());

        // 创建顶部面板 - 游戏状态
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // 创建中间面板 - 卡牌显示区域
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // 创建底部面板 - 操作按钮
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // 创建右侧面板 - 消息显示区域
        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.EAST);
    }

    /**
     * 创建顶部状态面板
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(0, 120, 50)); // 深绿色背景
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gameStatusLabel = new JLabel("欢迎来到21点游戏！");
        gameStatusLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        gameStatusLabel.setForeground(Color.WHITE);

        panel.add(gameStatusLabel);
        return panel;
    }

    /**
     * 创建中间卡牌显示面板
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(new Color(0, 80, 30)); // 较深的绿色
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 电脑玩家区域
        computerCardsPanel = createPlayerPanel("电脑玩家");
        computerScoreLabel = new JLabel("分数: 0");
        computerScoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        computerScoreLabel.setForeground(Color.WHITE);

        JPanel computerPanel = new JPanel(new BorderLayout());
        computerPanel.setBackground(new Color(0, 100, 40));
        computerPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "电脑玩家",
                0, 0,
                new Font("微软雅黑", Font.BOLD, 14),
                Color.WHITE));
        computerPanel.add(computerCardsPanel, BorderLayout.CENTER);
        computerPanel.add(computerScoreLabel, BorderLayout.SOUTH);

        // 人类玩家区域
        playerCardsPanel = createPlayerPanel("你的手牌");
        playerScoreLabel = new JLabel("分数: 0");
        playerScoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        playerScoreLabel.setForeground(Color.WHITE);

        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBackground(new Color(0, 100, 40));
        playerPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "你的手牌",
                0, 0,
                new Font("微软雅黑", Font.BOLD, 14),
                Color.WHITE));
        playerPanel.add(playerCardsPanel, BorderLayout.CENTER);
        playerPanel.add(playerScoreLabel, BorderLayout.SOUTH);

        panel.add(computerPanel);
        panel.add(playerPanel);

        return panel;
    }

    /**
     * 创建玩家面板（显示手牌）
     */
    private JPanel createPlayerPanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(0, 100, 40));
        return panel;
    }

    /**
     * 创建底部操作按钮面板
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(0, 120, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 要牌按钮
        hitButton = new JButton("要牌 (Hit)");
        hitButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        hitButton.setPreferredSize(new Dimension(120, 40));
        hitButton.addActionListener(new HitButtonListener());

        // 停牌按钮
        standButton = new JButton("停牌 (Stand)");
        standButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        standButton.setPreferredSize(new Dimension(120, 40));
        standButton.addActionListener(new StandButtonListener());

        // 新游戏按钮
        newGameButton = new JButton("新游戏");
        newGameButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        newGameButton.setPreferredSize(new Dimension(120, 40));
        newGameButton.setBackground(new Color(220, 100, 100));
        newGameButton.addActionListener(new NewGameButtonListener());

        panel.add(hitButton);
        panel.add(standButton);
        panel.add(newGameButton);

        return panel;
    }

    /**
     * 创建右侧消息显示面板
     */
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBorder(BorderFactory.createTitledBorder("游戏消息"));

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        messageArea.setBackground(new Color(250, 250, 250));
        messageArea.setText("游戏开始！\n");

        JScrollPane scrollPane = new JScrollPane(messageArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 创建卡牌显示标签
     */
    private JLabel createCardLabel(Card card) {
        // 获取卡牌显示文本
        String cardText = card.toString();

        JLabel cardLabel = new JLabel(cardText);
        // 使用支持Unicode的字体
        cardLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        cardLabel.setForeground(Color.BLACK);
        cardLabel.setBackground(Color.WHITE);
        cardLabel.setOpaque(true);
        cardLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardLabel.setVerticalAlignment(SwingConstants.CENTER);
        cardLabel.setPreferredSize(new Dimension(80, 100)); // 适当增大尺寸以显示符号

        // 根据花色设置颜色
        String suit = card.getSuit();
        if (suit.equals("\u2665") || suit.equals("\u2666")) { // ♥=红桃, ♦=方块
            cardLabel.setForeground(Color.RED);
        } else { // \u2660=黑桃, \u2663=梅花
            cardLabel.setForeground(Color.BLACK);
        }

        return cardLabel;
    }

    /**
     * 创建隐藏卡牌标签
     */
    private JLabel createHiddenCardLabel() {
        JLabel cardLabel = new JLabel("??");
        cardLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        cardLabel.setForeground(Color.WHITE);
        cardLabel.setBackground(new Color(100, 100, 100));
        cardLabel.setOpaque(true);
        cardLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardLabel.setPreferredSize(new Dimension(60, 80));

        return cardLabel;
    }

    /**
     * 更新显示界面
     */
    private void updateDisplay() {
        // 更新人类玩家手牌
        updatePlayerHand();

        // 更新电脑玩家手牌
        updateComputerHand();

        // 更新游戏状态
        updateGameStatus();

        // 更新按钮状态
        updateButtonStates();

        // 刷新界面
        repaint();
    }

    /**
     * 更新人类玩家手牌显示
     */
    private void updatePlayerHand() {
        playerCardsPanel.removeAll();

        List<Card> humanHand = game.getHumanHand();
        for (Card card : humanHand) {
            playerCardsPanel.add(createCardLabel(card));
        }

        playerScoreLabel.setText("分数: " + game.getHumanScore());
        playerCardsPanel.revalidate();
    }

    /**
     * 更新电脑玩家手牌显示
     */
    private void updateComputerHand() {
        computerCardsPanel.removeAll();

        List<Card> computerHand = game.getComputerHand();

        // 显示所有牌（因为现在每人只有一张初始牌）
        for (Card card : computerHand) {
            computerCardsPanel.add(createCardLabel(card));
        }
        computerScoreLabel.setText("分数: " + game.getComputerScore());

        computerCardsPanel.revalidate();
    }

    /**
     * 更新游戏状态显示
     */
    private void updateGameStatus() {
        String status = "";
        switch (game.getState()) {
            case PLAYING:
                if (game.getHuman().isStanding()) {
                    status = "电脑正在决策...";
                } else {
                    status = "轮到你了，选择要牌或停牌";
                }
                break;
            case HUMAN_BUST:
                status = "你爆牌了！电脑获胜";
                break;
            case COMPUTER_BUST:
                status = "电脑爆牌！你赢了";
                break;
            case HUMAN_WIN:
                status = "恭喜你赢了！";
                break;
            case COMPUTER_WIN:
                status = "电脑赢了！";
                break;
            case DRAW:
                status = "平局！";
                break;
            default:
                status = "游戏准备中...";
        }
        gameStatusLabel.setText(status);
    }

    /**
     * 更新按钮可用状态
     */
    private void updateButtonStates() {
        boolean gameInProgress = (game.getState() == BlackjackGame.GameState.PLAYING);
        boolean humanCanPlay = gameInProgress && !game.getHuman().isStanding();

        hitButton.setEnabled(humanCanPlay);
        standButton.setEnabled(humanCanPlay);
    }

    /**
     * 添加消息到消息区域
     */
    private void addMessage(String message) {
        messageArea.append(message + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    /**
     * 开始新游戏
     */
    private void startNewGame() {
        game.startGame();
        updateDisplay();
        addMessage("新游戏开始！");
        addMessage("每位玩家发到一张牌");
    }

    /**
     * 要牌按钮监听器
     */
    private class HitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.getState() == BlackjackGame.GameState.PLAYING &&
                    !game.getHuman().isStanding()) {

                game.humanHit();
                addMessage("你选择了要牌");
                updateDisplay();

                if (game.getState() == BlackjackGame.GameState.HUMAN_BUST) {
                    addMessage("你爆牌了！");
                    game.finalizeGame();
                    updateDisplay();
                    return;
                }

                // 电脑回合
                if (!game.getHuman().isStanding()) {
                    processComputerTurn();
                }
            }
        }
    }

    /**
     * 停牌按钮监听器
     */
    private class StandButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.getState() == BlackjackGame.GameState.PLAYING) {
                game.humanStand();
                addMessage("你选择了停牌");
                updateDisplay();

                // 继续电脑回合直到游戏结束
                playComputerTurns();
            }
        }

        /**
         * 处理电脑的所有回合，直到游戏结束
         */
        private void playComputerTurns() {
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (game.isGameOver()) {
                        ((Timer) e.getSource()).stop();
                        game.finalizeGame();
                        updateDisplay();
                        return;
                    }

                    if (game.getState() == BlackjackGame.GameState.PLAYING &&
                            !game.getComputer().isStanding()) {

                        boolean wantsToHit = game.checkComputerWantsToHit();
                        if (wantsToHit) {
                            addMessage("电脑选择了要牌");
                            game.computerHit();

                            if (game.getState() == BlackjackGame.GameState.COMPUTER_BUST) {
                                addMessage("电脑爆牌了！");
                                ((Timer) e.getSource()).stop();
                                game.finalizeGame();
                                updateDisplay();
                                return;
                            }
                        } else {
                            addMessage("电脑选择了停牌");
                            game.getComputer().setStanding(true);
                        }

                        updateDisplay();
                    }

                    // 如果游戏结束，停止计时器
                    if (game.isGameOver()) {
                        ((Timer) e.getSource()).stop();
                        game.finalizeGame();
                        updateDisplay();
                    }
                }
            });
            timer.start();
        }
    }

    /**
     * 新游戏按钮监听器
     */
    private class NewGameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            messageArea.setText("");
            startNewGame();
        }
    }

    /**
     * 处理电脑回合
     */
    private void processComputerTurn() {
        if (game.getState() != BlackjackGame.GameState.PLAYING ||
                game.getComputer().isStanding()) {
            return;
        }

        // 添加一点延迟，让游戏更有真实感
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Timer) e.getSource()).stop();

                boolean wantsToHit = game.checkComputerWantsToHit();
                if (wantsToHit && !game.getComputer().isStanding()) {
                    addMessage("电脑选择了要牌");
                    game.computerHit();

                    if (game.getState() == BlackjackGame.GameState.COMPUTER_BUST) {
                        addMessage("电脑爆牌了！");
                    }
                } else {
                    addMessage("电脑选择了停牌");
                    game.getComputer().setStanding(true);
                }

                updateDisplay();

                if (game.isGameOver()) {
                    game.finalizeGame();
                    updateDisplay();
                }
            }
        });
        timer.start();
    }

    /**
     * 主方法，启动GUI应用
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // 设置系统外观
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new BlackjackGUI().setVisible(true);
            }
        });
    }
}