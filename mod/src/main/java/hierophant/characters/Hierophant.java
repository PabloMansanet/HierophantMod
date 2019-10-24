package hierophant.characters;

import static hierophant.HierophantMod.HIEROPHANT_CORPSE;
import static hierophant.HierophantMod.HIEROPHANT_SHOULDER_1;
import static hierophant.HierophantMod.HIEROPHANT_SHOULDER_2;
import static hierophant.HierophantMod.makeID;
import static hierophant.characters.Hierophant.Enums.COLOR_GOLD;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import hierophant.HierophantMod;
import hierophant.cards.ChurchCoffers;
import hierophant.cards.Defend_Hierophant;
import hierophant.cards.Punish;
import hierophant.cards.SealAway;
import hierophant.cards.Strike_Hierophant;
import hierophant.relics.PropheticMaskRelic;

public class Hierophant extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(HierophantMod.class.getName());

    // =============== CHARACTER ENUMERATORS =================
    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass HIEROPHANT;
        @SpireEnum(name = "HIEROPHANT_GOLD_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor COLOR_GOLD;
        @SpireEnum(name = "HIEROPHANT_GOLD_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    // =============== CHARACTER ENUMERATORS  =================

    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    private static final String ID = makeID("HierophantCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "hierophantResources/images/char/hierophantCharacter/orb/layer1.png",
            "hierophantResources/images/char/hierophantCharacter/orb/layer2.png",
            "hierophantResources/images/char/hierophantCharacter/orb/layer3.png",
            "hierophantResources/images/char/hierophantCharacter/orb/layer4.png",
            "hierophantResources/images/char/hierophantCharacter/orb/layer5.png",
            "hierophantResources/images/char/hierophantCharacter/orb/layer6.png",
            "hierophantResources/images/char/hierophantCharacter/orb/layer1d.png",
            "hierophantResources/images/char/hierophantCharacter/orb/layer2d.png",
            "hierophantResources/images/char/hierophantCharacter/orb/layer3d.png",
            "hierophantResources/images/char/hierophantCharacter/orb/layer4d.png",
            "hierophantResources/images/char/hierophantCharacter/orb/layer5d.png",};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public Hierophant(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
                "hierophantResources/images/char/hierophantCharacter/orb/vfx.png", null,
                new SpriterAnimation("hierophantResources/images/char/hierophantCharacter/idle/idle.scml"));


        // =============== TEXTURES, ENERGY, LOADOUT =================

        initializeClass(null,
                HIEROPHANT_SHOULDER_1, // campfire pose
                HIEROPHANT_SHOULDER_2, // another campfire pose
                HIEROPHANT_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== /TEXT BUBBLE LOCATION/ =================

    }

    // =============== /CHARACTER CLASS END/ =================
    //
    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");

        for(int i = 0; i < 4; i = i + 1) {
           retVal.add(Strike_Hierophant.ID);
        }
        retVal.add(Punish.ID);
        for(int i = 0; i < 4; i = i + 1) {
           retVal.add(Defend_Hierophant.ID);
        }
        retVal.add(ChurchCoffers.ID);

        return retVal;
    }

    // Starting Relics
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(PropheticMaskRelic.ID);
        UnlockTracker.markRelicAsSeen(PropheticMaskRelic.ID);

        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 0;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return COLOR_GOLD;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return HierophantMod.HIEROPHANT_GOLD;
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new SealAway();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new Hierophant(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return HierophantMod.HIEROPHANT_GOLD;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return HierophantMod.HIEROPHANT_GOLD;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    @Override
    public String getVampireText() {
        return TEXT[2];
    }

}
