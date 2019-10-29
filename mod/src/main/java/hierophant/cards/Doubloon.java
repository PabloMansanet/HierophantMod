
package hierophant.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hierophant.HierophantMod;
import hierophant.tags.HierophantTags;
import hierophant.characters.Hierophant;
import hierophant.powers.CharityPower;
import hierophant.powers.EnlightenedPower;
import hierophant.powers.GenerosityPower;
import hierophant.powers.PietyPower;

import static hierophant.HierophantMod.makeCardPath;

public class Doubloon extends AbstractDynamicCard {
    public static final String ID = HierophantMod.makeID(Doubloon.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Doubloon.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 0;
    private static final int MAGIC = 5;
    private static final int UPGRADE_PLUS_MAGIC = 5;
    private static final int DRAW = 1;

    // /STAT DECLARATION/

    public Doubloon() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        tags.add(HierophantTags.HIEROPHANT_HOARD);
        exhaust = true;
        tags.add(CardTags.HEALING); // To prevent generation
    }

    public Doubloon(boolean upgraded) {
        this();
        if (upgraded) {
            upgrade();
        }
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(GenerosityPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, p.getPower(GenerosityPower.POWER_ID).amount));
        }

        if (p.hasPower(CharityPower.POWER_ID)) {
            int pietyToGain = p.getPower(CharityPower.POWER_ID).amount;
            if (AbstractDungeon.player.hasPower(EnlightenedPower.POWER_ID)) {
                pietyToGain = (pietyToGain * EnlightenedPower.PIETY_BONUS) / 100;
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new PietyPower(p, p, pietyToGain), pietyToGain));
        }

        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, DRAW));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }
}
