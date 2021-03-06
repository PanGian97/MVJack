package com.company;

import com.company.Rules.AceRules;
import com.company.Rules.BlackJRules;

import java.util.ArrayList;

public class Presenter implements IPresenter{

        IView view;
        Dealer dealer = new Dealer();

        IDeckCreate trapoula = new DeckCreate();
        AceRules aceRule = new BlackJRules();
        private static ArrayList<Player> playerList = new ArrayList<>(4);
        private static ArrayList<Integer> results = new ArrayList<>();
        private static int playerNum = 0;

        public Presenter(IView view) {

            this.view = view;
        }

       public void fullGame(int playerNum){
            this.playerNum = playerNum;
           trapoula.deckCreate();

            for (int i = 0; i < playerNum; i++) {
                playerList.add(new Player());
            }
            GiveStartingCards();

            for (int i = 0; i < playerNum; i++) {//players round
                playerGame(i);
            }
            dealerGame();//dealer round

        }
@Override
        public void GiveStartingCards() {
            for (int x = 0; x < 2; x++) {
                for (int i = 0; i < playerList.size(); i++) {
                    Player currPlayer = playerList.get(i);
                    currPlayer.addCardToPlayersHand(trapoula.getDeck().get(0));
                    trapoula.removeDeckCard(0);
                    view.printStartPlayerHand(i,currPlayer);
                }
                dealer.addCardToDealerHand(trapoula.getDeck().get(0));// while players have drawn
                trapoula.removeDeckCard(0);
                  view.printStartDealerHand(dealer);
            }
        }
@Override
        public void giveSinglePlayerCard(int player) {
            Player currPlayer = playerList.get(player);
            Card drawnCard = trapoula.getDeck().get(0);
           if( aceRule.checkAndSetAceValue(currPlayer,drawnCard)) System.out.println("ACE IS 1");
            currPlayer.addCardToPlayersHand(drawnCard);
            view.printPlayerDrawResult();
            currPlayer.printDrawnCard(drawnCard.getAll());//will change
            trapoula.removeDeckCard(0);
            //return drawnCard;
        }


    @Override
        public void giveSingleDealerCard() {
            Card drawnCard = trapoula.getDeck().get(0);
          if( aceRule.checkAndSetAceValue(dealer,drawnCard))System.out.println("ACE IS 1");
            dealer.addCardToDealerHand(drawnCard);
            view.printDealerDrawResult();
            dealer.printDealerLastCard();//will change
            trapoula.removeDeckCard(0);

        }
@Override
        public void playerGame(int player) {
            boolean draw = true;
            Player currentPlayer =  playerList.get(player);
            int playerHandValue;

            while (draw) {
                playerHandValue = currentPlayer.getCurrentHandValue();
              draw = view.viewPlayerGame(player,playerHandValue,draw);
            }
        }
@Override
        public Player bestPlayerScore() {
            Player bestPlayer = new Player();
            int comparedPlayerValue=0;
            for (int i = 0; i < playerNum; i++) {
                comparedPlayerValue = playerList.get(i).getCurrentHandValue();
                if (bestPlayer.getCurrentHandValue() <= comparedPlayerValue && comparedPlayerValue<=21) {
                    bestPlayer = playerList.get(i);
                }
            }
            return bestPlayer;
        }

@Override
        public  void dealerGame() {
            boolean draw = true;
            int maxPlayerValue = 0;
            int dealerHandValue;

            while (draw) {
                dealerHandValue = dealer.getDealerCurrentHandValue();
               draw=  view.viewDealerGame(dealerHandValue,draw,maxPlayerValue);

            }
        }

@Override
        public void playerResults(){
            int currentPlayerHand=0;
            int maxAcceptedValue=0;
            int playerIndex=0;
            Player silverWinner;
            //playerIndex=-1;//case we have all players above 21...not used now cause we have checked this in dealerTurn()

            for (int i=0;i<playerList.size();i++)//+dealer
            { currentPlayerHand = playerList.get(i).getCurrentHandValue();
                if(currentPlayerHand <=21){

                    if(maxAcceptedValue < currentPlayerHand){
                        playerIndex = i;
                        maxAcceptedValue = currentPlayerHand;

                    }
                }
            }
            if(maxAcceptedValue!=21){
                silverWinner = aceRule.checkForSilverWin(playerList);
                if(silverWinner!= null) {
                    view.printPlayerWinner(silverWinner);
                }else return;
            }

           else if(playerIndex>=0)view.printPlayerWinner(playerIndex,maxAcceptedValue);

        }


}


