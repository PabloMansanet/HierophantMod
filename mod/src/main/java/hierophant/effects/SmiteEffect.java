package hierophant.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;

import hierophant.HierophantMod;

public class SmiteEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private static final float DUR = 1.0F;
    private static AtlasRegion img;
    private boolean playedSfx = false;

    public SmiteEffect(float x, float y) {
        if (img == null) {
            img = ImageMaster.vfxAtlas.findRegion("combat/laserThick");
        }

        this.x = x;
        this.y = y;
        this.color = HierophantMod.HIEROPHANT_GOLD.cpy();
        this.duration = 0.5F;
        this.startingDuration = 0.2F;
    }

    public void update() {
        if (!this.playedSfx) {
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.SKY));
            this.playedSfx = true;
            CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
            CardCrawlGame.screenShake.rumble(1.0F);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration > this.startingDuration / 2.0F) {
            this.color.a = Interpolation.pow2In.apply(1.0F, 0.0F, this.duration - 0.5F);
        } else {
            this.color.a = Interpolation.pow2Out.apply(0.0F, 1.0F, this.duration);
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(0.5F, 0.7F, 1.0F, this.color.a));
        for (int i = 0; i < 4; i++) {
           sb.draw(img,
              this.x,
              this.y - (float)(img.packedHeight / 2),
              0.0F,
              (float)img.packedHeight / 2.0F,
              (float)img.packedWidth,
              (float)img.packedHeight,
              this.scale * 2.5F + MathUtils.random(-0.05F, 0.05F), // Scale X
              this.scale * 6.0F + MathUtils.random(-0.1F, 0.1F), // Scale y
              -22 //rotation
              );
        }

        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}

