package hierophant.relics;

import static hierophant.HierophantMod.makeRelicOutlinePath;
import static hierophant.HierophantMod.makeRelicPath;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.abstracts.CustomRelic;
import hierophant.HierophantMod;
import hierophant.util.TextureLoader;

public class AtriumRelic extends CustomRelic {
    // ID, images, text.
    public static final String ID = HierophantMod.makeID("AtriumRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("donation.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("donation.png"));

    public AtriumRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public void onPlayerEndTurn() {
        int cost = 0;
        int index = 0;
        int highestCardPosition = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.costForTurn > cost) {
                highestCardPosition = index;
                cost = c.costForTurn;
            }
            index++;
        }
        AbstractCard highest = AbstractDungeon.player.hand.group.get(highestCardPosition);
        highest.retain = true;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
