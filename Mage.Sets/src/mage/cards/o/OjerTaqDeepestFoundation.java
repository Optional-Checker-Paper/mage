package mage.cards.o;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.DiesSourceTriggeredAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.ReplacementEffectImpl;
import mage.abilities.keyword.TransformAbility;
import mage.abilities.keyword.VigilanceAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.game.Game;
import mage.game.events.CreateTokenEvent;
import mage.game.events.GameEvent;

import java.util.UUID;

/**
 * @author Susucr
 */
public final class OjerTaqDeepestFoundation extends CardImpl {

    public OjerTaqDeepestFoundation(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{4}{W}{W}");
        this.secondSideCardClazz = mage.cards.t.TempleOfCivilization.class;

        this.supertype.add(SuperType.LEGENDARY);
        this.subtype.add(SubType.GOD);
        this.power = new MageInt(6);
        this.toughness = new MageInt(6);

        // Vigilance
        this.addAbility(VigilanceAbility.getInstance());

        // If one or more creature tokens would be created under your control, three times that many of those tokens are created instead.
        this.addAbility(new SimpleStaticAbility(new OjerTaqDeepestFoundationTriplingEffect()));

        // When Ojer Taq dies, return it to the battlefield tapped and transformed under its owner's control.
        this.addAbility(new TransformAbility());
        this.addAbility(new DiesSourceTriggeredAbility(new OjerAxonilDeepestMightTransformEffect()));
    }

    private OjerTaqDeepestFoundation(final OjerTaqDeepestFoundation card) {
        super(card);
    }

    @Override
    public OjerTaqDeepestFoundation copy() {
        return new OjerTaqDeepestFoundation(this);
    }
}

class OjerTaqDeepestFoundationTriplingEffect extends ReplacementEffectImpl {

    OjerTaqDeepestFoundationTriplingEffect() {
        super(Duration.WhileOnBattlefield, Outcome.Copy);
        staticText = "If one or more creature tokens would be created under your control, "
                + "three times that many of those tokens are created instead.";
    }

    private OjerTaqDeepestFoundationTriplingEffect(final OjerTaqDeepestFoundationTriplingEffect effect) {
        super(effect);
    }

    @Override
    public OjerTaqDeepestFoundationTriplingEffect copy() {
        return new OjerTaqDeepestFoundationTriplingEffect(this);
    }

    @Override
    public boolean checksEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.CREATE_TOKEN;
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        return event.getPlayerId().equals(source.getControllerId())
                && (((CreateTokenEvent) event)
                .getTokens()
                .entrySet()
                .stream()
                .anyMatch(entry -> entry.getKey().isCreature(game) && entry.getValue() > 0));
    }

    @Override
    public boolean replaceEvent(GameEvent event, Ability source, Game game) {
        if (event instanceof CreateTokenEvent) {
            ((CreateTokenEvent) event).multiplyTokens(3, token -> token.isCreature(game));
        }
        return false;
    }
}