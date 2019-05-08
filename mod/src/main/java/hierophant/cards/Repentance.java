
package hierophant.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hierophant.HierophantMod;
import hierophant.characters.Hierophant;
import hierophant.powers.PietyPower;

import static hierophant.HierophantMod.makeCardPath;

public class Repentance extends AbstractDynamicCard {

    public static final String ID = HierophantMod.makeID(Repentance.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 1;

    public Repentance() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
        piety = basePiety = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new PietyPower(p, p, piety), piety));
        if (upgraded) {
            rawDescription = UPGRADE_DESCRIPTION;
        } else {
            rawDescription = DESCRIPTION;
        }
        initializeDescription();
    }

    public void applyPowers()
    {
      this.basePiety = 2 * HierophantMod.pietyLostInCombat;
      super.applyPowers();
      
      if (upgraded) {
          rawDescription = UPGRADE_DESCRIPTION;
      } else {
          rawDescription = DESCRIPTION;
      }
      rawDescription = rawDescription + EXTENDED_DESCRIPTION[0];
      initializeDescription();
    }

    public void onMoveToDiscard()
    {
        if (upgraded) {
            rawDescription = UPGRADE_DESCRIPTION;
        } else {
            rawDescription = DESCRIPTION;
        }
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
