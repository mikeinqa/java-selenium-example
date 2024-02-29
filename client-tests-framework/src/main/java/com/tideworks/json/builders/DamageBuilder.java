package com.tideworks.json.builders;

import com.tideworks.json.objectmodel.Damage;
import com.tideworks.json.objectmodel.DamageObject;

import lombok.val;
import org.testng.Assert;

import java.util.ArrayList;

/** Builder for damage model. */
public class DamageBuilder {
  private Damage damage;

  private DamageBuilder() {
    damage = new Damage();
    damage.damages = new ArrayList<>();
  }

  /**
   * Create new builder.
   *
   * @return Return DamageBuilder
   */
  public static DamageBuilder create() {
    return new DamageBuilder();
  }

  /**
   * Set damaged flag.
   *
   * @param isDamaged Damaged value.
   * @return This builder.
   */
  public DamageBuilder isDamaged(boolean isDamaged) {
    damage.damaged = isDamaged;

    return this;
  }

  /**
   * Set up new DamageObject to damages list.
   *
   * @param location Damage location value.
   * @param type Damage type value.
   * @return This builder.
   */
  public DamageBuilder addDamage(String location, String type) {
    val newDamage = new DamageObject();
    newDamage.location = location;
    newDamage.type = type;

    return addDamage(newDamage);
  }

  /**
   * Set up new DamageObject to damages list.
   *
   * @param damageObject DamageObject which will be added to damages list.
   * @return This builder.
   */
  public DamageBuilder addDamage(DamageObject damageObject) {
    if (damageObject.location.length() > 4 || damageObject.type.length() > 4) {
      Assert.fail("Maximum length for location and type four symbols");
    }

    if (damage.damages.size() > 3) {
      Assert.fail("Container may have only four damage element");
    }

    damage.damages.add(damageObject);

    return this;
  }

  /**
   * Complete create Damage.
   *
   * @return Damage instance.
   */
  public Damage build() {
    return damage;
  }
}
