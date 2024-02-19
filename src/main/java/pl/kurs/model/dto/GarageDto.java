package pl.kurs.model.dto;

import pl.kurs.model.Garage;

public record GarageDto(int id, int places, String address, Boolean lpgAllowed, int amountOfCars) {

    public static GarageDto from(Garage garage) {
        return new GarageDto(garage.getId(), garage.getPlaces(), garage.getAddress(), garage.getLpgAllowed(), garage.getCars().size());
    }
}
