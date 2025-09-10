/**
 * 扑克牌类
 * 表示21点游戏中的一张扑克牌，包含花色和数值属性
 * 在21点游戏中，J、Q、K的分值均为10点，A可以为1点或11点（由游戏逻辑决定）
 */
public class Card {
    /**
     * 扑克牌的花色
     * 支持的花色包括：♠（黑桃）、♥（红桃）、♦（方块）、♣（梅花）
     */
    private final String suit;

    /**
     * 扑克牌的数值
     * 取值范围：1-13，其中1代表A，11代表J，12代表Q，13代表K
     */
    private final int value;

    /**
     * 构造方法，创建一张具有指定花色和数值的扑克牌
     * 
     * @param suit  花色（♠、♥、♦、♣之一）
     * @param value 牌面数值（1-13，其中1代表A，11代表J，12代表Q，13代表K）
     */
    public Card(String suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    /**
     * 获取牌的基础分值
     * 在21点游戏中，J、Q、K的分值为10点，其他牌的分值为其数值本身
     * 注意：此方法返回的是基础分值，A的最终分值可能在游戏逻辑中根据情况调整为11点
     * 
     * @return 牌的基础分值
     */
    public int getScore() {
        return (value >= 10) ? 10 : value; // J/Q/K=10
    }

    /**
     * 获取牌的原始数值
     * 
     * @return 牌面数值（1-13，其中1代表A，11代表J，12代表Q，13代表K）
     */
    public int getValue() {
        return value;
    }

    /**
     * 获取牌的花色
     * 
     * @return 花色（♠、♥、♦、♣之一）
     */
    public String getSuit() {
        return suit;
    }

    /**
     * 重写toString方法，返回格式化的牌面信息字符串
     * 例如：♠A、♥10、♦J、♣K
     * 
     * @return 格式化的牌面信息字符串
     */
    @Override
    public String toString() {
        String faceValue;
        switch (value) {
            case 1:
                faceValue = "A";
                break;
            case 11:
                faceValue = "J";
                break;
            case 12:
                faceValue = "Q";
                break;
            case 13:
                faceValue = "K";
                break;
            default:
                faceValue = String.valueOf(value);
        }

        // 直接返回花色符号和数字
        return suit + faceValue;
    }
}