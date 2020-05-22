package com.programmers.ui_elements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.programmers.enums.Difficulty;
import com.programmers.game.GameCard;
import com.programmers.game.GameController;
import com.programmers.game_objects.Car;
import com.programmers.screens.ScreenLoader;

public final class AlgorithmCardWindow extends Table {

    private final GameController gameController;

    private final Button button;
    private CardContainer actionsCardContainer;
    private CycleCardContainer cyclesCardContainer;

    public AlgorithmCardWindow(final String name, final GameController gameController, final AssetManager assetManager) {
        this.gameController = gameController;
        setFillParent(true);
        final Table table = new Table();
        button = new Button(
                new TextureRegionDrawable((Texture) assetManager.get("Sprites/AlgorithmButton/StartButtonOn.png")),
                new TextureRegionDrawable((Texture) assetManager.get("Sprites/AlgorithmButton/StartButtonOff.png"))
        );

        Label label = new Label(name, ScreenLoader.getGameSkin());
        label.setFontScale(2);

        add(label).bottom();
        add(table).right().bottom();
        // if difficulty is hard
        if (gameController.getDifficulty() == Difficulty.Hard) {
            table.add(button).colspan(2).top().row();
            cyclesCardContainer = new CycleCardContainer(gameController);
            table.add(cyclesCardContainer).spaceRight(10).bottom();
        } else {
            table.add(button).top().row();
        }
        actionsCardContainer = new CardContainer(
                gameController.getAlgorithmToDo(), gameController.getDifficulty(),
                CardContainer.Content.Actions, gameController) {
            @Override
            protected void setTouchable() {
            }

            @Override
            protected void childrenChanged() {
                super.childrenChanged();
                if (areContainersEmpty()) {
                    gameController.getPlayerCardWindow().enableButton();
                    if (cyclesCardContainer != null)
                        cyclesCardContainer.zeroingPoints();
                } else {
                    gameController.getPlayerCardWindow().disableButton();
                    if (cyclesCardContainer != null && actionsCardContainer.getChildren().size == 1
                            && cyclesCardContainer.getCells().get(cyclesCardContainer.getCells().size - 1).getActor() == null)
                        cyclesCardContainer.drawLast();
                }

                if (gameController.getPlayerCardWindow().getCardContainer().getChildren().size == 5)
                    gameController.getPlayerCardWindow().enableButton();
            }
        };
        table.add(actionsCardContainer).bottom();
        right().bottom();
        //
        button.addListener(new MyButton.Listener() {
            @Override
            public void call() {
                final YesNoDialog dialog = new YesNoDialog("   Do you want to run algorithm? "
                        + "Move will go to next player.   ", ScreenLoader.getGameSkin()) {
                    @Override
                    public void call() {
                        gameController.getAlgorithmToDo().clear();
                        if (gameController.getDifficulty() == Difficulty.Easy) {
                            Array<Actor> array = actionsCardContainer.getChildren();
                            for (int i = 0; i < array.size + 10; i++) {
                                Card card = (Card) array.get(0);
                                GameCard gameCard = card.getGameCard();
                                if (gameCard == null)
                                    continue;
                                gameCard.apply();
                                gameController.getDiscardPile().add(gameCard);
                                gameController.getAlgorithmToDo().add(gameCard);

                                Cell cell = getCell(array.get(0));
                                array.get(0).remove();
                                getCells().removeValue(cell, true);
                                invalidate();

                                actionsCardContainer.childrenChanged();
                            }
                            actionsCardContainer.controlEmpty();
                            gameController.toNextPlayer();
                        } else {
                            // algorithm
                            Array<Actor> actions = actionsCardContainer.getChildren();
                            int n = cyclesCardContainer.getCells().size;

                            for (int i = 0; i < n; i++) {
                                Card thisCycleCard = (Card) cyclesCardContainer.getCells().get(i).getActor();
                                if (thisCycleCard != null) {
                                    if (thisCycleCard.getGameCard() != null) {
                                        if (i % 2 == 0) {
                                            thisCycleCard.getGameCard().getCards().add(
                                                    ((Card) actions.get(mathCycleFull(i, actions.size))).getGameCard()
                                            );
                                            thisCycleCard.getGameCard().apply();
                                            gameController.getAlgorithmToDo().add(thisCycleCard.getGameCard());
                                        } else {
                                            thisCycleCard.getGameCard().getCards().add(
                                                    ((Card) actions.get(mathCycleFull(i - 1, actions.size))).getGameCard(),
                                                    ((Card) actions.get(mathCycleFull(i - 1, actions.size) + 1)).getGameCard()
                                            );
                                            thisCycleCard.getGameCard().apply();
                                            gameController.getAlgorithmToDo().add(thisCycleCard.getGameCard());
                                        }
                                    } else {
                                        if (i % 2 != 0 || i == n - 1) {
                                            Card actionCard = (Card) actions.get(mathCycleEmptyOdd(i, actions.size));
                                            if (actionCard.getGameCard() != null) {
                                                if (i == n - 1 || i == 1) {
                                                    actionCard.getGameCard().apply();
                                                    gameController.getAlgorithmToDo().add(actionCard.getGameCard());
                                                } else if (i % 2 != 0) {
                                                    Card prevCycleCard = (Card) cyclesCardContainer.getCells().get(i - 1).getActor(),
                                                            _prevCycleCard = (Card) cyclesCardContainer.getCells().get(i - 2).getActor();
                                                    if ((prevCycleCard != null && prevCycleCard.getGameCard() == null)
                                                            || (_prevCycleCard != null && _prevCycleCard.getGameCard() == null)) {
                                                        actionCard.getGameCard().apply();
                                                        gameController.getAlgorithmToDo().add(actionCard.getGameCard());
                                                    }
                                                }
                                            }
                                        } else if (i < n - 1) {
                                            Card actionCard = (Card) actions.get(mathCycleEmptyEven(i, actions.size));
                                            if (actionCard.getGameCard() != null) {
                                                Card nextCycleCard = (Card) cyclesCardContainer.getCells().get(i + 1).getActor();
                                                if (i == 0 && nextCycleCard == null) {
                                                    actionCard.getGameCard().apply();
                                                    gameController.getAlgorithmToDo().add(actionCard.getGameCard());
                                                } else if (nextCycleCard == null) {
                                                    actionCard.getGameCard().apply();
                                                    gameController.getAlgorithmToDo().add(actionCard.getGameCard());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            // set position if car is on the Lift
                            Car car = gameController.getThisPlayer().getCar();
                            if (car.getChunk().getLift() != null) {
                                car.setCompensated(false);
                                car.setPosition(car.getChunk().getLift());
                                car.addLivesFrom(car.getChunk());
                            }
                            for (Cell cell : cyclesCardContainer.getCells()) {
                                Card card = (Card) cell.getActor();
                                if (card != null && card.getGameCard() != null) {
                                    card.getGameCard().getCards().clear();
                                    gameController.getDiscardPile().add(((Card) cell.getActor()).getGameCard());
                                }
                            }
                            if (actions.size == 5) {
                                for (Actor actor : actions)
                                    gameController.getDiscardPile().add(((Card) actor).getGameCard());
                                actionsCardContainer.clearChildren();
                                cyclesCardContainer.clearChildren();
                            }

                            gameController.toNextPlayer();
                            actionsCardContainer.setActionToPrevious();
                            cyclesCardContainer.drawPoints(actions.size,0);

                            for (Actor actor : actions) {
                                if (((Card) actor).getGameCard() != null)
                                    ((Card) actor).getGameCard().setPlayer(gameController.getThisPlayer());
                            }
                        }
                    }
                };
                dialog.show(gameController.getGameScreen());
            }
        });
    }

    public void enable() {
        button.setTouchable(Touchable.enabled);
        actionsCardContainer.setTouchable(Touchable.enabled);
        CardContainer.cardContainers.add(actionsCardContainer);
        if (gameController.getDifficulty() == Difficulty.Hard) {
            cyclesCardContainer.setTouchable(Touchable.enabled);
            CardContainer.cardContainers.add(cyclesCardContainer);
        }
    }

    public void disable() {
        button.setTouchable(Touchable.disabled);
        actionsCardContainer.setTouchable(Touchable.disabled);
        CardContainer.cardContainers.removeValue(actionsCardContainer, false);
        if (gameController.getDifficulty() == Difficulty.Hard && cyclesCardContainer != null) {
            cyclesCardContainer.setTouchable(Touchable.disabled);
            CardContainer.cardContainers.removeValue(cyclesCardContainer,false);
        }
    }

    boolean areContainersEmpty() {
        if (actionsCardContainer != null) {
            boolean flag = ((Card)actionsCardContainer.getChild(0)).getGameCard() == null;
            if (gameController.getDifficulty() == Difficulty.Hard
                && cyclesCardContainer != null
                && !cyclesCardContainer.getChildren().isEmpty()) {
                    return flag && (((Card)cyclesCardContainer.getChild(0)).getGameCard() == null);
            }
            return flag;
        }
        return true;
    }

    private int mathCycleFull(int i, int size) {
        int a = cyclesCardContainer.actionSizeToUse(),
        b = size + a;
        b = (b % 2 == 0) ? (b / 2) : (b / 2 + 1);
        b = (size > a) ? (i / b) : (i / b - 1);
        if (i == cyclesCardContainer.getCells().size - 1 && a > 0) b++;
        if (b >= size) b = size - 1;
        return b;
    }

    private int mathCycleEmptyOdd(int i, int size) {
        int k = i - (size + cyclesCardContainer.actionSizeToUse());

        if (i == size) k = 2;
        else if (k < 0) k = 0;
        else if (k == cyclesCardContainer.actionSizeToUse()) k = 1;
        else if (i - size == 1) k = 1;

        return k;
    }

    private int mathCycleEmptyEven(int i, int size) {
        int k = i - (size + cyclesCardContainer.actionSizeToUse());
        k = k + (cyclesCardContainer.actionSizeToUse() < 0 ? 2 : 1);

        if (k < 0) k = 0;
        else if (size >= cyclesCardContainer.actionSizeToUse() && k == 0) k = 1;
        else if (k > size - 2) k = size - 2;

        return k;
    }

    public CardContainer getCyclesCardContainer() {
        return cyclesCardContainer;
    }

    public CardContainer getActionsCardContainer() {
        return actionsCardContainer;
    }
}
