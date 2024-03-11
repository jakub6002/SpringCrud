package pl.kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.model.Garage;

import java.util.List;
import java.util.Optional;

public interface GarageRepository extends JpaRepository<Garage, Integer> {
    @Query("select a from Garage a left join fetch a.cars")
    List<Garage> findAllWithCars();

    @Query("select a from Garage a left join fetch a.cars where a.id = ?1")
    Optional<Garage> findByIdWithCars(int id);
}
