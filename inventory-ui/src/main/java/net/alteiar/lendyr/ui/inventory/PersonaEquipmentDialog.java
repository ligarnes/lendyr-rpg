package net.alteiar.lendyr.ui.inventory;

import lombok.Builder;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.ui.inventory.component.PersonaEquipment;
import net.alteiar.lendyr.ui.inventory.listener.ItemSlotListener;
import net.alteiar.lendyr.ui.shared.component.Dialog;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class PersonaEquipmentDialog extends Dialog<PersonaEquipment> {

  @Builder
  public PersonaEquipmentDialog(UiFactory uiFactory) {
    super(uiFactory);
    PersonaEquipment personaEquipment = PersonaEquipment.builder().uiFactory(uiFactory).build();
    this.setContent(personaEquipment);
    this.setVisible(false);
  }

  public void setPersona(PersonaEntity persona) {
    getContent().setPersona(persona);
    setDialogTitle("%s - equipments".formatted(persona.getName()));
  }

  public void setItemSlotListener(ItemSlotListener itemSlotListener) {
    getContent().setItemSlotListener(itemSlotListener);
  }
}
