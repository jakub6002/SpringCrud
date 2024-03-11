package pl.kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;

import java.awt.print.Book;
@Repository
public interface CarRepository extends JpaRepository<Car,Integer> {


}
