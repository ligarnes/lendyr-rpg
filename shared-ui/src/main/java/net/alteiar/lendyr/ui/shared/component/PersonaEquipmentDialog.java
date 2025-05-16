package net.alteiar.lendyr.ui.shared.component;

import lombok.Builder;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.ui.shared.component.inventory.PersonaEquipment;

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
}
