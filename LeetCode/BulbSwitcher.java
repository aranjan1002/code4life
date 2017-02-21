class BulbSwitcher {
public:
    int bulbSwitch(int n) {
        int result = 0;
        while ((result + 1) * (result + 1) <= n) result++;
        return result;
    }
};