package org.example.api.Dto;

import lombok.*;
import org.example.persistence.Entity.Parachute;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class ParachuteDTO extends Parachute {

    public ParachuteDTO(int cost, String name, String desc) {
        super(cost, name, desc);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
