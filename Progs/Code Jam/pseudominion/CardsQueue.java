class CardsQueue {
    Card[] cards = new Card[80];
    int top = -1;
    int rear = -1;
    int length = 0;

    public void insert(Card c) {
	if(top == -1 && rear == -1) {
	    ++top;
	}
	cards[++rear] = c;
	length++;
    }

    public Card get() {
	if(top < rear) {
	    length--;
	    return cards[top++];
	}
	else if(top == rear && top != -1) {
	    Card temp = cards[top];
	    top = -1;
	    rear = -1;
	    length--;
	    return temp;
	}   
	return null;
    }

    public boolean notEmpty() {
	return length > 0;
    }
}
