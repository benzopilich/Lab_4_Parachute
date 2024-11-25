package org.example.api.Dto;

import lombok.*;
import org.example.persistence.Entity.Parachute;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ParachuteDTO extends Parachute {

    public ParachuteDTO(){}

    public ParachuteDTO(int cost, String name, String desc) {
        super(cost, name, desc);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static ParachuteDTOBuilder builder(){
        return new ParachuteDTOBuilder();
    }

    public static class ParachuteDTOBuilder {
        private int cost;
        private String name;
        private String description;

        // Setters for each field

        public ParachuteDTOBuilder(){}

        public ParachuteDTOBuilder Cost(int cost) {
            this.cost = cost;
            return this;
        }

        public ParachuteDTOBuilder Name(String name) {
            this.name = name;
            return this;
        }

        public ParachuteDTOBuilder Description(String description) {
            this.description = description;
            return this;
        }

        // Build method to return the final ParachuteDTO
        public ParachuteDTO build() {
            return new ParachuteDTO(cost, name, description);
        }
    }

}


