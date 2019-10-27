//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package hierophant.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ImpactSparkEffect;

import hierophant.HierophantMod;

public class PunishLightningEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private AtlasRegion img = null;

    public PunishLightningEffect(float x, float y) {
        if (this.img == null) {
            this.img = ImageMaster.vfxAtlas.findRegion("combat/lightning");
        }

        this.x = x - (float)this.img.packedWidth / 2.0F;
        this.y = y;
        this.color = HierophantMod.HIEROPHANT_GOLD.cpy();
        this.duration = 0.5F;
        this.startingDuration = 0.5F;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            CardCrawlGame.screenShake.shake(ShakeIntensity.LOW, ShakeDur.MED, false);

            for(int i = 0; i < 30; ++i) {
                AbstractDungeon.topLevelEffectsQueue.add(new ImpactSparkEffect(this.x + MathUtils.random(-20.0F, 20.0F) * Settings.scale + 150.0F * Settings.scale, this.y + MathUtils.random(-20.0F, 20.0F) * Settings.scale));
                AbstractDungeon.topLevelEffectsQueue.add(new ImpactSparkEffect(this.x + 15.0F * Settings.scale + MathUtils.random(-20.0F, 20.0F) * Settings.scale + 150.0F * Settings.scale, this.y + MathUtils.random(-20.0F, 20.0F) * Settings.scale));
                AbstractDungeon.topLevelEffectsQueue.add(new ImpactSparkEffect(this.x - 15.0F * Settings.scale + MathUtils.random(-20.0F, 20.0F) * Settings.scale + 150.0F * Settings.scale, this.y + MathUtils.random(-20.0F, 20.0F) * Settings.scale));
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        this.color.a = Interpolation.bounceIn.apply(this.duration * 2.0F);
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, 0.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}

