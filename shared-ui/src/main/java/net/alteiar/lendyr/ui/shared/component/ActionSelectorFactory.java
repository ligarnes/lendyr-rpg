package net.alteiar.lendyr.ui.shared.component;

public interface ActionSelectorFactory {
  static final String MELEE_ATTACK_ICON = "icon/TabletopBadges_28.png";
  static final String RANGE_ATTACK_ICON = "icon/TabletopBadges_29.png";
  static final String MOVE_ICON = "icon/TabletopBadges_15.png";

  static enum Icon {
    MOVE, MELEE_ATTACK, RANGE_ATTACK
  }

  static ActionSelector create(UiFactory uiFactory, Icon icon) {
    return ActionSelector.builder().uiFactory(uiFactory)
      .icon(resolveIcon(icon))
      .build();
  }

  static String resolveIcon(Icon icon) {
    return switch (icon) {
      case MOVE -> MOVE_ICON;
      case MELEE_ATTACK -> MELEE_ATTACK_ICON;
      case RANGE_ATTACK -> RANGE_ATTACK_ICON;
    };
  }
}
