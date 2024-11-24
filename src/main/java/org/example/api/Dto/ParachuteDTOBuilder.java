package org.example.api.Dto;

public class ParachuteDTOBuilder {
    private int cost;
    private String name;
    private String description;

    // Setters for each field
    public ParachuteDTOBuilder setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public ParachuteDTOBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ParachuteDTOBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    // Build method to return the final ParachuteDTO
    public ParachuteDTO build() {
        return new ParachuteDTO(cost, name, description);
    }
}
