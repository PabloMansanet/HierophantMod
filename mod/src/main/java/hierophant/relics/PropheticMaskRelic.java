package hierophant.relics;

import static hierophant.HierophantMod.makeRelicOutlinePath;
import static hierophant.HierophantMod.makeRelicPath;

import com.megacrit.cardcrawl.random.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.abstracts.CustomRelic;
import hierophant.HierophantMod;
import hierophant.util.TextureLoader;

public class PropheticMaskRelic extends CustomRelic {
    // ID, images, text.
    public static final String ID = HierophantMod.makeID("PropheticMaskRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("PropheticMask.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("PropheticMask.png"));

    public PropheticMaskRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public void onPlayerEndTurn() {
        int cost = 0;
        int index = 0;
        java.util.Vector<Integer> candidateIndices = new java.util.Vector<Integer>();
        int highestCost = 0;
        if (AbstractDungeon.player.hand.isEmpty()) {
            return;
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.costForTurn > highestCost && !c.isEthereal) {
                candidateIndices.clear();
                highestCost = c.costForTurn;
                candidateIndices.add(index);
            } else if (c.costForTurn == highestCost && !c.isEthereal) {
                candidateIndices.add(index);
            }
            index++;
        }

        if (candidateIndices.isEmpty()) {
            return;
        }
        Random rand = new Random();
        int chosenCardIndex = candidateIndices.get(rand.random(0, candidateIndices.size() - 1));
        AbstractCard highest = AbstractDungeon.player.hand.group.get(chosenCardIndex);
        highest.retain = true;
    }

	// Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
