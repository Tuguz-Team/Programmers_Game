package com.programmers.game;

import com.badlogic.gdx.utils.Array;
import com.programmers.enums.CardType;
import com.programmers.game.hotseat.HotseatPlayer;
import com.programmers.interfaces.ICard;
import com.programmers.interfaces.Procedure;

public final class GameCard implements ICard {

    private final CardType type;
    private final Array<GameCard> cards;
    private HotseatPlayer hotseatPlayer;

    public GameCard(final CardType type, final HotseatPlayer hotseatPlayer) {
        this.type = type;
        this.hotseatPlayer = hotseatPlayer;
        cards = new Array<>(2);
    }

    public CardType getType() {
        return type;
    }

    public Array<GameCard> getCards() {
        return cards;
    }

    public HotseatPlayer getHotseatPlayer() {
        return hotseatPlayer;
    }

    public void setHotseatPlayer(HotseatPlayer hotseatPlayer) {
        this.hotseatPlayer = hotseatPlayer;
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
        return hotseatPlayer.getCar().stepForward();
    }

    @Override
    public void stepForwardToFloor() {
        hotseatPlayer.getCar().stepForwardToFloor();
    }

    @Override
    public void jump() {
        hotseatPlayer.getCar().jump();
    }

    @Override
    public void turn90Left() {
        hotseatPlayer.getCar().turn90Left();
    }

    @Override
    public void turn90Right() {
        hotseatPlayer.getCar().turn90Right();
    }

    @Override
    public void turn180() {
        hotseatPlayer.getCar().turn180();
    }

    @Override
    public void cycle2(Procedure[] procedures) {
        hotseatPlayer.getCar().cycle2(procedures);
    }

    @Override
    public void cycle3(Procedure[] procedures) {
        hotseatPlayer.getCar().cycle3(procedures);
    }

    @Override
    public void teleport() {
        hotseatPlayer.getCar().teleport();
    }
}
