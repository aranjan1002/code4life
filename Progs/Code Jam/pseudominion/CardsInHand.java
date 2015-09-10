class CardsInHand {
    public CardsQueue tCards = new CardsQueue();
    public CardsQueue cCards = new CardsQueue();
    public SCards sCards = new SCards();
    
    public void insert(Card c) {
	if(c.t > 0)
	    tCards.insert(c);
	else if(c.c > 0)
	    cCards.insert(c);
	else if(c.s > 0)
	    sCards.insert(c);
    }

}