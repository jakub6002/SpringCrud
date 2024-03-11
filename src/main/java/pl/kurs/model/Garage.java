package pl.kurs.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "cars")
@NoArgsConstructor
@Entity
@EqualsAndHashCode
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int places;
    private String address;
    private Boolean lpgAllowed;

    @OneToMany(mappedBy = "garage")
    private Set<Car> cars = new HashSet<>();


    public Garage(int places, String address, boolean lpgAllowed) {
        this.places = places;
        this.address = address;
        this.lpgAllowed = lpgAllowed;
    }

    public void addCar(Car car) {
        validateAddCar(car);
        cars.add(car);
        car.setGarage(this);
    }

    public void remove(Car car) {
        validateRemoveCar(car);
        car.setGarage(null);
        cars.remove(car);
    }

    private void validateAddCar(Car car) {
        if(cars.size() >= places){
            throw new IllegalStateException("GARAGE_IS_FULL");
        }
        if(!lpgAllowed && "LPG".equals(car.getFuelType())){
            throw new IllegalStateException("GARAGE_NOT_ACCEpT_LPG");
        }
    }

    private void validateRemoveCar(Car car) {
        if(Optional.ofNullable(car.getGarage()).map(garage -> garage.getId() != id).orElse(true)){
            throw new IllegalStateException("CAR_NOT_IN_GARAGE");
        }
    }
}
