package com.programmers.game;

import com.badlogic.gdx.utils.Array;
import com.programmers.enums.CardType;
import com.programmers.interfaces.ICard;
import com.programmers.interfaces.Procedure;

public final class Card implements ICard {

    private final CardType type;
    private final Array<Card> cards;
    private Player player;

    public Card(final CardType type, final Player player) {
        this.type = type;
        this.player = player;
        cards = new Array<>(2);
    }

    public CardType getType() {
        return type;
    }

    public Array<Card> getCards() {
        return cards;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void apply() {
        Procedure[] procedures;
        switch (type) {
            case StepForward:
                stepForward();
                break;
            case StepForwardToFloor:
                stepForwardToFloor();
                break;
            case Jump:
                jump();
                break;
            case Turn90Left:
                turn90Left();
                break;
            case Turn90Right:
                turn90Right();
                break;
            case Turn180:
                turn180();
                break;
            case Cycle2:
                procedures = new Procedure[cards.size];
                for (int i = 0; i < cards.size; i++) {
                    final int finalI = i;
                    procedures[i] = new Procedure() {
                        @Override
                        public void call() {
                            cards.get(finalI).apply();
                        }
                    };
                }
                cycle2(procedures);
                break;
            case Cycle3:
                procedures = new Procedure[cards.size];
                for (int i = 0; i < cards.size; i++) {
                    final int finalI = i;
                    procedures[i] = new Procedure() {
                        @Override
                        public void call() {
                            cards.get(finalI).apply();
                        }
                    };
                }
                cycle3(procedures);
                break;
            case Teleport:
                teleport();
                break;
        }
    }

    @Override
    public boolean stepForward() {
        return player.getCar().stepForward();
    }

    @Override
    public void stepForwardToFloor() {
        player.getCar().stepForwardToFloor();
    }

    @Override
    public void jump() {
        player.getCar().jump();
    }

    @Override
    public void turn90Left() {
        player.getCar().turn90Left();
    }

    @Override
    public void turn90Right() {
        player.getCar().turn90Right();
    }

    @Override
    public void turn180() {
        player.getCar().turn180();
    }

    @Override
    public void cycle2(Procedure[] procedures) {
        player.getCar().cycle2(procedures);
    }

    @Override
    public void cycle3(Procedure[] procedures) {
        player.getCar().cycle3(procedures);
    }

    @Override
    public void teleport() {
        player.getCar().teleport();
    }
}
