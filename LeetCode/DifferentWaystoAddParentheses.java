
void print(int i) {
        cout << i << " ";
    }
class DifferentWaystoAddParentheses {
public:
    void apply(vector<int>& left, vector<int>& right, char c) {
        for (int i = 0; i < left.size(); ++i) 
        {
            switch (c) 
            {
                case '+':
                    left[i] += right[i];
                    break;
                case '-':
                    left[i] -= right[i];
                    break;
                case '*':
                    left[i] *= right[i];
                    break;
                default:
                    break;
            }
        }
    }
    
    

    vector<int> diffWaysToCompute(string input) {
        vector<int> result;
        //cout << input << endl;
        for (int i = 0; i < input.length(); ++i) 
        {
            if (input[i] == '+' || input[i] == '-' || input[i] == '*') 
            {
                vector<int> left = diffWaysToCompute(input.substr(0, i));
                vector<int> right = diffWaysToCompute(input.substr(i + 1));
                
                for (int j = 0; j < left.size(); ++j) {
                    for (int k = 0; k < right.size(); ++k) 
                    {int val = 0;
                        switch (input[i]) 
                        {
                            case '+':
                                val = left[j] + right[k];
                                break;
                            case '-':
                                val = left[j] - right[k];
                                break;
                            case '*':
                                val = left[j] * right[k];
                                break;
                            default:
                                break;
                        }
                        result.push_back(val);
                    }
                }
            }
        }
        if (result.empty()) 
        {
            result.push_back(atoi(input.c_str()));
        }
        sort(result.begin(), result.end());
        //for_each(result.begin(), result.end(), print);
        //cout << endl;
        return result;
    }
};