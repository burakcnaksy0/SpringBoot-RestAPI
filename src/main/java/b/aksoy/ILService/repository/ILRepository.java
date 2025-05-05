package b.aksoy.ILService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import b.aksoy.ILService.entity.Il;

@Repository
public interface ILRepository extends JpaRepository<Il, String> {

	Optional<Il> findByName(String name);           // İsme göre tek bir il arar.
    List<Il> findAllByName(String name);  			// Belirli bir isme sahip tüm illeri getirir. (List döner)

}
