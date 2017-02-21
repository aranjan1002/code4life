class MedianFinder {
priority_queue<long> small, large;
public:

    void addNum(int num) {
        large.push(num);
        small.push(-large.top());
        large.pop();
        if (small.size() > large.size()) {
            large.push(-small.top());
            small.pop();
        }
    }

    double findMedian() {
        return small.size() < large.size()
               ? large.top()
               : (large.top() - small.top()) / 2.0;
    }
};

// Your MedianFinder object will be instantiated and called as such:
// MedianFinder mf;
// mf.addNum(1);
// mf.findMedian();